package com.vecentek.back.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.DkmKeyLifecycle;
import com.vecentek.back.entity.DkmUser;
import com.vecentek.back.entity.DkmUserVehicle;
import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.exception.ParameterValidationException;
import com.vecentek.back.mapper.DkmKeyLifecycleMapper;
import com.vecentek.back.mapper.DkmKeyMapper;
import com.vecentek.back.mapper.DkmUserMapper;
import com.vecentek.back.mapper.DkmUserVehicleMapper;
import com.vecentek.back.mapper.DkmVehicleMapper;
import com.vecentek.back.vo.*;
import com.vecentek.common.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    private static final int MAX_DATA_TOTAL = 50;

    @Resource
    private DkmUserVehicleMapper dkmUserVehicleMapper;
    @Resource
    private DkmUserMapper dkmUserMapper;
    @Resource
    private DkmVehicleMapper dkmVehicleMapper;
    @Resource
    private DkmKeyMapper dkmKeyMapper;
    @Resource
    private DkmKeyLifecycleMapper dkmKeyLifecycleMapper;

    @Transactional(rollbackFor = Exception.class)
    public PageResp insertUserVehicle(UserVehicleVO userVehicle) {
        if (StrUtil.hasBlank(userVehicle.getUserId(), userVehicle.getVin(), userVehicle.getUsername()) || userVehicle.getBindTime() == null) {
            log.info("response：" + "/api/userVehicle/insertUserVehicle " + "上传失败，用户ID，VIN，用户名，购车时间等必要参数未传递！");
            return PageResp.success("上传失败，用户ID，VIN，用户名，购车时间等必要参数未传递！");
        }
        LambdaQueryWrapper<DkmUser> userWrapper = Wrappers.<DkmUser>lambdaQuery().eq(DkmUser::getPhone, userVehicle.getUserId());

        DkmUser dkmUser = dkmUserMapper.selectOne(userWrapper);
        if (dkmUser == null) {
            dkmUser = new DkmUser();
            dkmUser.setPhone(userVehicle.getUserId());
            dkmUser.setUsername(userVehicle.getUsername());
            dkmUserMapper.insert(dkmUser);
        }
        LambdaQueryWrapper<DkmVehicle> vehicleWrapper = Wrappers.<DkmVehicle>lambdaQuery().eq(DkmVehicle::getVin, userVehicle.getVin());
        DkmVehicle dkmVehicle = dkmVehicleMapper.selectOne(vehicleWrapper);
        if (dkmVehicle == null) {
            log.info("response：" + "/api/userVehicle/insertUserVehicle " + "系统不存在该车辆信息！");
            return PageResp.fail(2106, "系统不存在该车辆信息！");
        }

        // 检查用户和vin唯一性
        LambdaQueryWrapper<DkmUserVehicle> dkmUserVehicleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dkmUserVehicleLambdaQueryWrapper.eq(DkmUserVehicle::getVin,userVehicle.getVin());
        DkmUserVehicle dkmUserVehicle1 = dkmUserVehicleMapper.selectOne(dkmUserVehicleLambdaQueryWrapper);
        LambdaQueryWrapper<DkmUserVehicle> dkmUserVehicleLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        dkmUserVehicleLambdaQueryWrapper1.eq(DkmUserVehicle::getUserId,userVehicle.getUserId());
        DkmUserVehicle dkmUserVehicle2 = dkmUserVehicleMapper.selectOne(dkmUserVehicleLambdaQueryWrapper1);
        if (dkmUserVehicle1 == null && dkmUserVehicle2 == null){
            DkmUserVehicle dkmUserVehicle = new DkmUserVehicle();
            dkmUserVehicle.setVehicleId(dkmVehicle.getId());
            dkmUserVehicle.setUserId(dkmUser.getId());
            dkmUserVehicle.setBindTime(userVehicle.getBindTime());
            dkmUserVehicle.setBindStatus(1);
            if (userVehicle.getLicense() != null){
                dkmUserVehicle.setLicense(userVehicle.getLicense());
            }
            dkmUserVehicle.setVehicleType(userVehicle.getVehicleType());
            dkmUserVehicle.setVin(userVehicle.getVin());
            dkmUserVehicle.setPhone(userVehicle.getUserId());
            dkmUserVehicle.setCreateTime(new Date());
            int insert = dkmUserVehicleMapper.insert(dkmUserVehicle);

            if (insert == 1) {
                log.info("response：" + "/api/userVehicle/insertUserVehicle " + "上传成功");
                return PageResp.success("上传成功");
            }
        }else {
            log.info("response：" + "/api/userVehicle/insertUserVehicle " + "系统已存在此VIN号，请勿重复绑定！");
            return PageResp.success("数据库已存在相同用户车辆关系！");
        }

        log.info("response：" + "/api/userVehicle/insertUserVehicle " + "系统繁忙，请稍后再试！");
        return PageResp.fail("系统繁忙，请稍后再试！");
    }

    public PageResp updateUserVehicle(UserChangeVO userChange) throws ParameterValidationException {
        if (userChange == null || StringUtils.isBlank(userChange.getVin())) {
            throw new ParameterValidationException();
        }
        int i = dkmUserVehicleMapper.updateUserVehicle(userChange);
        if (i != 1) {
            throw new ParameterValidationException();
        }
        return PageResp.success("过户成功！");
    }

    @Transactional(rollbackFor = Exception.class)
    public PageResp logoutUserVehicle(LogoutUserVehicleVO logoutUserVehicle) {
        String userId = logoutUserVehicle.getUserId();
        String vin = logoutUserVehicle.getVin();
        Date logoutTime = logoutUserVehicle.getLogoutTime();
        if (StrUtil.hasBlank(userId, vin)) {
            // 用户与车辆信息不匹配
            log.info("response：" + "/api/userVehicle/logoutUserVehicle " + "必填参数未传递!");
            return PageResp.fail(1001, "必填参数未传递!");
        }
        if (logoutTime == null) {
            logoutTime = new Date();
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
            key.setUpdateTime(new Date());
            dkmKeyMapper.updateById(key);
            // 钥匙生命周期
            // 封装生命周期对象
            // 根据用户id找到手机号
            DkmUser dkmUser = dkmUserMapper.selectById(userId);
            DkmKeyLifecycle dkmKeyLifecycle = new DkmKeyLifecycle();
            dkmKeyLifecycle.setUserId(dkmUser.getId().toString());
            dkmKeyLifecycle.setKeyId(key.getId());
            dkmKeyLifecycle.setVin(vin);
            // 操作来源
            dkmKeyLifecycle.setKeySource(3);
            String parentId = key.getParentId();
            int keyType;
            // 车主钥匙
            if (Objects.equals("0", parentId)) {
                keyType = 1;
            } else { // 分享钥匙
                keyType = 2;
            }
            dkmKeyLifecycle.setKeyType(keyType);
            // 吊销
            dkmKeyLifecycle.setKeyStatus(5);
            dkmKeyLifecycle.setCreateTime(new Date());
            dkmKeyLifecycleMapper.insert(dkmKeyLifecycle);
            // 返回钥匙用户id
            userList.add(key.getUserId());
        }
        log.info("response：" + "/api/userVehicle/logoutUserVehicle " + "注销成功" + userList);
        return PageResp.success("注销成功", userList);
    }

    public PageResp getBluetoothVin(GetBluetoothVinVO getBluetoothVinVO) {
        String vin = getBluetoothVinVO.getVin();
        if (StrUtil.hasBlank(vin)) {
            log.info("response：" + "/api/userVehicle/getBluetoothVin " + "必填参数未传递!");
            return PageResp.fail(1001, "必填参数未传递!");
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
        String userId = revokeKeyVO.getUserId();
        if (StrUtil.hasBlank(userId)) {
            log.info("response：" + "/api/userVehicle/revokeKey " + "必填参数未传递!");
            return PageResp.fail(1001, "必填参数未传递!");
        }
        // 根据userId查询钥匙表 吊销相关正在使用的钥匙
        List<DkmKey> keys = dkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery().eq(DkmKey::getUserId, userId).eq(DkmKey::getDkState, "1"));
        // 返回【用户id-vin号】的list
        ArrayList<String> list = new ArrayList<>();
        for (DkmKey key : keys) {
            // 吊销
            key.setDkState(5);
            key.setUpdateTime(new Date());
            dkmKeyMapper.updateById(key);
            // 钥匙生命周期
            // 封装生命周期对象
            DkmKeyLifecycle dkmKeyLifecycle = new DkmKeyLifecycle();
            dkmKeyLifecycle.setUserId(userId);
            dkmKeyLifecycle.setKeyId(key.getId());
            dkmKeyLifecycle.setVin(key.getVin());
            // 操作来源
            dkmKeyLifecycle.setKeySource(3);
            String parentId = key.getParentId();
            int keyType;
            // 车主钥匙
            if (Objects.equals("0", parentId)) {
                keyType = 1;
                // 查询子钥匙吊销
                List<DkmKey> childList = dkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery().eq(DkmKey::getParentId, key.getId()).eq(DkmKey::getDkState, "1"));
                for (DkmKey child : childList) {
                    // 吊销子钥匙
                    child.setDkState(5);
                    child.setUpdateTime(new Date());
                    dkmKeyMapper.updateById(child);
                    // 钥匙生命周期
                    // 封装生命周期对象
                    DkmKeyLifecycle childLifecycle = new DkmKeyLifecycle();
                    childLifecycle.setUserId(child.getUserId());
                    childLifecycle.setKeyId(child.getId());
                    childLifecycle.setVin(child.getVin());
                    // 操作来源
                    childLifecycle.setKeySource(3);
                    dkmKeyLifecycle.setKeyType(2);
                    dkmKeyLifecycle.setKeyStatus(5);
                    dkmKeyLifecycle.setCreateTime(new Date());
                    dkmKeyLifecycleMapper.insert(dkmKeyLifecycle);
                    // 加入list
                    list.add(child.getUserId() + "-" + child.getVin());
                }
            } else { // 分享钥匙
                keyType = 2;
            }
            dkmKeyLifecycle.setKeyType(keyType);
            // 吊销
            dkmKeyLifecycle.setKeyStatus(5);
            dkmKeyLifecycle.setCreateTime(new Date());
            dkmKeyLifecycleMapper.insert(dkmKeyLifecycle);
            // 加入list
            list.add(key.getUserId() + "-" + key.getVin());
        }
        log.info("response：" + "/api/userVehicle/revokeKey " + "吊销钥匙成功!" + list);
        return PageResp.success("吊销钥匙成功!", list);
    }
}
