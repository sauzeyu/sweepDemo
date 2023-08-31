package com.vecentek.back.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.constant.DiagnosticLogsEnum;
import com.vecentek.back.constant.KeyErrorReasonEnumJac;
import com.vecentek.back.constant.KeyLifecycleResourceConstant;
import com.vecentek.back.constant.DiagnosticLogsEnum;
import com.vecentek.back.constant.KeyErrorReasonEnumJac;
import com.vecentek.back.constant.KeyLifecycleResourceConstant;
import com.vecentek.back.constant.KeyStatusCodeEnum;
import com.vecentek.back.dto.UploadBluetoothsErrorDTO;
import com.vecentek.back.dto.UploadDTO;
import com.vecentek.back.entity.*;
import com.vecentek.back.exception.DiagnosticLogsException;
import com.vecentek.back.exception.ParameterValidationException;
import com.vecentek.back.exception.UploadOverMaximumException;
import com.vecentek.back.exception.VecentException;
import com.vecentek.back.mapper.*;
import com.vecentek.back.vo.*;
import com.vecentek.common.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
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
    private DkmKeyLogMapper dkmKeyLogMapper;
    @Value(value = "${spring.profiles.active}")
    private String currentBranch;
    private void verifyVehicleBluetoothVO(List<VehicleBluetoothVO> dkmVehicles) throws VecentException, DiagnosticLogsException {
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
            try {
                byte[] bytes = Hex.decodeHex(vehicle.getHwDeviceSn());
                new String(bytes, StandardCharsets.US_ASCII);
            } catch (DecoderException e) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙设备序列号格式不正确！");
                throw new VecentException(1001, "蓝牙设备序列号格式不正确！");
            }

            boolean pubKeyValidFlag = true;
            if (CharSequenceUtil.hasBlank(vehicle.getPubKey())){

                if (!currentBranch.contains("jac")) {
                    throw new VecentException(1001, "必填参数未传递！");
                }

                pubKeyValidFlag = false;
            }

            if (CharSequenceUtil.hasBlank(vehicle.getVin(),
                    vehicle.getVehicleModel(),
                    vehicle.getHwDeviceSn(),
                    vehicle.getBleMacAddress())

            ) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "必填参数未传递！");
                throw new VecentException(1001, "必填参数未传递！");
            }
            if (CharSequenceUtil.isNotBlank(vehicle.getSearchNumber())) {
                if (vehicle.getSearchNumber().length() != 38) {
                    log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙检索号长度不正确！");
                    throw new VecentException(1001, "蓝牙检索号长度不正确！");
                }

                LambdaQueryWrapper<DkmBluetooths> queryWrapper = Wrappers.lambdaQuery(DkmBluetooths.class)
                        .eq(DkmBluetooths::getSearchNumber, vehicle.getSearchNumber());

                // 根据SearchNumberWrapper条件查找是否存在相同的搜索号
                if (dkmBluetoothsMapper.selectCount(queryWrapper) > 0) {
                    log.info("response：/api/offlineCheck/insertOrUpdateVehicleBatch 蓝牙检索号不是唯一的！");
                    throw new VecentException(1001, "蓝牙检索号不是唯一的！");
                }

            }

            if (vehicle.getVin().length() != 17) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "VIN长度不正确！");
                throw new VecentException(1001, "VIN长度不正确！");
            }
            // 使用正则表达式 [a-zA-Z0-9]+ 来匹配只包含数字和字母的字符串
            String pattern = "^[a-zA-Z0-9]+$";
            if (!Pattern.matches(pattern,vehicle.getVin())) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "必填参数未传递或传入的参数格式不正确！");
                throw new VecentException(1001, "必填参数未传递或传入的参数格式不正确！");
            }

            if (vehicle.getBleMacAddress().length() != 12) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙Mac地址长度不正确！");
                throw new VecentException(1001, "蓝牙Mac地址长度不正确！");
            }

            if (pubKeyValidFlag && vehicle.getPubKey().length() != 130) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙公钥长度不正确！");
                throw new VecentException(1001, "蓝牙公钥长度不正确！");
            }


            if (!com.vecentek.back.util.HexUtil.isAsciiHexString(vehicle.getHwDeviceSn()) || HexUtil.decodeHex(vehicle.getHwDeviceSn()) == null) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙设备序列号格式不正确！");
                throw new VecentException(1001, "蓝牙设备序列号格式不正确！");
            }

            if (CharSequenceUtil.isNotBlank(vehicle.getSearchNumber())) {
                if (!com.vecentek.back.util.HexUtil.isAsciiHexString(vehicle.getSearchNumber()) || HexUtil.decodeHex(vehicle.getSearchNumber()) == null) {
                    log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙检索号格式不正确！");
                    throw new VecentException(1001, "蓝牙检索号格式不正确！");
                }

            }
            if (!com.vecentek.back.util.HexUtil.isAsciiHexString(vehicle.getBleMacAddress()) || HexUtil.decodeHex(vehicle.getBleMacAddress()) == null) {
            }
            if (!com.vecentek.back.util.HexUtil.isAsciiHexString(vehicle.getBleMacAddress()) || HexUtil.decodeHex(vehicle.getBleMacAddress()) == null) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙Mac地址格式不正确！");
                throw new VecentException(1001, "蓝牙Mac地址格式不正确！");
            }

            if (pubKeyValidFlag && (!com.vecentek.back.util.HexUtil.isAsciiHexString(vehicle.getPubKey()) || HexUtil.decodeHex(vehicle.getPubKey()) == null)) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "蓝牙公钥格式不正确！");
                throw new VecentException(1001, "蓝牙公钥格式不正确！");
            }

