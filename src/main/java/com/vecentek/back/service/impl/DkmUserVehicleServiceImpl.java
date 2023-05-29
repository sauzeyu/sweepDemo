package com.vecentek.back.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.payneteasy.tlv.HexUtil;
import com.vecentek.back.constant.DiagnosticLogsEnum;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.DkmKeyLifecycle;
import com.vecentek.back.entity.DkmUser;
import com.vecentek.back.entity.DkmUserVehicle;
import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.exception.DiagnosticLogsException;
import com.vecentek.back.mapper.DkmBluetoothsMapper;
import com.vecentek.back.mapper.DkmKeyLifecycleMapper;
import com.vecentek.back.mapper.DkmKeyMapper;
import com.vecentek.back.mapper.DkmUserMapper;
import com.vecentek.back.mapper.DkmUserVehicleMapper;
import com.vecentek.back.mapper.DkmVehicleMapper;
import com.vecentek.back.util.DkDateUtils;
import com.vecentek.back.util.HMACUTILSHA256;
import com.vecentek.back.util.KeyLifecycleUtil;
import com.vecentek.back.vo.GetBluetoothVinVO;
import com.vecentek.back.vo.LogoutUserVehicleVO;
import com.vecentek.back.vo.RevokeKeyVO;
import com.vecentek.back.vo.ShareKeyVO;
import com.vecentek.back.vo.UserVehicleVO;
import com.vecentek.common.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.vecentek.back.util.ToolUtils.*;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-01-14 15:40
 */

@Service("dkmUserVehicleService")
@Slf4j
public class DkmUserVehicleServiceImpl {

    @Resource
    private DkmUserVehicleMapper dkmUserVehicleMapper;
    @Resource
    private DkmUserMapper dkmUserMapper;
    @Resource
    private DkmVehicleMapper dkmVehicleMapper;
    @Resource
    private DkmBluetoothsMapper dkmBluetoothsMapper;
    @Resource
    private DkmKeyMapper dkmKeyMapper;
    @Resource
    private DkmKeyLifecycleMapper dkmKeyLifecycleMapper;
    @Resource
    private KeyLifecycleUtil keyLifecycleUtil;


