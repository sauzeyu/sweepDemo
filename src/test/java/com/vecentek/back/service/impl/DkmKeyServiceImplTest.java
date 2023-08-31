package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.DkmKeyLifecycle;
import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.entity.DkmUser;
import com.vecentek.back.exception.DiagnosticLogsException;
import com.vecentek.back.mapper.DkmKeyLifecycleMapper;
import com.vecentek.back.mapper.DkmKeyLogHistoryExportMapper;
import com.vecentek.back.mapper.DkmKeyMapper;
import com.vecentek.back.mapper.DkmUserMapper;
import com.vecentek.back.util.KeyLifecycleUtil;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmKeyServiceImplTest {

    @Mock
    private DkmKeyMapper mockDkmKeyMapper;
    @Mock
    private DkmUserMapper mockDkmUserMapper;
    @Mock
    private DkmKeyLifecycleMapper mockDkmKeyLifecycleMapper;
    @Mock
    private DkmKeyLogHistoryExportMapper mockDkmKeyLogHistoryExportMapper;
    @Mock
    private KeyLifecycleUtil mockKeyLifecycleUtil;

    @InjectMocks
    private DkmKeyServiceImpl dkmKeyServiceImplUnderTest;
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmKey.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmUser.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmKeyLifecycle.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmKeyLogHistoryExport.class);
    }
    @Test
    void testSelectById() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmKeyMapper.selectById(...).
        final DkmKey dkmKey = new DkmKey("id", 0, "userId", "vin", 0,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint", 0,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0, "personalAndCalibration", 0L,
                0, "keyResourceVO", 0, "keyClassificationVO", "keyType", "deviceType", "accountIdHash", "endpointId",
                "slotId", "keyOptions", "devicePublicKey", "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox",
                "confidentialMailbox", "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                "keyFriendlyName","bleMacAddress");
        when(mockDkmKeyMapper.selectById("id")).thenReturn(dkmKey);

        // Run the test
        final PageResp result = dkmKeyServiceImplUnderTest.selectById("id");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectForPageByVehicleId() {
        // Setup
        DkmKey dkmKey = new DkmKey();
        dkmKey.setUserId("123");
        List<Object> dkmKeyList = new ArrayList<>();
        dkmKeyList.add(dkmKey);
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(0L, 0L, 0L, false).setRecords(dkmKeyList));
        when(mockDkmUserMapper.selectById(any())).thenReturn(new DkmUser("id", "phone", "username"));

        // Run the test
        final PageResp result = dkmKeyServiceImplUnderTest.selectForPageByVehicleId(0, 0, 0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectForPageByVehicleId_DkmUserMapperReturnsNull() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(0L, 0L, 0L, false));
        when(mockDkmUserMapper.selectById("userId")).thenReturn(null);

        // Run the test
        final PageResp result = dkmKeyServiceImplUnderTest.selectForPageByVehicleId(0, 0, 0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectForPage() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(0L, 0L, 0L, false));

        // Run the test
        final PageResp result = dkmKeyServiceImplUnderTest.selectForPage(0, 0, "userId", "vin", 0, 0, "minute",
                "applyStartTime", "applyEndTime", "valFromStartTime", "valFromEndTime", "valToStartTime",
                "valToEndTime", null, 0, new Integer[]{0,1,2}, new Integer[]{0,1,2});
        // Run the test
        final PageResp result1 = dkmKeyServiceImplUnderTest.selectForPage(0, 0, "userId", "vin", 0, 0, "hour",
                "applyStartTime", "applyEndTime", "valFromStartTime", "valFromEndTime", "valToStartTime",
                "valToEndTime", null, 0, new Integer[]{0,1,2}, new Integer[]{0,1,2});
        // Run the test
        final PageResp result2 = dkmKeyServiceImplUnderTest.selectForPage(0, 0, "userId", "vin", 0, 0, "day",
                "applyStartTime", "applyEndTime", "valFromStartTime", "valFromEndTime", "valToStartTime",
                "valToEndTime", null, 0, new Integer[]{0,1,2}, new Integer[]{0,1,2});
        // Run the test
        final PageResp result3 = dkmKeyServiceImplUnderTest.selectForPage(0, 0, "userId", "vin", -2, 0, "day",
                "applyStartTime", "applyEndTime", "valFromStartTime", "valFromEndTime", "valToStartTime",
                "valToEndTime", null, 0, new Integer[]{0,1,2}, new Integer[]{0,1,2});
        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectForPageByUserId() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(0L, 0L, 0L, false));

        // Run the test
        final PageResp result = dkmKeyServiceImplUnderTest.selectForPageByUserId(0, 0, 0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }
    @Test
    void testUpdateStateById_freezeKeys() throws DiagnosticLogsException {
        // Setup
        final PageResp expectedResult = PageResp.success("更新成功");
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
        DkmKey dkmKey = new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "1", 0,
                "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                "keyFriendlyName","bleMacAddress");
        final List<DkmKey> dkmKeysParent = Arrays.asList(dkmKey
                );
        when(mockDkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery()

                .eq(DkmKey::getUserId, "userId")
                .eq(DkmKey::getVin, "vin")
                .and(wp -> wp.eq(DkmKey::getDkState, 1)
                        .or()
                        .eq(DkmKey::getDkState, 0))
        )).thenReturn(null);
        when(mockDkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery()

                .eq(DkmKey::getUserId, "userId")
                .eq(DkmKey::getVin, "vin")
                .eq(DkmKey::getDkState, 3)

        )).thenReturn(dkmKeys);
        // Run the test
        when( mockDkmKeyMapper.updateById(any())).thenReturn(1);
        final PageResp result2 = dkmKeyServiceImplUnderTest.updateStateById("keyId", 3, "userId", "vin");
        // Verify the results
        assertThat(result2).isEqualTo(expectedResult);
        when(mockDkmKeyMapper.selectList(any())).thenReturn(dkmKeysParent);
        // Run the test
        when( mockDkmKeyMapper.updateById(any())).thenReturn(1);
        final PageResp result = dkmKeyServiceImplUnderTest.updateStateById("keyId", 3, "userId", "vin");
        dkmKey.setParentId("0");
        final List<DkmKey> dkmKeysParent1 = Arrays.asList(dkmKey
        );
        when(mockDkmKeyMapper.selectList(any())).thenReturn(dkmKeysParent);
        final PageResp result3 = dkmKeyServiceImplUnderTest.updateStateById("keyId", 3, "userId", "vin");
    }
    @Test
    void testUpdateStateById_unFreezeKeys() throws DiagnosticLogsException {
        // Setup
        final PageResp expectedResult = PageResp.success("更新成功");
        // Configure DkmKeyMapper.selectList(...).
        final List<DkmKey> dkmKeys = Arrays.asList(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "1", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"));
        final List<DkmKey> dkmKeysParents = Arrays.asList(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "0", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"));
        when(mockDkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery()

                .eq(DkmKey::getUserId, "userId")
                .eq(DkmKey::getVin, "vin")
                .and(wp -> wp.eq(DkmKey::getDkState, 1)
                        .or()
                        .eq(DkmKey::getDkState, 0))
        )).thenReturn(null);
        when(mockDkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery()
                .eq(DkmKey::getDkState, 3)
                .eq(DkmKey::getUserId, "userId")
                .eq(DkmKey::getVin, "vin")


        )).thenReturn(dkmKeys);
        // Verify the results
        //when(mockDkmKeyMapper.selectList(any())).thenReturn(dkmKeys);
        when( mockDkmKeyMapper.updateById(any())).thenReturn(1);
        final PageResp result = dkmKeyServiceImplUnderTest.updateStateById("keyId", 1, "userId", "vin");

        when(mockDkmKeyMapper.selectList(any())).thenReturn(dkmKeysParents);
        when( mockDkmKeyMapper.updateById(any())).thenReturn(0);
        final PageResp result1 = dkmKeyServiceImplUnderTest.updateStateById("keyId", 1, "userId", "vin");
        assertThat(result).isEqualTo(expectedResult);
    }
    @Test
    void testUpdateStateById() throws DiagnosticLogsException {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

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
        when(mockDkmKeyMapper.selectList(any())).thenReturn(dkmKeys);

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
                        "keyFriendlyName","bleMacAddress"), 1, 0, 2)).thenReturn(0);

        // Run the test
        final PageResp result = dkmKeyServiceImplUnderTest.updateStateById("keyId", 1, "userId", "vin");