//             存在重复参数校验 主键索引重复导致插入失败 dkmVehicleMapper dkmBluetoothsMapper
            String vin = vehicle.getVin();
            String hwDeviceSn = vehicle.getHwDeviceSn();
            List<DkmVehicle> dkmVehicles1 = dkmVehicleMapper.selectList(new QueryWrapper<DkmVehicle>().lambda()
                    .eq(ObjectUtil.isNotNull(vin), DkmVehicle::getVin, vin)
                    .eq(ObjectUtil.isNotNull(hwDeviceSn), DkmVehicle::getHwDeviceSn, hwDeviceSn));
            if (dkmVehicles1.size() > 0) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "存在重复的VIN号！");
                throw new VecentException(1001, "存在重复成对的VIN号和蓝牙序列号，请勿重复插入！");
            }
            List<DkmBluetooths> dkmBluetooths = dkmBluetoothsMapper.selectList(new QueryWrapper<DkmBluetooths>().lambda().eq(DkmBluetooths::getHwDeviceSn, hwDeviceSn));
            if (dkmBluetooths.size() > 0) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "存在重复的TBOX SN号！");
                throw new VecentException(1001, "存在重复的TBOX SN号！");
            }
            String searchNumber = vehicle.getSearchNumber();
            List<DkmBluetooths> dkmBluetooths1 = dkmBluetoothsMapper.selectList(new QueryWrapper<DkmBluetooths>().lambda().eq(DkmBluetooths::getSearchNumber, searchNumber));
            if (dkmBluetooths1.size() > 0) {
                log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "存在重复的蓝牙检索号！");
                throw new VecentException(1001, "存在重复的蓝牙检索号！");
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
    public PageResp insertOrUpdateVehicleBatch(List<VehicleBluetoothVO> dkmVehicles) throws Exception {
        log.info("request：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + dkmVehicles.toString());
        //校验车辆蓝牙信息
        verifyVehicleBluetoothVO(dkmVehicles);
        dkmVehicles.stream().filter(vehicle -> CharSequenceUtil.isBlank(vehicle.getSearchNumber()))
                .forEach(vehicle -> {
                            MessageDigest messageDigest = null;
                            try {
                                messageDigest = MessageDigest.getInstance("SHA-256");
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            byte[] hash = messageDigest.digest(com.payneteasy.tlv.HexUtil.parseHex(vehicle.getHwDeviceSn()));
                            String encdeStr = Hex.encodeHexString(hash);
                            String searchNumber = encdeStr.substring(encdeStr.length() - 38);
                            vehicle.setSearchNumber(searchNumber);
                        }
                );
        //划分换件车辆和新增车辆
        List<String> alreadyExistsVehicleVinList = dkmOfflineCheckMapper.selectVehicleByVin(dkmVehicles);

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
                bluetooth.setFlag(1);
                bluetooth.setDeviceStatus(0);
                // TODO: 从密码机中获取密钥并放置到Hmac算法中
                HMac hMac = new HMac(HmacAlgorithm.HmacSHA256);
                String digKey = hMac.digestHex(bluetooth.getHwDeviceSn());
                bluetooth.setDigKey(digKey);
                bluetooth.setMasterKey(digKey.substring(32));
                dkmBluetoothsMapper.insert(bluetooth);
            });
        }
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
                        dkmKey.setDkState(5);
                        //dkmKey.setUpdateTime(new Date());
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
                        dkmKeyLifecycle.setKeySource(KeyLifecycleResourceConstant.AFTERMARKET_WREPLACEMENT);
                        dkmKeyLifecycle.setUserId(dkmKey.getUserId());
                        dkmKeyLifecycleMapper.insert(dkmKeyLifecycle);
                        log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "钥匙平台识别到蓝牙BOX换件之后，吊销当前车辆的所有钥匙，并发送用户信息给APP后台");
                    });
                }
                //更新旧蓝牙设备，插入新蓝牙设备，插入售后换件表
                DkmAftermarketReplacement dkmAftermarketReplacement = new DkmAftermarketReplacement();
                dkmAftermarketReplacement.setOldBluetoothSn(vehicle.getHwDeviceSn());
                DkmBluetooths newBluetooth = new DkmBluetooths();
                DkmBluetooths oldBluetooth = new DkmBluetooths();
                try {
                    oldBluetooth.setHwDeviceSn(vehicle.getHwDeviceSn());
                } catch (VecentException e) {
                    e.printStackTrace();
                }
                BeanUtil.copyProperties(vehicleBluetooth, vehicle);
                BeanUtil.copyProperties(vehicleBluetooth, newBluetooth);

                vehicle.setUpdateTime(new Date());
                newBluetooth.setCreateTime(new Date());
                newBluetooth.setFlag(1); // 启用
                oldBluetooth.setFlag(0); // 报废
                oldBluetooth.setUpdateTime(new Date());

                // TODO: 从密码机中获取密钥并放置到Hmac算法中
                newBluetooth.setDeviceStatus(0);
                HMac hMac = new HMac(HmacAlgorithm.HmacSHA256);
                String digKey = hMac.digestHex(newBluetooth.getHwDeviceSn());
                newBluetooth.setDigKey(digKey);
                newBluetooth.setMasterKey(digKey.substring(32));
                dkmBluetoothsMapper.update(oldBluetooth, Wrappers.<DkmBluetooths>lambdaUpdate()
                        .eq(DkmBluetooths::getHwDeviceSn, oldBluetooth.getHwDeviceSn()));

                dkmBluetoothsMapper.insert(newBluetooth);

                dkmAftermarketReplacement.setVin(vehicleBluetooth.getVin());

                dkmVehicleMapper.update(vehicle, Wrappers.<DkmVehicle>lambdaUpdate().eq(DkmVehicle::getVin, vehicleBluetooth.getVin()));

                dkmAftermarketReplacement.setNewBluetoothSn(vehicleBluetooth.getHwDeviceSn());
                dkmAftermarketReplacement.setReplacementTime(new Date());
                dkmAftermarketReplacement.setCreateTime(new Date());
                dkmAftermarketReplacementMapper.insert(dkmAftermarketReplacement);
            });
        }


        log.info("response：" + "/api/offlineCheck/insertOrUpdateVehicleBatch " + "上传成功");
        return PageResp.success("上传成功");
    }

    /**
     * 批量插入蓝牙设备信息
     *
     * @param dkmBluetooths 蓝牙设备信息
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public PageResp insertBluetoothBatch(List<DkmBluetooths> dkmBluetooths) throws ParameterValidationException, UploadOverMaximumException, DiagnosticLogsException {
        log.info("request：" + "/api/offlineCheck/insertBluetoothBatch " + dkmBluetooths.toString());
        List<String> insertDeviceSnList = new ArrayList<>();
        List<String> insertMacList = new ArrayList<>();
        List<UploadBluetoothsErrorDTO> errorList = new ArrayList<>();
        int startNum = dkmBluetooths.size();
        for (DkmBluetooths bluetooth : dkmBluetooths) {
            if (CharSequenceUtil.hasBlank(bluetooth.getHwDeviceSn(), bluetooth.getDkSdkVersion(), bluetooth.getBleMacAddress())) {
                throw DiagnosticLogsException.builder()
                        .businessId(DiagnosticLogsEnum.OWNER_CANCELLATION_UNBINDING_PARAMETERS_NULL.getBusinessId())
                        .faultId(DiagnosticLogsEnum.OWNER_CANCELLATION_UNBINDING_PARAMETERS_NULL.getFaultId())
                        .code(1001)
                        .build();
                //throw new ParameterValidationException();
            }
        }
        int initSize = dkmBluetooths.size();
        if (initSize > MAX_DATA_TOTAL) {
            throw DiagnosticLogsException.builder()
                    .businessId(DiagnosticLogsEnum.ENTER_VEHICLE_REPLACEMENT_UPLOAD_MAXIMUM.getBusinessId())
                    .faultId(DiagnosticLogsEnum.ENTER_VEHICLE_REPLACEMENT_UPLOAD_MAXIMUM.getFaultId())
                    .code(2107)
                    .build();
            //throw new UploadOverMaximumException();
        }
        //对 dkmVehicles 去重,根据 hashCode 与 equals 去重
        dkmBluetooths = dkmBluetooths.stream().distinct().collect(Collectors.toList());
        // 如果有重复参数,则抛出异常
        if (initSize != dkmBluetooths.size()) {
            throw DiagnosticLogsException.builder()
                    .businessId(DiagnosticLogsEnum.ENTER_VEHICLE_REPLACEMENT_UPLOAD_REPEAT.getBusinessId())
                    .faultId(DiagnosticLogsEnum.ENTER_VEHICLE_REPLACEMENT_UPLOAD_REPEAT.getFaultId())
                    .code(1001)
                    .build();
            //throw new ParameterValidationException();
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
            bluetooth.setDeviceStatus(0);
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
        log.info("request：" + "/api/offlineCheck/getKeyLogDetail " + keyLogDetailVO.toString());
        ArrayList<KeyLogDetailResVO> res = new ArrayList<>();
        if (keyLogDetailVO.getPageIndex() == null || keyLogDetailVO.getPageSize() == null) {
            log.info("response：" + "/api/offlineCheck/getKeyLogDetail " + "必填参数未传递或传入的参数格式不正确！");
            return PageResp.fail(1001, "必填参数未传递或传入的参数格式不正确！");
        }

        Page<DkmKeyLog> page = new Page<>(keyLogDetailVO.getPageIndex(), keyLogDetailVO.getPageSize());
        LambdaQueryWrapper<DkmKeyLog> dkmKeyLogLambdaQueryWrapper = Wrappers.<DkmKeyLog>lambdaQuery()
                .eq(StrUtil.isNotBlank(keyLogDetailVO.getVin()), DkmKeyLog::getVin, keyLogDetailVO.getVin())
                .ge(ObjectUtil.isNotNull(keyLogDetailVO.getStartTime()), DkmKeyLog::getOperateTime, keyLogDetailVO.getStartTime())
                .le(ObjectUtil.isNotNull(keyLogDetailVO.getEndTime()), DkmKeyLog::getOperateTime, keyLogDetailVO.getEndTime())
                .eq(StrUtil.isNotBlank(keyLogDetailVO.getUserId()), DkmKeyLog::getUserId, keyLogDetailVO.getUserId())
                .eq(StrUtil.isNotBlank(keyLogDetailVO.getStatusCode()), DkmKeyLog::getStatusCode, keyLogDetailVO.getStatusCode())
                .orderByAsc(DkmKeyLog::getOperateTime);
        // 查询
        List<DkmKeyLog> dkmKeyLogs = dkmKeyLogMapper.selectList(dkmKeyLogLambdaQueryWrapper);
        page = dkmKeyLogMapper.selectPage(page, dkmKeyLogLambdaQueryWrapper);
        page.setRecords(dkmKeyLogs);
        // 转为结果对象
        for (DkmKeyLog dkmKeyLog : page.getRecords()) {
            KeyLogDetailResVO keyLogDetailResVO = new KeyLogDetailResVO();
            // 赋值对象
            BeanUtils.copyProperties(dkmKeyLog, keyLogDetailResVO);
            // 根据枚举对应
            keyLogDetailResVO.setStatusName(KeyStatusCodeEnum.matchName(dkmKeyLog.getStatusCode()));
//            keyLogDetailResVO.setErrorReasonName(KeyErrorReasonEnum.matchReason(dkmKeyLog.getErrorReason()));
            keyLogDetailResVO.setErrorReasonName(KeyErrorReasonEnumJac.matchReason(dkmKeyLog.getErrorReason()));
            res.add(keyLogDetailResVO);
        }
        log.info("response：" + "/api/offlineCheck/getKeyLogDetail " + "查询成功" + res);
        return PageResp.success("查询成功", page.getTotal(), res);
    }

    public PageResp getKeyData(KeyLogDataVO keyLogDataVO) {
        log.info("request：" + "/api/offlineCheck/getKeyData " + keyLogDataVO.toString());
        // 入参检查
        if (ObjectUtil.isNull(keyLogDataVO.getStartTime()) ||
                ObjectUtil.isNull(keyLogDataVO.getEndTime()) ||
                Objects.isNull(keyLogDataVO.getPageIndex()) ||
                Objects.isNull(keyLogDataVO.getPageSize())) {
            log.info("response：" + "/api/offlineCheck/getKeyData " + "必填参数未传递或传入的参数格式不正确！");
            return PageResp.fail(1001, "必填参数未传递或传入的参数格式不正确！");
        }
        Page<DkmKey> page = new Page<>(keyLogDataVO.getPageIndex(), keyLogDataVO.getPageSize());
        boolean parentId = false;
        if (keyLogDataVO.getStatus() != null) {
            parentId = keyLogDataVO.getStatus() == 1;
        }
        LambdaQueryWrapper<DkmKey> wrapper = new QueryWrapper<DkmKey>().lambda()
                .ge(DkmKey::getValFrom, keyLogDataVO.getStartTime())
                .le(DkmKey::getValTo, keyLogDataVO.getEndTime())
                .eq(StrUtil.isNotBlank(keyLogDataVO.getVin()), DkmKey::getVin, keyLogDataVO.getVin())
                .eq(StrUtil.isNotBlank(keyLogDataVO.getUserId()), DkmKey::getUserId, keyLogDataVO.getUserId())
                .eq(keyLogDataVO.getDkState() != null, DkmKey::getDkState, keyLogDataVO.getDkState())
                .eq(parentId, DkmKey::getDkState, "0");
        page = dkmKeyMapper.selectPage(page, wrapper);
        ArrayList<KeyLogDataResVO> res = new ArrayList<>();
        for (DkmKey dkmKey : page.getRecords()) {
            KeyLogDataResVO keyLogDataResVO = new KeyLogDataResVO();
            BeanUtil.copyProperties(dkmKey, keyLogDataResVO);
            keyLogDataResVO.setKeyId(dkmKey.getId());
            res.add(keyLogDataResVO);
        }
        log.info("response：" + "/api/offlineCheck/getKeyData " + res);
        return PageResp.success("查询成功", page.getTotal(), res);
    }
}