    @Transactional(rollbackFor = Exception.class)
    public PageResp insertUserVehicle(UserVehicleVO userVehicle) throws DiagnosticLogsException {
        log.info("request：" + "/api/userVehicle/insertUserVehicle " + userVehicle.toString());
        if (StrUtil.hasBlank(userVehicle.getUserId(), userVehicle.getVin())) {
            log.error("response：" + "/api/userVehicle/insertUserVehicle " + "上传失败，用户ID，VIN等必要参数未传递！");
            return PageResp.fail(2106, "上传失败，用户ID，VIN等必要参数未传递！");
        }
        LambdaQueryWrapper<DkmUser> userWrapper = Wrappers.<DkmUser>lambdaQuery().eq(DkmUser::getId, userVehicle.getUserId());
        DkmUser dkmUser = dkmUserMapper.selectOne(userWrapper);
        if (dkmUser == null) {
            dkmUser = new DkmUser();
            dkmUser.setId(userVehicle.getUserId());
            dkmUser.setPhone(userVehicle.getUserId());

            if (userVehicle.getUsername() != null) {
                dkmUser.setUsername(userVehicle.getUsername());
            }
            dkmUserMapper.insert(dkmUser);
        }
        LambdaQueryWrapper<DkmVehicle> vehicleWrapper = Wrappers.<DkmVehicle>lambdaQuery().eq(DkmVehicle::getVin, userVehicle.getVin());
        DkmVehicle dkmVehicle = dkmVehicleMapper.selectOne(vehicleWrapper);
        if (dkmVehicle == null) {
            log.info("response：" + "/api/userVehicle/insertUserVehicle " + "系统不存在该车辆信息！");

            return PageResp.fail(2106, "系统不存在该车辆信息！");
        }
        // 检查车辆vin唯一性
        LambdaQueryWrapper<DkmUserVehicle> dkmUserVehicleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dkmUserVehicleLambdaQueryWrapper.like(DkmUserVehicle::getVin, userVehicle.getVin());
        DkmUserVehicle dkmUserVehicle = dkmUserVehicleMapper.selectOne(dkmUserVehicleLambdaQueryWrapper);
        if (Objects.isNull(dkmUserVehicle)) {
            // 数据库中从未出现过，直接新增
            DkmUserVehicle dkmUserVehicle1 = new DkmUserVehicle();
            dkmUserVehicle1.setVehicleId(dkmVehicle.getId());
            dkmUserVehicle1.setUserId(dkmUser.getId());
            if (userVehicle.getBindTime() != null) {
                dkmUserVehicle1.setBindTime(userVehicle.getBindTime());
            } else {
                dkmUserVehicle1.setBindTime(new Date());
            }
            dkmUserVehicle1.setBindStatus(1);
            if (userVehicle.getLicense() != null) {
                dkmUserVehicle1.setLicense(userVehicle.getLicense());
            }
            dkmUserVehicle1.setVehicleType(userVehicle.getVehicleType());
            dkmUserVehicle1.setVin(userVehicle.getVin());
            dkmUserVehicle1.setPhone(userVehicle.getUserId());
            dkmUserVehicle1.setCreateTime(new Date());
            int insert = dkmUserVehicleMapper.insert(dkmUserVehicle1);
            if (insert == 1) {
                log.info("response：" + "/api/userVehicle/insertUserVehicle " + "上传成功");
                return PageResp.success("上传成功");
            }
        } else if (dkmUserVehicle.getBindStatus() == 1) {
            // 数据库中存在有绑定的车辆，要求先解绑再绑定
            log.info("response：" + "/api/userVehicle/insertUserVehicle " + "上传成功");
            throw DiagnosticLogsException.builder()
                    .businessId(DiagnosticLogsEnum.USER_VEHICLE_BINDING_THE_VEHICLE_BOUND_OWNER.getBusinessId())
                    .faultId(DiagnosticLogsEnum.USER_VEHICLE_BINDING_THE_VEHICLE_BOUND_OWNER.getFaultId())
                    .code(200)
                    .vin(userVehicle.getVin())
                    .userId(userVehicle.getUserId())
                    .build();


            //return PageResp.fail("当前车辆已存在车主，请先解绑后再绑定");
        } else { // 绑定状态为解绑改为绑定，执行更新操作，可能是过户更换车主
            dkmUserVehicle.setVehicleId(dkmVehicle.getId());
            dkmUserVehicle.setUserId(dkmUser.getId());
            dkmUserVehicle.setPhone(dkmUser.getId());
            if (userVehicle.getBindTime() != null) {
                dkmUserVehicle.setBindTime(userVehicle.getBindTime());
            } else {
                dkmUserVehicle.setBindTime(new Date());
            }
            dkmUserVehicle.setBindStatus(1);
            if (userVehicle.getLicense() != null) {
                dkmUserVehicle.setLicense(userVehicle.getLicense());
            }
            dkmUserVehicle.setUpdateTime(new Date());
            int i = dkmUserVehicleMapper.updateById(dkmUserVehicle);
            if (i == 1) {
                log.info("response：" + "/api/userVehicle/insertUserVehicle " + "上传成功");
                return PageResp.success("上传成功");
            }
        }
        log.info("response：" + "/api/userVehicle/insertUserVehicle " + "系统繁忙，请稍后再试！");
        return PageResp.fail("系统繁忙，请稍后再试！");
    }
    //public PageResp updateUserVehicle(UserChangeVO userChange) throws ParameterValidationException {
    //    log.info("request：" + "/api/userVehicle/updateUserVehicle " + userChange.toString());
    //    if (userChange == null || StringUtils.isBlank(userChange.getVin())) {
    //        throw new ParameterValidationException();
    //    }
    //    int i = dkmUserVehicleMapper.updateUserVehicle(userChange);
    //    if (i != 1) {
    //        throw new ParameterValidationException();
    //    }
    //    return PageResp.success("过户成功！");
    //}

