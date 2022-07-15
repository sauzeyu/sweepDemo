package com.vecentek.back.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.constant.KeyErrorReasonEnum;
import com.vecentek.back.constant.KeyStatusCodeEnum;
import com.vecentek.back.dto.UploadBluetoothsErrorDTO;
import com.vecentek.back.dto.UploadDTO;
import com.vecentek.back.entity.*;
import com.vecentek.back.exception.ParameterValidationException;
import com.vecentek.back.exception.UploadOverMaximumException;
import com.vecentek.back.exception.VecentException;
import com.vecentek.back.mapper.*;
import com.vecentek.back.vo.*;
import com.vecentek.common.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 蓝牙数据(DkmBluetooths)表服务实现类
 *
 * @author EdgeYu
 * @version 1.0
 * @since 2022-01-10 14:48:00
 */

@Service("dkmOfflineCheckService")
@Slf4j
public class DkmOfflineCheckServiceImpl {

    private static final int MAX_DATA_TOTAL = 50;

    @Resource
    private DkmOfflineCheckMapper dkmOfflineCheckMapper;
    @Resource
    private DkmBluetoothsMapper dkmBluetoothsMapper;
    @Resource
    private DkmVehicleMapper dkmVehicleMapper;
    @Resource
    private DkmAftermarketReplacementMapper dkmAftermarketReplacementMapper;
    @Resource
    private DkmKeyMapper dkmKeyMapper;
    @Resource
    private DkmKeyLifecycleMapper dkmKeyLifecycleMapper;
    @Resource
    private DkmUserMapper dkmUserMapper;
    @Resource
    private DkmKeyLogMapper dkmKeyLogMapper;

    private void verifyVehicleBluetoothVO(List<VehicleBluetoothVO> dkmVehicles) throws VecentException {
        if (CollUtil.isEmpty(dkmVehicles)) {
            log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "上传数据不得为空！");
            throw new VecentException(1001, "上传数据不得为空！");
        }
        int startSize = dkmVehicles.size();

