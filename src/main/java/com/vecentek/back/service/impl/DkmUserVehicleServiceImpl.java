package com.vecentek.back.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.vecentek.back.entity.DkmBluetooths;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.DkmKeyLifecycle;
import com.vecentek.back.entity.DkmUser;
import com.vecentek.back.entity.DkmUserVehicle;
import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.mapper.DkmBluetoothsMapper;
import com.vecentek.back.mapper.DkmKeyLifecycleMapper;
import com.vecentek.back.mapper.DkmKeyMapper;
import com.vecentek.back.mapper.DkmUserMapper;
import com.vecentek.back.mapper.DkmUserVehicleMapper;
import com.vecentek.back.mapper.DkmVehicleMapper;
import com.vecentek.back.util.KeyLifecycleUtil;
import com.vecentek.back.vo.GetBluetoothVinVO;
import com.vecentek.back.vo.LogoutUserVehicleVO;
import com.vecentek.back.vo.RevokeKeyVO;
import com.vecentek.back.vo.UserVehicleVO;
import com.vecentek.common.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    public PageResp insertUserVehicle(UserVehicleVO userVehicle) {
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
            return PageResp.fail("当前车辆已存在车主，请先解绑后再绑定");
        } else { // 绑定状态为解绑改为绑定，执行更新操作，可能是过户更换车主
            dkmUserVehicle.setVehicleId(dkmVehicle.getId());
            dkmUserVehicle.setUserId(dkmUser.getId());
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
    public PageResp logoutUserVehicle(LogoutUserVehicleVO logoutUserVehicle) {
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
                .eq(DkmUserVehicle::getPhone, userId)
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


    public PageResp revokeKey(RevokeKeyVO revokeKeyVO) {
        log.info("request：" + "/api/userVehicle/revokeKey " + revokeKeyVO.toString());
        if (StrUtil.hasBlank(revokeKeyVO.getUserId())) {
            log.info("response：" + "/api/userVehicle/revokeKey " + "必填参数未传递!");
            return PageResp.fail(1001, "必填参数未传递或传入的参数格式不正确！");
        }
        String userId = revokeKeyVO.getUserId();
        // 根据userId查询钥匙表 吊销相关正在使用的钥匙
        List<DkmKey> keys = dkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery().eq(DkmKey::getUserId, userId).eq(DkmKey::getDkState, "1"));
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
                DkmVehicle vehicle = dkmVehicleMapper.selectOne(new LambdaQueryWrapper<DkmVehicle>().eq(DkmVehicle::getVin, key.getVin()));
                if (!Objects.isNull(vehicle)) {
                    DkmBluetooths dkmBluetooths = dkmBluetoothsMapper.selectOne(new LambdaQueryWrapper<DkmBluetooths>().eq(DkmBluetooths::getHwDeviceSn, vehicle.getHwDeviceSn()));
                    if (!Objects.isNull(dkmBluetooths)) {
                        dkmBluetooths.setDeviceStatus(0);
                        dkmBluetooths.setUpdateTime(new Date());
                        dkmBluetoothsMapper.updateById(dkmBluetooths);
                    }
                }
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
}