    @Transactional(rollbackFor = Exception.class)
    public PageResp logoutUserVehicle(LogoutUserVehicleVO logoutUserVehicle) throws DiagnosticLogsException {
        log.info("request：" + "/api/userVehicle/logoutUserVehicle " + logoutUserVehicle.toString());
        String userId = logoutUserVehicle.getUserId();
        String vin = logoutUserVehicle.getVin();
        Date logoutTime;
        if (logoutUserVehicle.getLogoutTime() == null) {
            logoutTime = new Date();
        } else {
            logoutTime = logoutUserVehicle.getLogoutTime();
        }
        if (StrUtil.hasBlank(userId, vin)) {
            log.info("response：" + "/api/userVehicle/logoutUserVehicle " + "必填参数未传递!");
            return PageResp.fail(1001, "必填参数未传递或传入的参数格式不正确！");
        }
        // 根据userId和vin查询中间表
        DkmUserVehicle dkmUserVehicle = dkmUserVehicleMapper.selectOne(Wrappers.<DkmUserVehicle>lambdaQuery()
                .eq(DkmUserVehicle::getVin, vin)
                .eq(DkmUserVehicle::getUserId, userId)
                .eq(DkmUserVehicle::getBindStatus, 1));
        if (dkmUserVehicle == null) {
            // 用户与车辆信息不匹配
            log.info("response：" + "/api/userVehicle/logoutUserVehicle " + "用户与车辆信息不匹配!");
            return PageResp.fail(1001, "用户与车辆信息不匹配!");
        }
        // 根据vin查询车辆表
        DkmVehicle vehicle = dkmVehicleMapper.selectOne(Wrappers.<DkmVehicle>lambdaQuery().eq(DkmVehicle::getVin, vin));
        if (vehicle == null) {
            // 用户与车辆信息不匹配
            log.info("response：" + "/api/userVehicle/logoutUserVehicle " + "系统不存在该车辆信息!");
            return PageResp.fail(2106, "系统不存在该车辆信息!");
        }
        // 解绑 首先改变bind_status 的状态为2 然后吊销当前车辆的所有钥匙 返回钥匙用户id
        dkmUserVehicle.setBindStatus(2);
        dkmUserVehicle.setUnbindTime(logoutTime);
        dkmUserVehicleMapper.updateById(dkmUserVehicle);
        // 根据vin吊销车辆所有钥匙
        List<DkmKey> keys = dkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery().eq(DkmKey::getVin, vin).ne(DkmKey::getDkState, 5));
        ArrayList<String> userList = new ArrayList<>();
        for (DkmKey key : keys) {
            key.setDkState(5);
            //key.setUpdateTime(new Date());
            dkmKeyMapper.updateById(key);
            // 钥匙生命周期
            // 封装生命周期对象
            if (Objects.equals("0", key.getParentId())) {
                keyLifecycleUtil.insert(key, 1, 3, 5);
            } else {
                keyLifecycleUtil.insert(key, 2, 3, 5);
            }
            // 返回钥匙用户id
            userList.add(key.getUserId());
        }
        log.info("response：" + "/api/userVehicle/logoutUserVehicle " + "注销成功" + userList);
        return PageResp.success("注销成功", userList);
    }

    public PageResp getBluetoothVin(GetBluetoothVinVO getBluetoothVinVO) {
        log.info("request：" + "/api/userVehicle/getBluetoothVin " + getBluetoothVinVO.toString());
        String vin = getBluetoothVinVO.getVin();
        if (StrUtil.hasBlank(vin)) {
            log.info("response：" + "/api/userVehicle/getBluetoothVin " + "必填参数未传递!");
            return PageResp.fail(1001, "必填参数未传递或传入的参数格式不正确！");
        }
        // 根据vin查询车辆表 表查得到就是有蓝牙 查不到说明没有蓝牙
        DkmVehicle vehicle = dkmVehicleMapper.selectOne(Wrappers.<DkmVehicle>lambdaQuery().eq(DkmVehicle::getVin, vin));
        if (vehicle == null) {
            log.info("response：" + "/api/userVehicle/getBluetoothVin " + "不具有蓝牙钥匙功能!");
            return PageResp.success("不具有蓝牙钥匙功能!", false);
        }
        log.info("response：" + "/api/userVehicle/getBluetoothVin " + "具有蓝牙钥匙功能!");
        return PageResp.success("具有蓝牙钥匙功能!", true);
    }


    public PageResp revokeKey(RevokeKeyVO revokeKeyVO) throws DiagnosticLogsException {
        log.info("request：" + "/api/userVehicle/revokeKey " + revokeKeyVO.toString());
        if (StrUtil.hasBlank(revokeKeyVO.getUserId())) {
            log.info("response：" + "/api/userVehicle/revokeKey " + "必填参数未传递!");
            return PageResp.fail(1001, "必填参数未传递或传入的参数格式不正确！");
        }
        String userId = revokeKeyVO.getUserId();
        // 根据userId查询钥匙表 吊销相关正在使用的钥匙 不为5的全部吊销
        List<DkmKey> keys = dkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery().eq(DkmKey::getUserId, userId).ne(DkmKey::getDkState, "5"));
        // 返回【用户id-vin号】的list
        ArrayList<String> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(keys)) {
            return PageResp.fail(1001, "吊销失败,该用户下没有启动状态的钥匙");
        }
        for (DkmKey key : keys) {
            // 吊销
            key.setDkState(5);
            dkmKeyMapper.updateById(key);
            // 钥匙生命周期
            // 封装生命周期对象
            String parentId = key.getParentId();
            int keyType;
            // 车主钥匙
            if (Objects.equals("0", parentId)) {
                // 设备激活状态改为未激活
                //DkmVehicle vehicle = dkmVehicleMapper.selectOne(new LambdaQueryWrapper<DkmVehicle>().eq(DkmVehicle::getVin, key.getVin()));
                //if (!Objects.isNull(vehicle)) {
                //    DkmBluetooths dkmBluetooths = dkmBluetoothsMapper.selectOne(new LambdaQueryWrapper<DkmBluetooths>().eq(DkmBluetooths::getHwDeviceSn, vehicle.getHwDeviceSn()));
                //    if (!Objects.isNull(dkmBluetooths)) {
                //        dkmBluetooths.setDeviceStatus(0);
                //        dkmBluetooths.setUpdateTime(new Date());
                //        dkmBluetoothsMapper.updateById(dkmBluetooths);
                //    }
                //}
                keyType = 1;
                // 查询子钥匙吊销
                List<DkmKey> childList = dkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery().eq(DkmKey::getParentId, key.getId()).eq(DkmKey::getDkState, "1"));
                for (DkmKey child : childList) {
                    // 吊销子钥匙
                    child.setDkState(5);
                    //child.setUpdateTime(new Date());
                    dkmKeyMapper.updateById(child);
                    // 钥匙生命周期
                    // 封装生命周期对象
                    DkmKeyLifecycle childLifecycle = new DkmKeyLifecycle();
                    childLifecycle.setUserId(child.getUserId());
                    childLifecycle.setKeyId(child.getId());
                    childLifecycle.setVin(child.getVin());
                    // 操作来源
                    childLifecycle.setKeySource(3);
                    keyLifecycleUtil.insert(key, keyType, 3, 5);
                    // 加入list
                    list.add(child.getUserId() + "-" + child.getVin());
                }
            } else { // 分享钥匙
                keyType = 2;
                keyLifecycleUtil.insert(key, keyType, 3, 5);
            }
            // 加入list
            list.add(key.getUserId() + "-" + key.getVin());
        }
        log.info("response：" + "/api/userVehicle/revokeKey " + "吊销钥匙成功!" + list);
        return PageResp.success("吊销钥匙成功!", list);
    }

    /**
     * 分享钥匙
     * @param shareKeyVO
     * @return
     */
    public PageResp shareKey(ShareKeyVO shareKeyVO) throws DiagnosticLogsException {
        // 非空检验
        if (StringUtils.isEmpty(shareKeyVO.getUserId()) ||
                StringUtils.isEmpty(shareKeyVO.getVin()) ||
                StringUtils.isEmpty(shareKeyVO.getKeyId()) ||
                StringUtils.isEmpty(shareKeyVO.getShareUserId()) ||
                StringUtils.isEmpty(shareKeyVO.getPhoneFingerprint()) ||
                StringUtils.isEmpty(shareKeyVO.getValFrom()) ||
                StringUtils.isEmpty(shareKeyVO.getValTo()) ||
                Objects.isNull(shareKeyVO.getKeyPermit())){
            throw DiagnosticLogsException.builder()
                    .businessId(DiagnosticLogsEnum.SHARE_KEYS_USER_CENTER_KEY_PLATFORM_PARAMETERS_NULL.getBusinessId())
                    .faultId(DiagnosticLogsEnum.SHARE_KEYS_USER_CENTER_KEY_PLATFORM_PARAMETERS_NULL.getFaultId())
                    .build();
            //return PageResp.fail("传参中存在空值!");
        }
        // 时间格式校验
        DateTime valFrom;
        DateTime valTo;
        try {
            valFrom = DateUtil.parse(shareKeyVO.getValFrom(), "yyyy-MM-dd HH:mm:ss");
            valTo = DateUtil.parse(shareKeyVO.getValTo(), "yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
            e.printStackTrace();
            return PageResp.fail(1001,"钥匙生效或失效时间格式解析失败!");
        }
        // 检查是否自我分享
        if (Objects.equals(shareKeyVO.getUserId(),shareKeyVO.getShareUserId())){
            return PageResp.fail(1001,"禁止自己分享给自己!");
        }
        // 钥匙检查
        DkmKey dkmKey = dkmKeyMapper.selectOne(new LambdaQueryWrapper<DkmKey>().eq(DkmKey::getId, shareKeyVO.getKeyId()));
        if (Objects.isNull(dkmKey)){
            return PageResp.fail(1001,"钥匙信息为空!");
        }
        if (!Objects.equals(dkmKey.getParentId(),"0")){
            return PageResp.fail(1001,"钥匙为分享钥匙不能进行分享!");
        }
        if (!Objects.equals(dkmKey.getDkState(),1)){
            return PageResp.fail(1001,"钥匙状态异常不能分享!");
        }
        DkmVehicle dkmVehicle = dkmVehicleMapper.selectOne(new LambdaQueryWrapper<DkmVehicle>().eq(DkmVehicle::getVin, shareKeyVO.getVin()));
        if (Objects.isNull(dkmVehicle)){
            return PageResp.fail(1001,"车辆信息为空!");
        }
        // 查询是否已存在分享钥匙，如果存在即更新，不存在即新建
        DkmKey shareKey = dkmKeyMapper.selectOne(new LambdaQueryWrapper<DkmKey>()
                .eq(DkmKey::getParentId, shareKeyVO.getKeyId())
                .eq(DkmKey::getUserId, shareKeyVO.getShareUserId())
                .eq(DkmKey::getDkState, 1));
        byte keySlotRandom = (byte) (RandomUtil.randomInt(2, 6));
        //随机数
        int v = (int) (Math.random() * 100000000);
        String random = v + "";
        if (Objects.isNull(shareKey)){ // 新建
            DkmKey newKey = new DkmKey();
            newKey.setKeyResource(1);
            newKey.setId(DkDateUtils.getUnionId());
            newKey.setUserId(shareKeyVO.getShareUserId());
            newKey.setPhoneFingerprint(shareKeyVO.getPhoneFingerprint());
            newKey.setVehicleId(dkmVehicle.getId());
            newKey.setVin(shareKeyVO.getVin());
            newKey.setDkState(1);
            newKey.setKeyClassification(1);// 1 为 icce 2 为 ccc
            newKey.setActivateTimes(5);//目前后台默认设置五次，后期可能会改
            newKey.setValFrom(valFrom);
            newKey.setValTo(valTo);
            // 计算分享钥匙周期  [20220725T111120Z]
            long between = DateUtil.between(valFrom, valTo, DateUnit.MINUTE);
            newKey.setPeriod(between);
            newKey.setPermissions(shareKeyVO.getKeyPermit());
            newKey.setApplyTime(new Date());
            newKey.setParentId(shareKeyVO.getKeyId());
            byte[] buffer = new byte[0];
            byte[] permission = new byte[6];
            permission[0] = 0X49;
            permission[4] = 0X3B;
            buffer = byteMerger(buffer, byteMergerFull0(permission, 6));
            buffer = byteMerger(buffer, byteMergerFull0(random.getBytes(), 16));
            String startTime = getISO8601Timestamp(newKey.getValFrom());
            String endTime = getISO8601Timestamp(newKey.getValTo());
            buffer = byteMerger(buffer, byteMergerFull0(startTime.getBytes(), 16));
            buffer = byteMerger(buffer, byteMergerFull0(endTime.getBytes(), 16));
            buffer = byteMerger(buffer, byteMergerFull0(shareKeyVO.getVin().getBytes(), 17));
            buffer = byteMerger(buffer, byteMergerFull0(shareKeyVO.getPhoneFingerprint().getBytes(), 16));
            buffer = byteMerger(buffer, byteMergerFull0((shareKeyVO.getShareUserId() + "").getBytes(), 14));
            byte[] style = new byte[1];
            style[0] = 2;
            buffer = byteMerger(buffer, style);
            buffer = byteMerger(buffer, byteMergerFull0(shareKeyVO.getKeyId().getBytes(), 16));
            byte[] keySlot = new byte[1];
            keySlot[0] = keySlotRandom;
            buffer = byteMerger(buffer, keySlot);
            buffer = byteMerger(buffer, intToByteTwoByteArray(newKey.getActivateTimes()));
            buffer = byteMerger(buffer, byteMergerFull0(newKey.getId().getBytes(), 16));
            newKey.setKr(HexUtil.toHexString(buffer));
            //用K1生成ks
            Map map = dkmVehicleMapper.selectMasterKeyAndBleAddressByVin(shareKeyVO.getVin());
            String key1 = (String) map.get("master_key");
            String blMacAddress = (String) map.get("ble_mac_address");
            //使用KR和K1生成KS, 生成算法Low16(HMAC_SHA256(K1,KR))K1是密钥,取后16位作为KS
            String ksdata = HMACUTILSHA256.sha256_HMAC(buffer, HexUtil.parseHex(key1));
            String ks = ksdata.substring(ksdata.length() - 32, ksdata.length());
            newKey.setKs(ks);
            newKey.setBleMacAddress(blMacAddress);
            // 计算密钥K1
            String masterKey = dkmVehicleMapper.selectMasterKeyByVin(shareKeyVO.getVin());
            if (StringUtils.isBlank(masterKey)) {
                return PageResp.fail(1001,"蓝牙信息没有对应二级密钥!");
            }
            dkmKeyMapper.insert(newKey);
            // 新增钥匙生命周期表吊销记录
            // 根据用户id找到手机号
            // 封装生命周期对象
            keyLifecycleUtil.insert(newKey,2,2,1);
        }else { // 存在App分享钥匙即更新
            shareKey.setActivateTimes(5);//目前后台默认设置五次，后期可能会改
            shareKey.setValFrom(valFrom);
            shareKey.setValTo(valTo);
            // 计算分享钥匙周期
            long between = DateUtil.between(valFrom, valTo, DateUnit.MINUTE);
            shareKey.setPeriod(between);
            shareKey.setPermissions(shareKeyVO.getKeyPermit());
            shareKey.setApplyTime(new Date());
            byte[] buffer = new byte[0];
            byte[] permission = new byte[6];
            permission[0] = 0X49;
            permission[4] = 0X3B;
            buffer = byteMerger(buffer, byteMergerFull0(permission, 6));
            buffer = byteMerger(buffer, byteMergerFull0(random.getBytes(), 16));
            String startTime = getISO8601Timestamp(shareKey.getValFrom());
            String endTime = getISO8601Timestamp(shareKey.getValTo());
            buffer = byteMerger(buffer, byteMergerFull0(startTime.getBytes(), 16));
            buffer = byteMerger(buffer, byteMergerFull0(endTime.getBytes(), 16));
            buffer = byteMerger(buffer, byteMergerFull0(shareKeyVO.getVin().getBytes(), 17));
            buffer = byteMerger(buffer, byteMergerFull0(shareKeyVO.getPhoneFingerprint().getBytes(), 16));
            buffer = byteMerger(buffer, byteMergerFull0((shareKeyVO.getShareUserId() + "").getBytes(), 14));
            byte[] style = new byte[1];
            style[0] = 2;
            buffer = byteMerger(buffer, style);
            buffer = byteMerger(buffer, byteMergerFull0(shareKeyVO.getKeyId().getBytes(), 16));
            byte[] keySlot = new byte[1];
            keySlot[0] = keySlotRandom;
            buffer = byteMerger(buffer, keySlot);
            buffer = byteMerger(buffer, intToByteTwoByteArray(shareKey.getActivateTimes()));
            buffer = byteMerger(buffer, byteMergerFull0(shareKey.getId().getBytes(), 16));
            shareKey.setKr(HexUtil.toHexString(buffer));
            //用K1生成ks
            //用K1生成ks
            Map map = dkmVehicleMapper.selectMasterKeyAndBleAddressByVin(shareKeyVO.getVin());
            String key1 = (String) map.get("master_key");
            String blMacAddress = (String) map.get("ble_mac_address");
            //使用KR和K1生成KS, 生成算法Low16(HMAC_SHA256(K1,KR))K1是密钥,取后16位作为KS
            String ksdata = HMACUTILSHA256.sha256_HMAC(buffer, HexUtil.parseHex(key1));
            String ks = ksdata.substring(ksdata.length() - 32, ksdata.length());
            shareKey.setKs(ks);
            shareKey.setBleMacAddress(blMacAddress);
            dkmKeyMapper.updateById(shareKey);
        }
        return PageResp.success("分享成功!");
    }


//    public PageResp getAppScheme(SchemeVO schemeVO) {
//        String scheme = "myapp://";
//        String path = "user";
//        Map<String, String> params = new HashMap<>();
//        params.put("userId", "12345");
//        params.put("username", "johndoe");
//        params.put("email", "johndoe@example.com");
//
//        StringBuilder query = new StringBuilder();
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            if (query.length() > 0) {
//                query.append("&");
//            }
//            query.append(entry.getKey()).append("=").append(entry.getValue());
//        }
//        System.out.println((scheme + path + "?" + query));
//        return null;
//    }

    public String getAppScheme() {
        String baseUrl = "paraches://decorelink/share?keyId=1";
//        String userId = "12345";
//        String username = "johndoe";
//        String email = "johndoe@example.com";

//        return "<html><head><title>My Scheme Page</title></head><body>" +
//                "<a href=" + baseUrl + ">Open App</a></body>" +
//
//                "<script>\n" +
//                "        // 检测设备类型\n" +
//                "        function detectMobile() {\n" +
//                "            return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);\n" +
//                "        }\n" +
//                "\n" +
//                "        // 重定向到 App Scheme\n" +
//                "        function openApp() {\n" +
//                "            var schemeUrl = \"myapp://user?userId=12345&username=johndoe&email=johndoe@example.com\";\n" +
//                "            if (detectMobile()) {\n" +
//                "                var timeout;\n" +
//                "                var openTimestamp = Date.now();\n" +
//                "                var ifr = document.createElement('iframe');\n" +
//                "                ifr.src = schemeUrl;\n" +
//                "                ifr.style.display = 'none';\n" +
//                "                document.body.appendChild(ifr);\n" +
//                "                timeout = setTimeout(function() {\n" +
//                "                    var elapsedTime = Date.now() - openTimestamp;\n" +
//                "                    if (elapsedTime < 2000) {\n" +
//                "                        window.location.href = \"https://play.google.com/store/apps/details?id=com.example.myapp\"; // 应用商店链接\n" +
//                "                    }\n" +
//                "                }, 1500);\n" +
//                "            }\n" +
//                "        }\n" +
//                "    </script></html>";
        return "<html><head><title>My Scheme Page</title></head><body>" +
                "<a href=\"" + baseUrl + "\">打开应用</a>" +
                "</body></html>";
//        return "<html><head><title>My Scheme Page</title></head><body>" +
//                "<a href=\"" + baseUrl + "?key=" + key + "\">打开应用</a>" +
//                "</body></html>";
    }

    public String getIOSScheme() {
        String baseUrl = "vecentek://com.vecentek.deCoreLink?key=123";
        return "<html><head><title>My Scheme Page</title></head><body>" +
                "<a href=\"" + baseUrl + "\">打开应用</a>" +
                "</body></html>";
    }
}