// Run the test
        final PageResp result1 = dkmKeyServiceImplUnderTest.updateStateById("keyId", 3, "userId", "vin");


        // Verify the results
        assertThat(result).isEqualTo(expectedResult);

        verify(mockKeyLifecycleUtil).insert(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"), 1, 0, 2);
    }
    @Test
    void testUpdateStateById_keyStatusIsNotDelivered() throws DiagnosticLogsException {
        final PageResp expectedResult = PageResp.fail(500, "钥匙状态未传递");
        final PageResp result = dkmKeyServiceImplUnderTest.updateStateById("keyId", null, "userId", "vin");
        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }
    @Test
    void testUpdateStateById_keyStatusIsIncorrect() throws DiagnosticLogsException {
        final PageResp expectedResult = PageResp.fail(500, "钥匙状态未传递");
        final PageResp result = dkmKeyServiceImplUnderTest.updateStateById("keyId", 4, "userId", "vin");
        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }
    @Test
    void testUpdateStateById_fail() throws DiagnosticLogsException {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

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
        when(mockDkmKeyMapper.selectList(any())).thenReturn(dkmKeys);

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
                        "keyFriendlyName","bleMacAddress"), 1, 0, 2)).thenReturn(0);

        // Run the test
        final PageResp result = dkmKeyServiceImplUnderTest.updateStateById("keyId", 1, "userId", "vin");
