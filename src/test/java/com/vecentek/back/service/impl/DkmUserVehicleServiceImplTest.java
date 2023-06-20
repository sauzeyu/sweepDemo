package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.vecentek.back.entity.DkmKey;
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
import com.vecentek.back.util.KeyLifecycleUtil;
import com.vecentek.back.vo.GetBluetoothVinVO;
import com.vecentek.back.vo.LogoutUserVehicleVO;
import com.vecentek.back.vo.RevokeKeyVO;
import com.vecentek.back.vo.UserVehicleVO;
import com.vecentek.common.response.PageResp;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmUserVehicleServiceImplTest {

    @Mock
    private DkmUserVehicleMapper mockDkmUserVehicleMapper;
    @Mock
    private DkmUserMapper mockDkmUserMapper;
    @Mock
    private DkmVehicleMapper mockDkmVehicleMapper;
    @Mock
    private DkmBluetoothsMapper mockDkmBluetoothsMapper;
    @Mock
    private DkmKeyMapper mockDkmKeyMapper;
    @Mock
    private DkmKeyLifecycleMapper mockDkmKeyLifecycleMapper;
    @Mock
    private KeyLifecycleUtil mockKeyLifecycleUtil;

    @InjectMocks
    private DkmUserVehicleServiceImpl dkmUserVehicleServiceImplUnderTest;
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmVehicle.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmKey.class);
    }
    @Test
    void testInsertUserVehicle_fail() throws DiagnosticLogsException {
        // Setup
        final PageResp expectedResult = PageResp.fail(2106, "上传失败，用户ID，VIN等必要参数未传递！");
        final UserVehicleVO userVehicle = new UserVehicleVO("username", null, "license", "vehicleType", null,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final PageResp result = dkmUserVehicleServiceImplUnderTest.insertUserVehicle(userVehicle);
        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }
    @Test
    void testInsertUserVehicle() throws DiagnosticLogsException {
        // Setup
        final UserVehicleVO userVehicle = new UserVehicleVO("username", "id", "license", "vehicleType", "vin",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final PageResp expectedResult = PageResp.success("吊销失败,该用户下没有启动状态的钥匙");
        when(mockDkmUserMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(new DkmUser("id", "id", "username"));

        // Configure DkmVehicleMapper.selectOne(...).
        final DkmVehicle dkmVehicle = new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand",
                "vehicleType", "hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey",
                "dkSecUnitId");
        when(mockDkmVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicle);

        // Configure DkmUserVehicleMapper.selectOne(...).
        final DkmUserVehicle dkmUserVehicle = new DkmUserVehicle(0L, 0, "id",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "id", "deviceInfo", "carInfo", "vin",
                "ks", "vehicleType", "license");
        when(mockDkmUserVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmUserVehicle);

        when(mockDkmUserVehicleMapper.insert(
                new DkmUserVehicle(0L, 0, "id", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "id", "deviceInfo", "carInfo",
                        "vin", "ks", "vehicleType", "license"))).thenReturn(0);
        when(mockDkmUserVehicleMapper.updateById(
                new DkmUserVehicle(0L, 0, "id", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "id", "deviceInfo", "carInfo",
                        "vin", "ks", "vehicleType", "license"))).thenReturn(0);

        // Run the test
        userVehicle.setBindTime(new Date());
        final PageResp result = dkmUserVehicleServiceImplUnderTest.insertUserVehicle(userVehicle);
        userVehicle.setBindTime(null);
        when(mockDkmUserVehicleMapper.insert(any())).thenReturn(1);
        final PageResp result2 = dkmUserVehicleServiceImplUnderTest.insertUserVehicle(userVehicle);
        when(mockDkmUserVehicleMapper.selectOne(any())).thenReturn(null);
        final PageResp result1 = dkmUserVehicleServiceImplUnderTest.insertUserVehicle(userVehicle);
        // Verify the results
        //assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testInsertUserVehicle_DkmUserMapperSelectOneReturnsNull() throws DiagnosticLogsException {
        // Setup
        final UserVehicleVO userVehicle = new UserVehicleVO("username", "id", "license", "vehicleType", "vin",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final PageResp expectedResult = PageResp.success("系统繁忙，请稍后再试！");
        when(mockDkmUserMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(mockDkmUserMapper.insert(new DkmUser("id", "id", "username"))).thenReturn(0);

        // Configure DkmVehicleMapper.selectOne(...).
        final DkmVehicle dkmVehicle = new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand",
                "vehicleType", "hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey",
                "dkSecUnitId");
        when(mockDkmVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicle);

        // Configure DkmUserVehicleMapper.selectOne(...).
        final DkmUserVehicle dkmUserVehicle = new DkmUserVehicle(0L, 0, "id",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "id", "deviceInfo", "carInfo", "vin",
                "ks", "vehicleType", "license");
        when(mockDkmUserVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmUserVehicle);

        when(mockDkmUserVehicleMapper.insert(
                new DkmUserVehicle(0L, 0, "id", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "id", "deviceInfo", "carInfo",
                        "vin", "ks", "vehicleType", "license"))).thenReturn(0);
        when(mockDkmUserVehicleMapper.updateById(
                new DkmUserVehicle(0L, 0, "id", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "id", "deviceInfo", "carInfo",
                        "vin", "ks", "vehicleType", "license"))).thenReturn(0);

        // Run the test
        final PageResp result = dkmUserVehicleServiceImplUnderTest.insertUserVehicle(userVehicle);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        verify(mockDkmUserMapper).insert(new DkmUser("id", "id", "username"));
    }

    @Test
    void testInsertUserVehicle_DkmVehicleMapperReturnsNull() throws DiagnosticLogsException {
        // Setup
        final UserVehicleVO userVehicle = new UserVehicleVO("username", "id", "license", "vehicleType", "vin",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final PageResp expectedResult = PageResp.success("系统不存在该车辆信息！");
        when(mockDkmUserMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(new DkmUser("id", "id", "username"));
        when(mockDkmVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // Run the test
        final PageResp result = dkmUserVehicleServiceImplUnderTest.insertUserVehicle(userVehicle);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testLogoutUserVehicle() throws DiagnosticLogsException {
        // Setup
        final LogoutUserVehicleVO logoutUserVehicle = new LogoutUserVehicleVO();
        logoutUserVehicle.setUserId("userId");
        logoutUserVehicle.setVin("vin");
        logoutUserVehicle.setLogoutTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());

        final PageResp expectedResult = PageResp.success("注销成功");

        // Configure DkmUserVehicleMapper.selectOne(...).
        final DkmUserVehicle dkmUserVehicle = new DkmUserVehicle(0L, 0, "id",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "id", "deviceInfo", "carInfo", "vin",
                "ks", "vehicleType", "license");
        when(mockDkmUserVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmUserVehicle);

        // Configure DkmVehicleMapper.selectOne(...).
        final DkmVehicle dkmVehicle = new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand",
                "vehicleType", "hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey",
                "dkSecUnitId");
        when(mockDkmVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicle);

        when(mockDkmUserVehicleMapper.updateById(
                new DkmUserVehicle(0L, 0, "id", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "id", "deviceInfo", "carInfo",
                        "vin", "ks", "vehicleType", "license"))).thenReturn(0);

        // Configure DkmKeyMapper.selectList(...).
        final List<DkmKey> dkmKeys = Arrays.asList(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"));
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmKeys);

        when(mockDkmKeyMapper.updateById(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"))).thenReturn(0);
        when(mockKeyLifecycleUtil.insert(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"), 1, 3, 5)).thenReturn(0);

        // Run the test
        final PageResp result = dkmUserVehicleServiceImplUnderTest.logoutUserVehicle(logoutUserVehicle);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        //verify(mockDkmUserVehicleMapper).updateById(
        //        new DkmUserVehicle(0L, 0, "id", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
        //                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "id", "deviceInfo", "carInfo",
        //                "vin", "ks", "vehicleType", "license"));
        //verify(mockDkmKeyMapper).updateById(
        //        new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
        //                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
        //                0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
        //                "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
        //                "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
        //                "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
        //                "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
        //                "keyFriendlyName"));
        //verify(mockKeyLifecycleUtil).insert(
        //        new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
        //                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
        //                0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
        //                "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
        //                "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
        //                "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
        //                "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
        //                "keyFriendlyName"), 1, 3, 5);
    }

    @Test
    void testLogoutUserVehicle_DkmUserVehicleMapperSelectOneReturnsNull() throws DiagnosticLogsException {
        // Setup
        final LogoutUserVehicleVO logoutUserVehicle = new LogoutUserVehicleVO();
        logoutUserVehicle.setUserId("userId");
        logoutUserVehicle.setVin("vin");
        logoutUserVehicle.setLogoutTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());

        final PageResp expectedResult = PageResp.success("用户与车辆信息不匹配!");
        when(mockDkmUserVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // Run the test
        final PageResp result = dkmUserVehicleServiceImplUnderTest.logoutUserVehicle(logoutUserVehicle);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testLogoutUserVehicle_DkmVehicleMapperReturnsNull() throws DiagnosticLogsException {
        // Setup
        final LogoutUserVehicleVO logoutUserVehicle = new LogoutUserVehicleVO();
        logoutUserVehicle.setUserId("userId");
        logoutUserVehicle.setVin("vin");
        logoutUserVehicle.setLogoutTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());

        final PageResp expectedResult = PageResp.success("系统不存在该车辆信息!");

        // Configure DkmUserVehicleMapper.selectOne(...).
        final DkmUserVehicle dkmUserVehicle = new DkmUserVehicle(0L, 0, "id",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "id", "deviceInfo", "carInfo", "vin",
                "ks", "vehicleType", "license");
        when(mockDkmUserVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmUserVehicle);

        when(mockDkmVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // Run the test
        final PageResp result = dkmUserVehicleServiceImplUnderTest.logoutUserVehicle(logoutUserVehicle);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testLogoutUserVehicle_DkmKeyMapperSelectListReturnsNoItems() throws DiagnosticLogsException {
        // Setup
        final LogoutUserVehicleVO logoutUserVehicle = new LogoutUserVehicleVO();
        logoutUserVehicle.setUserId("userId");
        logoutUserVehicle.setVin("vin");
        logoutUserVehicle.setLogoutTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());

        final PageResp expectedResult = PageResp.success("注销成功");

        // Configure DkmUserVehicleMapper.selectOne(...).
        final DkmUserVehicle dkmUserVehicle = new DkmUserVehicle(0L, 0, "id",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "id", "deviceInfo", "carInfo", "vin",
                "ks", "vehicleType", "license");
        when(mockDkmUserVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmUserVehicle);

        // Configure DkmVehicleMapper.selectOne(...).
        final DkmVehicle dkmVehicle = new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand",
                "vehicleType", "hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey",
                "dkSecUnitId");
        when(mockDkmVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicle);

        when(mockDkmUserVehicleMapper.updateById(
                new DkmUserVehicle(0L, 0, "id", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "id", "deviceInfo", "carInfo",
                        "vin", "ks", "vehicleType", "license"))).thenReturn(0);
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmUserVehicleServiceImplUnderTest.logoutUserVehicle(logoutUserVehicle);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        //verify(mockDkmUserVehicleMapper).updateById(
        //        new DkmUserVehicle(0L, 0, "id", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
        //                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "id", "deviceInfo", "carInfo",
        //                "vin", "ks", "vehicleType", "license"));
    }

    @Test
    void testGetBluetoothVin() {
        // Setup
        final GetBluetoothVinVO getBluetoothVinVO = new GetBluetoothVinVO();
        getBluetoothVinVO.setVin("vin");

        final PageResp expectedResult = PageResp.success("具有蓝牙钥匙功能!");

        // Configure DkmVehicleMapper.selectOne(...).
        final DkmVehicle dkmVehicle = new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand",
                "vehicleType", "hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey",
                "dkSecUnitId");
        when(mockDkmVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicle);

        // Run the test
        final PageResp result = dkmUserVehicleServiceImplUnderTest.getBluetoothVin(getBluetoothVinVO);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testGetBluetoothVin_DkmVehicleMapperReturnsNull() {
        // Setup
        final GetBluetoothVinVO getBluetoothVinVO = new GetBluetoothVinVO();
        getBluetoothVinVO.setVin("vin");

        final PageResp expectedResult = PageResp.success("不具有蓝牙钥匙功能!");
        when(mockDkmVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // Run the test
        final PageResp result = dkmUserVehicleServiceImplUnderTest.getBluetoothVin(getBluetoothVinVO);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }
    @Test
    void testRevokeKey_fail() throws DiagnosticLogsException {
        // Setup
        final RevokeKeyVO revokeKeyVO = new RevokeKeyVO();

        final PageResp expectedResult = PageResp.fail(1001, "必填参数未传递或传入的参数格式不正确！");
        // Run the test
        final PageResp result = dkmUserVehicleServiceImplUnderTest.revokeKey(revokeKeyVO);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }
    @Test
    void testRevokeKey() throws DiagnosticLogsException {
        // Setup
        final RevokeKeyVO revokeKeyVO = new RevokeKeyVO();
        revokeKeyVO.setUserId("userId");

        final PageResp expectedResult = PageResp.success("吊销钥匙成功!");

        // Configure DkmKeyMapper.selectList(...).
        final List<DkmKey> dkmKeys = Arrays.asList(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "0", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"));
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmKeys);

        when(mockDkmKeyMapper.updateById(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"))).thenReturn(0);
        when(mockKeyLifecycleUtil.insert(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"), 0, 3, 5)).thenReturn(0);

        // Run the test
        final PageResp result = dkmUserVehicleServiceImplUnderTest.revokeKey(revokeKeyVO);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        //verify(mockDkmKeyMapper).updateById(
        //        new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
        //                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
        //                0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
        //                "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
        //                "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
        //                "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
        //                "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
        //                "keyFriendlyName"));
        //verify(mockKeyLifecycleUtil).insert(
        //        new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
        //                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
        //                0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
        //                "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
        //                "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
        //                "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
        //                "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
        //                "keyFriendlyName"), 0, 3, 5);
    }

    @Test
    void testRevokeKey_DkmKeyMapperSelectListReturnsNoItems() throws DiagnosticLogsException {
        // Setup
        final RevokeKeyVO revokeKeyVO = new RevokeKeyVO();
        revokeKeyVO.setUserId("userId");

        final PageResp expectedResult = PageResp.success("吊销失败,该用户下没有启动状态的钥匙");
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmUserVehicleServiceImplUnderTest.revokeKey(revokeKeyVO);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }
}