        if (startSize > MAX_DATA_TOTAL) {
            log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "上传数据量超过最大值，请控制在 50 条以内！");
            throw new VecentException(2107, "上传数据量超过最大值，请控制在 50 条以内！");
        }

        //对 dkmVehicles 去重,根据 hashCode 与 equals 去重
        dkmVehicles = dkmVehicles.stream().distinct().collect(Collectors.toList());
        // 如果有重复参数,则抛出异常
        if (startSize != dkmVehicles.size()) {
            log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "上传数据包含重复参数，请检查后上传！");
            throw new VecentException(1001, "上传数据包含重复参数，请检查后上传！");
        }

        for (VehicleBluetoothVO vehicle : dkmVehicles) {
            if (CharSequenceUtil.hasBlank(vehicle.getVin(),
                    vehicle.getVehicleModel(),
                    vehicle.getHwDeviceSn(),
                    vehicle.getSearchNumber(),
                    vehicle.getBleMacAddress(),
                    vehicle.getPubKey(),
                    vehicle.getDkSecUnitId(),
                    vehicle.getHwDeviceProviderNo())) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "必填参数未传递！");
                throw new VecentException(1001, "必填参数未传递！");
            }
            if (vehicle.getVin().length() != 17) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "VIN长度不正确！");
                throw new VecentException(1001, "VIN长度不正确！");
            }
            if (vehicle.getHwDeviceSn().length() != 40) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙设备序列号长度不正确！");
                throw new VecentException(1001, "蓝牙设备序列号长度不正确！");
            }
            if (vehicle.getSearchNumber().length() != 38) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙检索号长度不正确！");
                throw new VecentException(1001, "蓝牙检索号长度不正确！");
            }

            if (vehicle.getBleMacAddress().length() != 12) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙Mac地址长度不正确！");
                throw new VecentException(1001, "蓝牙Mac地址长度不正确！");
            }
            if (vehicle.getPubKey().length() != 130) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙公钥长度不正确！");
                throw new VecentException(1001, "蓝牙公钥长度不正确！");
            }
            if (HexUtil.decodeHex(vehicle.getHwDeviceSn()) == null) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙设备序列号格式不正确！");
                throw new VecentException(1001, "蓝牙设备序列号格式不正确！");
            }
            if (HexUtil.decodeHex(vehicle.getSearchNumber()) == null) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙检索号格式不正确！");
                throw new VecentException(1001, "蓝牙检索号格式不正确！");
            }

            if (HexUtil.decodeHex(vehicle.getBleMacAddress()) == null) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙Mac地址格式不正确！");
                throw new VecentException(1001, "蓝牙Mac地址格式不正确！");
            }

            if (HexUtil.decodeHex(vehicle.getPubKey()) == null) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙公钥格式不正确！");
                throw new VecentException(1001, "蓝牙公钥格式不正确！");
            }

            // 存在重复参数校验 主键索引导致 dkmVehicleMapper dkmBluetoothsMapper
            String vin = vehicle.getVin();
            List<DkmVehicle> dkmVehicles1 = dkmVehicleMapper.selectList(new QueryWrapper<DkmVehicle>().lambda().eq(DkmVehicle::getVin, vin));
            if ( dkmVehicles1.size() > 0){
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "存在重复的VIN号！");
                throw new VecentException(1001, "存在重复的VIN号！");
            }
            String hwDeviceSn = vehicle.getHwDeviceSn();
            List<DkmBluetooths> dkmBluetooths = dkmBluetoothsMapper.selectList(new QueryWrapper<DkmBluetooths>().lambda().eq(DkmBluetooths::getHwDeviceSn, hwDeviceSn));
            if ( dkmBluetooths.size() > 0){
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "存在重复的蓝牙设备号！");
                throw new VecentException(1001, "存在重复的蓝牙设备号！");
            }

        }


    }

    /**
     * 批量插入或更新车辆信息
     *
     * @param dkmVehicles 车辆信息集合
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public PageResp insertOrUpdateVehicleBatch(List<VehicleBluetoothVO> dkmVehicles) throws VecentException {
        //校验车辆蓝牙信息
        verifyVehicleBluetoothVO(dkmVehicles);
        //划分换件车辆和新增车辆
        List<String> alreadyExistsVehicleVinList = dkmOfflineCheckMapper.selectVehicleByVin(dkmVehicles);

        for (VehicleBluetoothVO dkmVehicle : dkmVehicles) {
            String hwDeviceSn = dkmVehicle.getHwDeviceSn();
            String vin = dkmVehicle.getVin();

        }
        List<VehicleBluetoothVO> newVehicleBluetoothList = dkmVehicles.stream()
                .filter(dkmVehicle -> !alreadyExistsVehicleVinList.contains(dkmVehicle.getVin()))
                .collect(Collectors.toList());
        List<VehicleBluetoothVO> aftermarketReplacementVehicleBluetoothList = dkmVehicles.stream()
                .filter(dkmVehicle -> alreadyExistsVehicleVinList.contains(dkmVehicle.getVin()))
                .collect(Collectors.toList());
        //当新增车辆存在时，插入新增车辆与蓝牙信息
        if (newVehicleBluetoothList.size() > 0) {
            newVehicleBluetoothList.forEach(vehicleBluetooth -> {
                DkmVehicle vehicle = new DkmVehicle();
                DkmBluetooths bluetooth = new DkmBluetooths();
                BeanUtil.copyProperties(vehicleBluetooth, vehicle);
                vehicle.setCreateTime(new Date());
                dkmVehicleMapper.insert(vehicle);
                BeanUtil.copyProperties(vehicleBluetooth, bluetooth);
                bluetooth.setCreateTime(new Date());
                bluetooth.setFlag(0);
                dkmBluetoothsMapper.insert(bluetooth);
            });
        }
        // 新增一个map返回给前端作为测试结果，正式环境删除
        ArrayList<Map> resList = new ArrayList<>();
        // 当换件车辆存在时，更新换件车辆并插入蓝牙信息
        if (aftermarketReplacementVehicleBluetoothList.size() > 0) {

            aftermarketReplacementVehicleBluetoothList.forEach(vehicleBluetooth -> {
                DkmVehicle vehicle = dkmVehicleMapper.selectOne(Wrappers.<DkmVehicle>lambdaQuery()
                        .eq(DkmVehicle::getVin, vehicleBluetooth.getVin()));

                List<DkmKey> keyList = dkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery()
                        .eq(DkmKey::getVin, vehicle.getVin()).eq(DkmKey::getDkState, "1"));
                //若换件车辆存在钥匙，则吊销钥匙，并记录钥匙生命周期
                if (keyList.size() > 0) {
                    keyList.forEach(dkmKey -> {
                        DkmUser dkmUser = dkmUserMapper.selectById(dkmKey.getUserId());
                        dkmKey.setDkState(5);
                        dkmKey.setUpdateTime(new Date());
                        dkmKeyMapper.updateById(dkmKey);
                        DkmKeyLifecycle dkmKeyLifecycle = new DkmKeyLifecycle();
                        dkmKeyLifecycle.setKeyId(dkmKey.getId());
                        dkmKeyLifecycle.setCreateTime(new Date());
                        dkmKeyLifecycle.setVin(dkmKey.getVin());
                        int keyType = 2;
                        ArrayList<String> userList = new ArrayList<>();
                        // 车主钥匙
                        if (Objects.equals("0", dkmKey.getParentId())) {
                            keyType = 1;
                            // 查询子钥匙吊销
                            List<DkmKey> childList = dkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery().eq(DkmKey::getParentId, dkmKey.getId()).eq(DkmKey::getDkState, "1"));
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
                                userList.add(child.getUserId());
                            }
                        }
                        // 加入list
                        userList.add(dkmKey.getUserId());
                        dkmKeyLifecycle.setKeyType(keyType);
                        dkmKeyLifecycle.setKeyStatus(5);
                        dkmKeyLifecycle.setKeySource(3);
                        dkmKeyLifecycle.setUserId(dkmUser.getId().toString());
                        dkmKeyLifecycleMapper.insert(dkmKeyLifecycle);
                        // 2.6 发送用户消息（换件后） 钥匙平台识别到蓝牙BOX换件之后，吊销当前车辆的所有钥匙，并发送用户信息给APP后台
                        // “vin“: “ ASDCSDASDADA1“,
                        // “userList”:[18202828282,15982637777,17237378989]

                        HashMap<String, Object> paramMap = new HashMap<>(16);
                        paramMap.put("vin", dkmKey.getVin());
                        paramMap.put("userList", userList);
                        resList.add(paramMap);
                        String urlString = "http://localhost:8007/dkserver-icce/dkm/wechat/recv";
                        HttpRequest.post(urlString).form(paramMap).execute().body();
                        log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "钥匙平台识别到蓝牙BOX换件之后，吊销当前车辆的所有钥匙，并发送用户信息给APP后台");
                    });
                }
                //更新旧蓝牙设备，插入新蓝牙设备，插入售后换件表
                DkmBluetooths newBluetooth = new DkmBluetooths();
                DkmBluetooths oldBluetooth = new DkmBluetooths();
                BeanUtil.copyProperties(vehicleBluetooth, vehicle);
                vehicle.setUpdateTime(new Date());
                newBluetooth.setCreateTime(new Date());
                newBluetooth.setFlag(1);
                oldBluetooth.setHwDeviceSn(vehicle.getHwDeviceSn());
                oldBluetooth.setFlag(1);
                oldBluetooth.setUpdateTime(new Date());
                dkmBluetoothsMapper.update(oldBluetooth, Wrappers.<DkmBluetooths>lambdaUpdate()
                        .eq(DkmBluetooths::getHwDeviceSn, vehicle.getHwDeviceSn()));
                dkmVehicleMapper.update(vehicle, Wrappers.<DkmVehicle>lambdaUpdate().eq(DkmVehicle::getVin, vehicleBluetooth.getVin()));
                BeanUtil.copyProperties(vehicleBluetooth, newBluetooth);
                dkmBluetoothsMapper.insert(newBluetooth);
                DkmAftermarketReplacement dkmAftermarketReplacement = new DkmAftermarketReplacement();
                dkmAftermarketReplacement.setVin(vehicleBluetooth.getVin());
                dkmAftermarketReplacement.setOldBluetoothSn(vehicle.getHwDeviceSn());
                dkmAftermarketReplacement.setNewBluetoothSn(vehicleBluetooth.getHwDeviceSn());
                dkmAftermarketReplacement.setReplacementTime(new Date());
                dkmAftermarketReplacement.setCreateTime(new Date());
                newBluetooth.setFlag(0);
                dkmAftermarketReplacementMapper.insert(dkmAftermarketReplacement);
            });
        }
        log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "上传成功");
        return PageResp.success("上传成功",resList);
    }

    /**
     * 批量插入蓝牙设备信息
     *
     * @param dkmBluetooths 蓝牙设备信息
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public PageResp insertBluetoothBatch(List<DkmBluetooths> dkmBluetooths) throws ParameterValidationException, UploadOverMaximumException {
        List<String> insertDeviceSnList = new ArrayList<>();
        List<String> insertMacList = new ArrayList<>();
        List<UploadBluetoothsErrorDTO> errorList = new ArrayList<>();
        int startNum = dkmBluetooths.size();
        for (DkmBluetooths bluetooth : dkmBluetooths) {
            if (CharSequenceUtil.hasBlank(bluetooth.getHwDeviceSn(), bluetooth.getHwDeviceProviderNo(), bluetooth.getDkSdkVersion(), bluetooth.getDkSecUnitId(), bluetooth.getBleMacAddress())) {
                throw new ParameterValidationException();
            }
        }
        int initSize = dkmBluetooths.size();
        if (initSize > MAX_DATA_TOTAL) {
            throw new UploadOverMaximumException();
        }
        //对 dkmVehicles 去重,根据 hashCode 与 equals 去重
        dkmBluetooths = dkmBluetooths.stream().distinct().collect(Collectors.toList());
        // 如果有重复参数,则抛出异常
        if (initSize != dkmBluetooths.size()) {
            throw new ParameterValidationException();
        }
        dkmBluetooths.forEach(bluetooth -> insertDeviceSnList.add(bluetooth.getHwDeviceSn()));
        dkmBluetooths.forEach(bluetooth -> insertMacList.add(bluetooth.getBleMacAddress()));
        List<String> alreadyExistDeviceSnList = dkmOfflineCheckMapper.selectBluetoothBatchByHwDeviceSn(insertDeviceSnList);
        List<String> alreadyExistMacList = dkmOfflineCheckMapper.selectBluetoothBatchByBleMacAddress(insertMacList);
        // 排除已存在蓝牙序列号设备
        if (CollUtil.isNotEmpty(alreadyExistDeviceSnList)) {
            alreadyExistDeviceSnList.forEach(deviceSn -> {
                UploadBluetoothsErrorDTO uploadBluetoothsErrorDTO = new UploadBluetoothsErrorDTO();
                uploadBluetoothsErrorDTO.setDescription("蓝牙设备序列号已存在！");
                uploadBluetoothsErrorDTO.setHwDeviceSn(deviceSn);
                errorList.add(uploadBluetoothsErrorDTO);
            });
            dkmBluetooths = dkmBluetooths.stream().filter(bluetooth -> !alreadyExistDeviceSnList.contains(bluetooth.getHwDeviceSn())).collect(Collectors.toList());
        }
        // 排除已存在蓝牙Mac地址设备
        if (CollUtil.isNotEmpty(alreadyExistMacList)) {
            alreadyExistMacList.forEach(mac -> {
                UploadBluetoothsErrorDTO uploadBluetoothsErrorDTO = new UploadBluetoothsErrorDTO();
                uploadBluetoothsErrorDTO.setDescription("蓝牙设备Mac地址已存在！");
                uploadBluetoothsErrorDTO.setBleMacAddress(mac);
                errorList.add(uploadBluetoothsErrorDTO);
            });
            dkmBluetooths = dkmBluetooths.stream().filter(bluetooth -> !alreadyExistMacList.contains(bluetooth.getBleMacAddress())).collect(Collectors.toList());
        }
        int endNum = dkmBluetooths.size();
        for (DkmBluetooths bluetooth : dkmBluetooths) {
            // TODO: 从密码机中获取密钥并放置到Hmac算法中
            HMac hMac = new HMac(HmacAlgorithm.HmacSHA256);
            String digKey = hMac.digestHex(bluetooth.getHwDeviceSn());
            bluetooth.setDigKey(digKey);
            bluetooth.setMasterKey(digKey.substring(32));
        }
        int successfulNum = 0;
        if (CollUtil.isNotEmpty(dkmBluetooths)) {
            successfulNum = dkmOfflineCheckMapper.insertBluetoothBatch(dkmBluetooths);
        }
        if (startNum != endNum) {
            return PageResp.fail(2106, "批量上传未完全成功", new UploadDTO<UploadBluetoothsErrorDTO>().buildUserVehicleDTO(successfulNum, errorList));
        }
        return PageResp.success("上传成功");
    }

    public PageResp getKeyLogDetail(KeyLogDetailVO keyLogDetailVO) {
        ArrayList<KeyLogDetailResVO> res = new ArrayList<>();
        if(keyLogDetailVO.getPageIndex() == null || keyLogDetailVO.getPageSize() == null){
            log.info("response：" + "/api/offlineCheck/getKeyLogDetail " + "必填参数未传递或传入的参数格式不正确！");
            return PageResp.fail(1001,"必填参数未传递或传入的参数格式不正确！");
        }
        Page<DkmKeyLog> page = new Page<>(keyLogDetailVO.getPageIndex(), keyLogDetailVO.getPageSize());
        // 入参检查
//        if(StrUtil.isBlank(keyLogDetailVO.getStartTime()) || StrUtil.isBlank(keyLogDetailVO.getEndTime())){
//            log.info("response：" + "/api/offlineCheck/getKeyLogDetail " + "必填参数未传递或传入的参数格式不正确！");
//            return PageResp.fail(1001,"必填参数未传递或传入的参数格式不正确！");
//        }
        LambdaQueryWrapper<DkmKeyLog> dkmKeyLogLambdaQueryWrapper = new QueryWrapper<DkmKeyLog>().lambda()
                .eq(StrUtil.isNotBlank(keyLogDetailVO.getVin()),DkmKeyLog::getVin, keyLogDetailVO.getVin())
                .ge(DkmKeyLog::getOperateTime, keyLogDetailVO.getStartTime())
                .le(DkmKeyLog::getOperateTime, keyLogDetailVO.getEndTime())
                .eq(StrUtil.isNotBlank(keyLogDetailVO.getUserId()), DkmKeyLog::getUserId, keyLogDetailVO.getUserId())
                .eq(StrUtil.isNotBlank(keyLogDetailVO.getStatusCode()), DkmKeyLog::getStatusCode, keyLogDetailVO.getStatusCode())
                .orderBy(true, true, DkmKeyLog::getOperateTime);
        // 查询
        page = dkmKeyLogMapper.selectPage(page, dkmKeyLogLambdaQueryWrapper);
        // 转为结果对象
        for (DkmKeyLog dkmKeyLog : page.getRecords()) {
            KeyLogDetailResVO keyLogDetailResVO = new KeyLogDetailResVO();
            // 赋值对象
            BeanUtils.copyProperties(dkmKeyLog, keyLogDetailResVO);
            // 根据枚举对应
            keyLogDetailResVO.setStatusName(KeyStatusCodeEnum.matchName(dkmKeyLog.getStatusCode()));
            keyLogDetailResVO.setErrorReasonName(KeyErrorReasonEnum.matchReason(dkmKeyLog.getErrorReason()));
            res.add(keyLogDetailResVO);
        }
        log.info("response：" + "/api/offlineCheck/getKeyLogDetail " + "查询成功" + res);
        return PageResp.success("查询成功",page.getTotal(),res);
    }

    public PageResp getKeyData(KeyLogDataVO keyLogDataVO) {
        // 入参检查
        if(ObjectUtil.isNull(keyLogDataVO.getStartTime()) ||
                ObjectUtil.isNull(keyLogDataVO.getEndTime()) ||
                Objects.isNull(keyLogDataVO.getPageIndex()) ||
                Objects.isNull(keyLogDataVO.getPageSize())){
            log.info("response：" + "/api/offlineCheck/getKeyData " + "必填参数未传递或传入的参数格式不正确！");
            return PageResp.fail(1001,"必填参数未传递或传入的参数格式不正确！");
        }
        Page<DkmKey> page = new Page<>(keyLogDataVO.getPageIndex(), keyLogDataVO.getPageSize());
        boolean parentId = false;
        if (keyLogDataVO.getStatus() != null){
            parentId = keyLogDataVO.getStatus() == 1 ? true:false;
        }
        LambdaQueryWrapper<DkmKey> wrapper = new QueryWrapper<DkmKey>().lambda()
                .ge(DkmKey::getValFrom, keyLogDataVO.getStartTime())
                .le(DkmKey::getValTo, keyLogDataVO.getEndTime())
                .eq(StrUtil.isNotBlank(keyLogDataVO.getVin()),DkmKey::getVin, keyLogDataVO.getVin())
                .eq(StrUtil.isNotBlank(keyLogDataVO.getUserId()), DkmKey::getUserId, keyLogDataVO.getUserId())
                .eq(keyLogDataVO.getDkState() != null, DkmKey::getDkState, keyLogDataVO.getDkState())
                .eq(parentId, DkmKey::getDkState, "0");
        page = dkmKeyMapper.selectPage(page,wrapper);
        ArrayList<KeyLogDataResVO> res = new ArrayList<>();
        for (DkmKey dkmKey : page.getRecords()) {
            KeyLogDataResVO keyLogDataResVO = new KeyLogDataResVO();
            BeanUtil.copyProperties(dkmKey,keyLogDataResVO);
            keyLogDataResVO.setKeyId(dkmKey.getId());
            res.add(keyLogDataResVO);
        }
        log.info("response：" + "/api/offlineCheck/getKeyData " + res);
        return PageResp.success("查询成功",page.getTotal(),res);
    }
}