// Run the test
        final PageResp result1 = dkmKeyServiceImplUnderTest.updateStateById("keyId", 3, "userId", "vin");
        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        verify(mockKeyLifecycleUtil).insert(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"), 1, 0, 2);
    }

    @Test
    void testUpdateStateById_DkmKeyMapperSelectListReturnsNoItems() throws DiagnosticLogsException {
        // Setup
        final PageResp expectedResult = PageResp.success("更新成功");
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmKeyServiceImplUnderTest.updateStateById("keyId", 1, "userId", "vin");


        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testUpdateStateForRevokeById() throws DiagnosticLogsException {
        // Setup
        final PageResp expectedResult = PageResp.success("吊销成功");

        // Configure DkmKeyMapper.selectList(...).
        DkmKey dkmKey = new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "0", 0,
                "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                "keyFriendlyName","bleMacAddress");
        final List<DkmKey> dkmKeys = Arrays.asList(
                dkmKey
                );
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmKeys);

        when(mockDkmKeyMapper.update(any(), any())).thenReturn(1);
        when(mockDkmKeyLifecycleMapper.insert(new DkmKeyLifecycle(0L, "id", "vin", "userId", 0, 0, 0))).thenReturn(0);
        when(mockDkmKeyMapper.updateById(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"))).thenReturn(0);

        // Run the test
        final PageResp result = dkmKeyServiceImplUnderTest.updateStateForRevokeById("userId", "vin");
        dkmKey.setParentId("1");
        final List<DkmKey> dkmKeys1 = Arrays.asList(
                dkmKey
        );
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmKeys1);
        dkmKeyServiceImplUnderTest.updateStateForRevokeById("userId", "vin");
        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        //verify(mockDkmKeyLifecycleMapper).insert(new DkmKeyLifecycle(0L, "id", "vin", "userId", 0, 0, 0));
        //verify(mockDkmKeyMapper).updateById(
        //        new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
        //                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
        //                0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
        //                "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
        //                "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
        //                "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
        //                "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
        //                "keyFriendlyName"));
    }

    @Test
    void testUpdateStateForRevokeById_DkmKeyMapperSelectListReturnsNoItems() throws DiagnosticLogsException {
        // Setup
        final PageResp expectedResult = PageResp.success("吊销成功");
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmKeyServiceImplUnderTest.updateStateForRevokeById("userId", "vin");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectUserByKeyId() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmKeyMapper.selectById(...).
        final DkmKey dkmKey = new DkmKey("id", 0, "userId", "vin", 0,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint", 0,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0, "personalAndCalibration", 0L,
                0, "keyResourceVO", 0, "keyClassificationVO", "keyType", "deviceType", "accountIdHash", "endpointId",
                "slotId", "keyOptions", "devicePublicKey", "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox",
                "confidentialMailbox", "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                "keyFriendlyName","bleMacAddress");
        when(mockDkmKeyMapper.selectById("keyId")).thenReturn(dkmKey);

        when(mockDkmUserMapper.selectById("userId")).thenReturn(new DkmUser("id", "phone", "username"));

        // Run the test
        final PageResp result = dkmKeyServiceImplUnderTest.selectUserByKeyId("keyId");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectUserByKeyId_DkmKeyMapperReturnsNull() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询失败");
        when(mockDkmKeyMapper.selectById("keyId")).thenReturn(null);

        // Run the test
        final PageResp result = dkmKeyServiceImplUnderTest.selectUserByKeyId("keyId");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectForPageByVal() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(0L, 0L, 0L, false));

        // Run the test
        final PageResp result = dkmKeyServiceImplUnderTest.selectForPageByVal(0, 0, "valFrom", "valTo", 0L, 0L);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testDownloadKeyLogExcel() {
        // Setup
        when(mockDkmKeyMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(100001);

        // Configure DkmKeyMapper.selectList(...).
        DkmKey dkmKey = new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
                "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                "keyFriendlyName","bleMacAddress");
        final List<DkmKey> dkmKeys = Arrays.asList(
                dkmKey
                );
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmKeys);

        when(mockDkmKeyLogHistoryExportMapper.update(any(DkmKeyLogHistoryExport.class), any(LambdaUpdateWrapper.class))).thenReturn(0);

        // Run the test
        dkmKeyServiceImplUnderTest.downloadKeyLogExcel("vin", "userId", null, "applyStartTime", "applyEndTime", 0, 0,
                "periodUnit", "valFromStartTime", "valFromEndTime", "valToStartTime", "valToEndTime", new Integer[]{0,1,2},
                0, "creator", "excelName", new Integer[]{0,1,2});
        dkmKey.setParentId("0");
        final List<DkmKey> dkmKeys1 = Arrays.asList(
                dkmKey
        );
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmKeys1);

        dkmKeyServiceImplUnderTest.downloadKeyLogExcel("vin", "userId", null, "applyStartTime", "applyEndTime", 0, 0,
                "periodUnit", "valFromStartTime", "valFromEndTime", "valToStartTime", "valToEndTime", new Integer[]{0,1,2},
                0, "creator", "excelName", new Integer[]{0,1,2});

        dkmKey.setKeyResource(1);
        final List<DkmKey> dkmKeys2 = Arrays.asList(
                dkmKey
        );
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmKeys2);

        dkmKeyServiceImplUnderTest.downloadKeyLogExcel("vin", "userId", null, "applyStartTime", "applyEndTime", 0, 0,
                "periodUnit", "valFromStartTime", "valFromEndTime", "valToStartTime", "valToEndTime", new Integer[]{0,1,2},
                0, "creator", "excelName", new Integer[]{0,1,2});

        dkmKey.setKeyResource(2);
        final List<DkmKey> dkmKeys3 = Arrays.asList(
                dkmKey
        );
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmKeys3);

        dkmKeyServiceImplUnderTest.downloadKeyLogExcel("vin", "userId", null, "applyStartTime", "applyEndTime", 0, 0,
                "periodUnit", "valFromStartTime", "valFromEndTime", "valToStartTime", "valToEndTime", new Integer[]{0,1,2},
                0, "creator", "excelName", new Integer[]{0,1,2});

        dkmKey.setKeyClassification(1);
        final List<DkmKey> dkmKeys4 = Arrays.asList(
                dkmKey
        );
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmKeys4);

        dkmKeyServiceImplUnderTest.downloadKeyLogExcel("vin", "userId", null, "applyStartTime", "applyEndTime", 0, 0,
                "periodUnit", "valFromStartTime", "valFromEndTime", "valToStartTime", "valToEndTime", new Integer[]{0,1,2},
                0, "creator", "excelName", new Integer[]{0,1,2});

        dkmKey.setKeyClassification(2);
        final List<DkmKey> dkmKeys5 = Arrays.asList(
                dkmKey
        );
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmKeys5);

        dkmKeyServiceImplUnderTest.downloadKeyLogExcel("vin", "userId", null, "applyStartTime", "applyEndTime", 0, 0,
                "periodUnit", "valFromStartTime", "valFromEndTime", "valToStartTime", "valToEndTime", new Integer[]{0,1,2},
                0, "creator", "excelName", new Integer[]{0,1,2});
        // Verify the results
        //verify(mockDkmKeyLogHistoryExportMapper).update(any(DkmKeyLogHistoryExport.class), any(LambdaUpdateWrapper.class));
    }

    @Test
    void testDownloadKeyLogExcel_DkmKeyMapperSelectListReturnsNoItems() {
        // Setup
        when(mockDkmKeyMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());
        when(mockDkmKeyLogHistoryExportMapper.update(any(DkmKeyLogHistoryExport.class), any(LambdaUpdateWrapper.class))).thenReturn(0);

        // Run the test
        dkmKeyServiceImplUnderTest.downloadKeyLogExcel("vin", "userId", 0, "applyStartTime", "applyEndTime", 0, 0,
                "periodUnit", "valFromStartTime", "valFromEndTime", "valToStartTime", "valToEndTime", new Integer[]{0},
                0, "creator", "excelName", new Integer[]{0});

        // Verify the results
        //verify(mockDkmKeyLogHistoryExportMapper).update(any(DkmKeyLogHistoryExport.class), any(LambdaUpdateWrapper.class));
    }
}
