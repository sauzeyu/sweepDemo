package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmAftermarketReplacement;
import com.vecentek.back.entity.DkmBluetooths;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.DkmKeyLifecycle;
import com.vecentek.back.entity.DkmKeyLog;
import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.mapper.DkmAftermarketReplacementMapper;
import com.vecentek.back.mapper.DkmBluetoothsMapper;
import com.vecentek.back.mapper.DkmKeyLifecycleMapper;
import com.vecentek.back.mapper.DkmKeyLogMapper;
import com.vecentek.back.mapper.DkmKeyMapper;
import com.vecentek.back.mapper.DkmOfflineCheckMapper;
import com.vecentek.back.mapper.DkmUserMapper;
import com.vecentek.back.mapper.DkmVehicleMapper;
import com.vecentek.back.vo.KeyLogDataVO;
import com.vecentek.back.vo.KeyLogDetailVO;
import com.vecentek.back.vo.VehicleBluetoothVO;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmOfflineCheckServiceImplTest {

    @Mock
    private DkmOfflineCheckMapper mockDkmOfflineCheckMapper;
    @Mock
    private DkmBluetoothsMapper mockDkmBluetoothsMapper;
    @Mock
    private DkmVehicleMapper mockDkmVehicleMapper;
    @Mock
    private DkmAftermarketReplacementMapper mockDkmAftermarketReplacementMapper;
    @Mock
    private DkmKeyMapper mockDkmKeyMapper;
    @Mock
    private DkmKeyLifecycleMapper mockDkmKeyLifecycleMapper;
    @Mock
    private DkmUserMapper mockDkmUserMapper;
    @Mock
    private DkmKeyLogMapper mockDkmKeyLogMapper;

    @InjectMocks
    private DkmOfflineCheckServiceImpl dkmOfflineCheckServiceImplUnderTest;

    private VehicleBluetoothVO vehicleBluetoothVO;
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmVehicle.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmBluetooths.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmAftermarketReplacement.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmKey.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmKeyLifecycle.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmKeyLog.class);
        VehicleBluetoothVO vehicleBluetooth = new VehicleBluetoothVO();
        vehicleBluetooth.setVin("LGQIZ44O82E7T2107");
        vehicleBluetooth.setVehicleModel("vehicleModel");
        vehicleBluetooth.setVehicleBrand("vehicleBrand");
        vehicleBluetooth.setVehicleType("vehicleType");
        vehicleBluetooth.setHwDeviceSn("30323130323032323132333131323334FFFF2107");
        vehicleBluetooth.setSearchNumber("50343032303231323334FFFFFFFFFFFFFF2107");
        vehicleBluetooth.setBleMacAddress("BD8A76542107");
        vehicleBluetooth.setPubKey("04135B43E5572C2DF400476D20FCFAC1AAC6F1E3425BBDEAB96E33EFE750CD54B4217F0E8D7462E07F1024A418E0CCCFB56B3499BE6EAA121374FCB52ACC652107");
        vehicleBluetooth.setDkSecUnitId("dkSecUnitId");
        vehicleBluetooth.setHwDeviceProviderNo("30323130323032323132333131323334FFFF2107");
        vehicleBluetoothVO = vehicleBluetooth;
    }
    @Test
    void testInsertOrUpdateVehicleBatch_uploadsMustNotBeEmpty() throws Exception {
        // Setup

        List<VehicleBluetoothVO> emptyDkmVehicles = new ArrayList<>();

        final List<VehicleBluetoothVO> dkmVehicles = Arrays.asList(vehicleBluetoothVO);


        final PageResp expectedResult = PageResp.success("上传成功");
        PageResp resultDataEmpty = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(emptyDkmVehicles);


        final PageResp result = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(dkmVehicles);
    }
    @Test
    void testInsertOrUpdateVehicleBatch_exceedsTheMaximum() throws Exception {

        List<VehicleBluetoothVO> initDkmVehicles =new ArrayList<>();
        for (int i = 0; i < 51; i++) {
            vehicleBluetoothVO.setVehicleModel("vehicleModel"+i);
            initDkmVehicles.add(vehicleBluetoothVO);
        }
        PageResp resultfiftyBars = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(initDkmVehicles);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_repeatDkmVehicles() throws Exception {

        List<VehicleBluetoothVO> repeatDkmVehicles = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            repeatDkmVehicles.add(vehicleBluetoothVO);
        }

        PageResp repeatDkmVehicle = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(repeatDkmVehicles);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_notDelivered() throws Exception {

        List<VehicleBluetoothVO> notDeliveredDkmVehicles = new ArrayList<>();


        vehicleBluetoothVO.setPubKey(null);
        notDeliveredDkmVehicles.add(vehicleBluetoothVO);


        PageResp notDeliveredDkmVehicle = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(notDeliveredDkmVehicles);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_searchNumberError() throws Exception {

        List<VehicleBluetoothVO> searchNumberErrorDkmVehicles = new ArrayList<>();

        vehicleBluetoothVO.setSearchNumber("123");
        searchNumberErrorDkmVehicles.add(vehicleBluetoothVO);


        PageResp searchNumberErrorDkmVehicle = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(searchNumberErrorDkmVehicles);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_searchNumberNotSole() throws Exception {

        List<VehicleBluetoothVO> searchNumberNotSoleDkmVehicles = new ArrayList<>();


        searchNumberNotSoleDkmVehicles.add(vehicleBluetoothVO);

        when(mockDkmBluetoothsMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1);
        PageResp searchNumberNotSoleDkmVehicle = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(searchNumberNotSoleDkmVehicles);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_vinLength() throws Exception {

        List<VehicleBluetoothVO> vinLengthDkmVehicles = new ArrayList<>();

        vehicleBluetoothVO.setVin("123");
        vinLengthDkmVehicles.add(vehicleBluetoothVO);


        PageResp vinLengthDkmVehicle = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(vinLengthDkmVehicles);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_bleMacAddressLength() throws Exception {

        List<VehicleBluetoothVO> bleMacAddressLengthDkmVehicles = new ArrayList<>();

        vehicleBluetoothVO.setBleMacAddress("123");
        bleMacAddressLengthDkmVehicles.add(vehicleBluetoothVO);


        PageResp bleMacAddressLengthDkmVehicle = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(bleMacAddressLengthDkmVehicles);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_pubKeyLength() throws Exception {

        List<VehicleBluetoothVO> pubKeyLengthDkmVehicles = new ArrayList<>();

        vehicleBluetoothVO.setPubKey("123");
        pubKeyLengthDkmVehicles.add(vehicleBluetoothVO);


        PageResp pubKeyLengthDkmVehicle = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(pubKeyLengthDkmVehicles);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_hwDeviceSnNotFormat() throws Exception {

        List<VehicleBluetoothVO> hwDeviceSnNotFormatDkmVehicles = new ArrayList<>();

        vehicleBluetoothVO.setHwDeviceSn("30323130323032323132333131323334FFFF210@");
        hwDeviceSnNotFormatDkmVehicles.add(vehicleBluetoothVO);


        PageResp hwDeviceSnNotFormatDkmVehicle = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(hwDeviceSnNotFormatDkmVehicles);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_bleMacAddressNotFormat() throws Exception {

        List<VehicleBluetoothVO> bleMacAddressNotFormatNotFormatDkmVehicles = new ArrayList<>();

        vehicleBluetoothVO.setBleMacAddress("BD8A7654210@");
        bleMacAddressNotFormatNotFormatDkmVehicles.add(vehicleBluetoothVO);


        PageResp bleMacAddressNotFormatNotFormatDkmVehicle = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(bleMacAddressNotFormatNotFormatDkmVehicles);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_pubKeyNotFormat() throws Exception {

        List<VehicleBluetoothVO> pubKeyNotFormatDkmVehicles = new ArrayList<>();

        vehicleBluetoothVO.setPubKey("04135B43E5572C2DF400476D20FCFAC1AAC6F1E3425BBDEAB96E33EFE750CD54B4217F0E8D7462E07F1024A418E0CCCFB56B3499BE6EAA121374FCB52ACC65210@");
        pubKeyNotFormatDkmVehicles.add(vehicleBluetoothVO);


        PageResp pubKeyNotFormatFormatDkmVehicle = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(pubKeyNotFormatDkmVehicles);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_searchNumberNotFormat() throws Exception {

        List<VehicleBluetoothVO>searchNumberNotFormatDkmVehicles = new ArrayList<>();

        vehicleBluetoothVO.setSearchNumber("50343032303231323334FFFFFFFFFFFFFF210@");
        searchNumberNotFormatDkmVehicles.add(vehicleBluetoothVO);


        PageResp searchNumberNotFormatDkmVehicle = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(searchNumberNotFormatDkmVehicles);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_repeatInPairs() throws Exception {

        List<VehicleBluetoothVO>repeatInPairsDkmVehicles = new ArrayList<>();


        repeatInPairsDkmVehicles.add(vehicleBluetoothVO);
        final List<DkmVehicle> dkmVehicles1 = Arrays.asList(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"));
        when(mockDkmVehicleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicles1);

        PageResp repeatInPairsDkmVehicle = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(repeatInPairsDkmVehicles);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_repeatHwDeviceSn() throws Exception {

        List<VehicleBluetoothVO>repeatHwDeviceSnDkmVehicles = new ArrayList<>();


        repeatHwDeviceSnDkmVehicles.add(vehicleBluetoothVO);
        final List<DkmBluetooths> dkmBluetooths = Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        when(mockDkmBluetoothsMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmBluetooths);

        PageResp repeatHwDeviceSnDkmVehicle = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(repeatHwDeviceSnDkmVehicles);
    }

    @Test
    void testInsertOrUpdateVehicleBatch_repeatSearchNumber() throws Exception {

        List<VehicleBluetoothVO>repeatSearchNumberDkmVehicles = new ArrayList<>();


        repeatSearchNumberDkmVehicles.add(vehicleBluetoothVO);
        final List<DkmBluetooths> dkmBluetooths = Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        when(mockDkmBluetoothsMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmBluetooths);

        PageResp repeatSearchNumberDkmVehicle = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(repeatSearchNumberDkmVehicles);
    }
    @Test
    void testInsertOrUpdateVehicleBatch() throws Exception {
        // Setup
        final VehicleBluetoothVO vehicleBluetoothVO = new VehicleBluetoothVO();
        vehicleBluetoothVO.setVin("LGQIZ44O82E7T2107");
        vehicleBluetoothVO.setVehicleModel("vehicleModel");
        vehicleBluetoothVO.setVehicleBrand("vehicleBrand");
        vehicleBluetoothVO.setVehicleType("vehicleType");
        vehicleBluetoothVO.setHwDeviceSn("30323130323032323132333131323334FFFF2107");
        vehicleBluetoothVO.setSearchNumber("50343032303231323334FFFFFFFFFFFFFF2107");
        vehicleBluetoothVO.setBleMacAddress("BD8A76542107");
        vehicleBluetoothVO.setPubKey("04135B43E5572C2DF400476D20FCFAC1AAC6F1E3425BBDEAB96E33EFE750CD54B4217F0E8D7462E07F1024A418E0CCCFB56B3499BE6EAA121374FCB52ACC652107");
        vehicleBluetoothVO.setDkSecUnitId("dkSecUnitId");
        vehicleBluetoothVO.setHwDeviceProviderNo("30323130323032323132333131323334FFFF2107");
        final List<VehicleBluetoothVO> dkmVehicles = Arrays.asList(vehicleBluetoothVO);
        final PageResp expectedResult = PageResp.success("上传成功");
        when(mockDkmBluetoothsMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0);

        // Configure DkmVehicleMapper.selectList(...).
        final List<DkmVehicle> dkmVehicles1 = Arrays.asList(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"));
        when(mockDkmVehicleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicles1);

        // Configure DkmBluetoothsMapper.selectList(...).
        final List<DkmBluetooths> dkmBluetooths = Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        when(mockDkmBluetoothsMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmBluetooths);

        when(mockDkmOfflineCheckMapper.selectVehicleByVin(Arrays.asList(new VehicleBluetoothVO())))
                .thenReturn(Arrays.asList("value"));
        when(mockDkmVehicleMapper.insert(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"))).thenReturn(0);
        when(mockDkmBluetoothsMapper.insert(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"))).thenReturn(0);

        // Configure DkmVehicleMapper.selectOne(...).
        final DkmVehicle dkmVehicle = new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand",
                "vehicleType", "hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey",
                "dkSecUnitId");
        when(mockDkmVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicle);

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
        when(mockDkmKeyLifecycleMapper.insert(new DkmKeyLifecycle(0L, "id", "vin", "userId", 0, 0, 0))).thenReturn(0);
        when(mockDkmBluetoothsMapper.update(
                eq(new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey")),
                any(LambdaUpdateWrapper.class))).thenReturn(0);
        when(mockDkmVehicleMapper.update(
                eq(new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId")),
                any(LambdaUpdateWrapper.class))).thenReturn(0);
        when(mockDkmAftermarketReplacementMapper.insert(
                new DkmAftermarketReplacement(0L, "vin", "hwDeviceSn", "newBluetoothSn",
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()))).thenReturn(0);

        when(mockDkmVehicleMapper.selectList(any())).thenReturn(new ArrayList<>());
        when(mockDkmBluetoothsMapper.selectList(any())).thenReturn(new ArrayList<>());

        // Run the test
        final PageResp result = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(dkmVehicles);
        when(mockDkmOfflineCheckMapper.selectVehicleByVin(any()))
                .thenReturn(Arrays.asList("LGQIZ44O82E7T2107"));
        final PageResp result1 = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(dkmVehicles);
        vehicleBluetoothVO.setSearchNumber(null);
        final List<VehicleBluetoothVO> dkmVehicles3 = Arrays.asList(vehicleBluetoothVO);
        final PageResp result2 = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(dkmVehicles3);
        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        //verify(mockDkmVehicleMapper).insert(
        //        new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
        //                "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"));
        //verify(mockDkmBluetoothsMapper).insert(
        //        new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
        //                "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        //verify(mockDkmKeyMapper).updateById(
        //        new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
        //                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
        //                0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
        //                "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
        //                "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
        //                "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
        //                "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
        //                "keyFriendlyName"));
        //verify(mockDkmKeyLifecycleMapper).insert(new DkmKeyLifecycle(0L, "id", "vin", "userId", 0, 0, 0));
        //verify(mockDkmBluetoothsMapper).update(
        //        eq(new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
        //                "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey")), any(LambdaUpdateWrapper.class));
        //verify(mockDkmVehicleMapper).update(
        //        eq(new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
        //                "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId")),
        //        any(LambdaUpdateWrapper.class));
        //verify(mockDkmAftermarketReplacementMapper).insert(
        //        new DkmAftermarketReplacement(0L, "vin", "hwDeviceSn", "newBluetoothSn",
        //                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
    }

    @Test
    void testInsertOrUpdateVehicleBatch_DkmVehicleMapperSelectListReturnsNoItems() throws Exception {
        // Setup
        final VehicleBluetoothVO vehicleBluetoothVO = new VehicleBluetoothVO();
        vehicleBluetoothVO.setVin("vin");
        vehicleBluetoothVO.setVehicleModel("vehicleModel");
        vehicleBluetoothVO.setVehicleBrand("vehicleBrand");
        vehicleBluetoothVO.setVehicleType("vehicleType");
        vehicleBluetoothVO.setHwDeviceSn("newBluetoothSn");
        vehicleBluetoothVO.setSearchNumber("searchNumber");
        vehicleBluetoothVO.setBleMacAddress("bleMacAddress");
        vehicleBluetoothVO.setPubKey("pubKey");
        vehicleBluetoothVO.setDkSecUnitId("dkSecUnitId");
        vehicleBluetoothVO.setHwDeviceProviderNo("hwDeviceProviderNo");
        final List<VehicleBluetoothVO> dkmVehicles = Arrays.asList(vehicleBluetoothVO);
        final PageResp expectedResult = PageResp.success("msg");
        when(mockDkmBluetoothsMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmVehicleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Configure DkmBluetoothsMapper.selectList(...).
        final List<DkmBluetooths> dkmBluetooths = Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        when(mockDkmBluetoothsMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmBluetooths);

        when(mockDkmOfflineCheckMapper.selectVehicleByVin(Arrays.asList(new VehicleBluetoothVO())))
                .thenReturn(Arrays.asList("value"));
        when(mockDkmVehicleMapper.insert(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"))).thenReturn(0);
        when(mockDkmBluetoothsMapper.insert(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"))).thenReturn(0);

        // Configure DkmVehicleMapper.selectOne(...).
        final DkmVehicle dkmVehicle = new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand",
                "vehicleType", "hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey",
                "dkSecUnitId");
        when(mockDkmVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicle);

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
        when(mockDkmKeyLifecycleMapper.insert(new DkmKeyLifecycle(0L, "id", "vin", "userId", 0, 0, 0))).thenReturn(0);
        when(mockDkmBluetoothsMapper.update(
                eq(new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey")),
                any(LambdaUpdateWrapper.class))).thenReturn(0);
        when(mockDkmVehicleMapper.update(
                eq(new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId")),
                any(LambdaUpdateWrapper.class))).thenReturn(0);
        when(mockDkmAftermarketReplacementMapper.insert(
                new DkmAftermarketReplacement(0L, "vin", "hwDeviceSn", "newBluetoothSn",
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()))).thenReturn(0);

        // Run the test
        final PageResp result = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(dkmVehicles);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        verify(mockDkmVehicleMapper).insert(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"));
        verify(mockDkmBluetoothsMapper).insert(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        verify(mockDkmKeyMapper).updateById(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"));
        verify(mockDkmKeyLifecycleMapper).insert(new DkmKeyLifecycle(0L, "id", "vin", "userId", 0, 0, 0));
        verify(mockDkmBluetoothsMapper).update(
                eq(new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey")), any(LambdaUpdateWrapper.class));
        verify(mockDkmVehicleMapper).update(
                eq(new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId")),
                any(LambdaUpdateWrapper.class));
        verify(mockDkmAftermarketReplacementMapper).insert(
                new DkmAftermarketReplacement(0L, "vin", "hwDeviceSn", "newBluetoothSn",
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
    }

    @Test
    void testInsertOrUpdateVehicleBatch_DkmBluetoothsMapperSelectListReturnsNoItems() throws Exception {
        // Setup
        final VehicleBluetoothVO vehicleBluetoothVO = new VehicleBluetoothVO();
        vehicleBluetoothVO.setVin("vin");
        vehicleBluetoothVO.setVehicleModel("vehicleModel");
        vehicleBluetoothVO.setVehicleBrand("vehicleBrand");
        vehicleBluetoothVO.setVehicleType("vehicleType");
        vehicleBluetoothVO.setHwDeviceSn("newBluetoothSn");
        vehicleBluetoothVO.setSearchNumber("searchNumber");
        vehicleBluetoothVO.setBleMacAddress("bleMacAddress");
        vehicleBluetoothVO.setPubKey("pubKey");
        vehicleBluetoothVO.setDkSecUnitId("dkSecUnitId");
        vehicleBluetoothVO.setHwDeviceProviderNo("hwDeviceProviderNo");
        final List<VehicleBluetoothVO> dkmVehicles = Arrays.asList(vehicleBluetoothVO);
        final PageResp expectedResult = PageResp.success("msg");
        when(mockDkmBluetoothsMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0);

        // Configure DkmVehicleMapper.selectList(...).
        final List<DkmVehicle> dkmVehicles1 = Arrays.asList(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"));
        when(mockDkmVehicleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicles1);

        when(mockDkmBluetoothsMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());
        when(mockDkmOfflineCheckMapper.selectVehicleByVin(Arrays.asList(new VehicleBluetoothVO())))
                .thenReturn(Arrays.asList("value"));
        when(mockDkmVehicleMapper.insert(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"))).thenReturn(0);
        when(mockDkmBluetoothsMapper.insert(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"))).thenReturn(0);

        // Configure DkmVehicleMapper.selectOne(...).
        final DkmVehicle dkmVehicle = new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand",
                "vehicleType", "hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey",
                "dkSecUnitId");
        when(mockDkmVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicle);

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
        when(mockDkmKeyLifecycleMapper.insert(new DkmKeyLifecycle(0L, "id", "vin", "userId", 0, 0, 0))).thenReturn(0);
        when(mockDkmBluetoothsMapper.update(
                eq(new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey")),
                any(LambdaUpdateWrapper.class))).thenReturn(0);
        when(mockDkmVehicleMapper.update(
                eq(new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId")),
                any(LambdaUpdateWrapper.class))).thenReturn(0);
        when(mockDkmAftermarketReplacementMapper.insert(
                new DkmAftermarketReplacement(0L, "vin", "hwDeviceSn", "newBluetoothSn",
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()))).thenReturn(0);

        // Run the test
        final PageResp result = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(dkmVehicles);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        verify(mockDkmVehicleMapper).insert(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"));
        verify(mockDkmBluetoothsMapper).insert(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        verify(mockDkmKeyMapper).updateById(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"));
        verify(mockDkmKeyLifecycleMapper).insert(new DkmKeyLifecycle(0L, "id", "vin", "userId", 0, 0, 0));
        verify(mockDkmBluetoothsMapper).update(
                eq(new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey")), any(LambdaUpdateWrapper.class));
        verify(mockDkmVehicleMapper).update(
                eq(new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId")),
                any(LambdaUpdateWrapper.class));
        verify(mockDkmAftermarketReplacementMapper).insert(
                new DkmAftermarketReplacement(0L, "vin", "hwDeviceSn", "newBluetoothSn",
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
    }

    @Test
    void testInsertOrUpdateVehicleBatch_DkmOfflineCheckMapperReturnsNoItems() throws Exception {
        // Setup
        final VehicleBluetoothVO vehicleBluetoothVO = new VehicleBluetoothVO();
        vehicleBluetoothVO.setVin("vin");
        vehicleBluetoothVO.setVehicleModel("vehicleModel");
        vehicleBluetoothVO.setVehicleBrand("vehicleBrand");
        vehicleBluetoothVO.setVehicleType("vehicleType");
        vehicleBluetoothVO.setHwDeviceSn("newBluetoothSn");
        vehicleBluetoothVO.setSearchNumber("searchNumber");
        vehicleBluetoothVO.setBleMacAddress("bleMacAddress");
        vehicleBluetoothVO.setPubKey("pubKey");
        vehicleBluetoothVO.setDkSecUnitId("dkSecUnitId");
        vehicleBluetoothVO.setHwDeviceProviderNo("hwDeviceProviderNo");
        final List<VehicleBluetoothVO> dkmVehicles = Arrays.asList(vehicleBluetoothVO);
        final PageResp expectedResult = PageResp.success("msg");
        when(mockDkmBluetoothsMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0);

        // Configure DkmVehicleMapper.selectList(...).
        final List<DkmVehicle> dkmVehicles1 = Arrays.asList(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"));
        when(mockDkmVehicleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicles1);

        // Configure DkmBluetoothsMapper.selectList(...).
        final List<DkmBluetooths> dkmBluetooths = Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        when(mockDkmBluetoothsMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmBluetooths);

        when(mockDkmOfflineCheckMapper.selectVehicleByVin(Arrays.asList(new VehicleBluetoothVO())))
                .thenReturn(Collections.emptyList());
        when(mockDkmVehicleMapper.insert(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"))).thenReturn(0);
        when(mockDkmBluetoothsMapper.insert(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"))).thenReturn(0);

        // Configure DkmVehicleMapper.selectOne(...).
        final DkmVehicle dkmVehicle = new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand",
                "vehicleType", "hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey",
                "dkSecUnitId");
        when(mockDkmVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicle);

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
        when(mockDkmKeyLifecycleMapper.insert(new DkmKeyLifecycle(0L, "id", "vin", "userId", 0, 0, 0))).thenReturn(0);
        when(mockDkmBluetoothsMapper.update(
                eq(new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey")),
                any(LambdaUpdateWrapper.class))).thenReturn(0);
        when(mockDkmVehicleMapper.update(
                eq(new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId")),
                any(LambdaUpdateWrapper.class))).thenReturn(0);
        when(mockDkmAftermarketReplacementMapper.insert(
                new DkmAftermarketReplacement(0L, "vin", "hwDeviceSn", "newBluetoothSn",
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()))).thenReturn(0);

        // Run the test
        final PageResp result = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(dkmVehicles);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        verify(mockDkmVehicleMapper).insert(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"));
        verify(mockDkmBluetoothsMapper).insert(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        verify(mockDkmKeyMapper).updateById(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"));
        verify(mockDkmKeyLifecycleMapper).insert(new DkmKeyLifecycle(0L, "id", "vin", "userId", 0, 0, 0));
        verify(mockDkmBluetoothsMapper).update(
                eq(new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey")), any(LambdaUpdateWrapper.class));
        verify(mockDkmVehicleMapper).update(
                eq(new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId")),
                any(LambdaUpdateWrapper.class));
        verify(mockDkmAftermarketReplacementMapper).insert(
                new DkmAftermarketReplacement(0L, "vin", "hwDeviceSn", "newBluetoothSn",
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
    }

    @Test
    void testInsertOrUpdateVehicleBatch_DkmKeyMapperSelectListReturnsNoItems() throws Exception {
        // Setup
        final VehicleBluetoothVO vehicleBluetoothVO = new VehicleBluetoothVO();
        vehicleBluetoothVO.setVin("vin");
        vehicleBluetoothVO.setVehicleModel("vehicleModel");
        vehicleBluetoothVO.setVehicleBrand("vehicleBrand");
        vehicleBluetoothVO.setVehicleType("vehicleType");
        vehicleBluetoothVO.setHwDeviceSn("newBluetoothSn");
        vehicleBluetoothVO.setSearchNumber("searchNumber");
        vehicleBluetoothVO.setBleMacAddress("bleMacAddress");
        vehicleBluetoothVO.setPubKey("pubKey");
        vehicleBluetoothVO.setDkSecUnitId("dkSecUnitId");
        vehicleBluetoothVO.setHwDeviceProviderNo("hwDeviceProviderNo");
        final List<VehicleBluetoothVO> dkmVehicles = Arrays.asList(vehicleBluetoothVO);
        final PageResp expectedResult = PageResp.success("msg");
        when(mockDkmBluetoothsMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0);

        // Configure DkmVehicleMapper.selectList(...).
        final List<DkmVehicle> dkmVehicles1 = Arrays.asList(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"));
        when(mockDkmVehicleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicles1);

        // Configure DkmBluetoothsMapper.selectList(...).
        final List<DkmBluetooths> dkmBluetooths = Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        when(mockDkmBluetoothsMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmBluetooths);

        when(mockDkmOfflineCheckMapper.selectVehicleByVin(Arrays.asList(new VehicleBluetoothVO())))
                .thenReturn(Arrays.asList("value"));
        when(mockDkmVehicleMapper.insert(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"))).thenReturn(0);
        when(mockDkmBluetoothsMapper.insert(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"))).thenReturn(0);

        // Configure DkmVehicleMapper.selectOne(...).
        final DkmVehicle dkmVehicle = new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand",
                "vehicleType", "hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey",
                "dkSecUnitId");
        when(mockDkmVehicleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmVehicle);

        when(mockDkmKeyMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());
        when(mockDkmKeyMapper.updateById(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"))).thenReturn(0);
        when(mockDkmKeyLifecycleMapper.insert(new DkmKeyLifecycle(0L, "id", "vin", "userId", 0, 0, 0))).thenReturn(0);
        when(mockDkmBluetoothsMapper.update(
                eq(new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey")),
                any(LambdaUpdateWrapper.class))).thenReturn(0);
        when(mockDkmVehicleMapper.update(
                eq(new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId")),
                any(LambdaUpdateWrapper.class))).thenReturn(0);
        when(mockDkmAftermarketReplacementMapper.insert(
                new DkmAftermarketReplacement(0L, "vin", "hwDeviceSn", "newBluetoothSn",
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()))).thenReturn(0);

        // Run the test
        final PageResp result = dkmOfflineCheckServiceImplUnderTest.insertOrUpdateVehicleBatch(dkmVehicles);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        verify(mockDkmVehicleMapper).insert(
                new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId"));
        verify(mockDkmBluetoothsMapper).insert(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        verify(mockDkmKeyMapper).updateById(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "parentId", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"));
        verify(mockDkmKeyLifecycleMapper).insert(new DkmKeyLifecycle(0L, "id", "vin", "userId", 0, 0, 0));
        verify(mockDkmBluetoothsMapper).update(
                eq(new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey")), any(LambdaUpdateWrapper.class));
        verify(mockDkmVehicleMapper).update(
                eq(new DkmVehicle(0, "factoryNo", "vin", "vehicleModel", "vehicleBrand", "vehicleType", "hwDeviceSn",
                        "searchNumber", "hwDeviceProviderNo", "bleMacAddress", "pubKey", "dkSecUnitId")),
                any(LambdaUpdateWrapper.class));
        verify(mockDkmAftermarketReplacementMapper).insert(
                new DkmAftermarketReplacement(0L, "vin", "hwDeviceSn", "newBluetoothSn",
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
    }

    @Test
    void testInsertBluetoothBatch() throws Exception {
        // Setup
        final List<DkmBluetooths> dkmBluetooths = Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        final PageResp expectedResult = PageResp.success("msg");
        when(mockDkmOfflineCheckMapper.selectBluetoothBatchByHwDeviceSn(any()))
                .thenReturn(Arrays.asList("value"));
        when(mockDkmOfflineCheckMapper.selectBluetoothBatchByBleMacAddress(any()))
                .thenReturn(Arrays.asList("value"));
        when(mockDkmOfflineCheckMapper.insertBluetoothBatch(Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey")))).thenReturn(0);

        // Run the test
        final PageResp result = dkmOfflineCheckServiceImplUnderTest.insertBluetoothBatch(dkmBluetooths);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testInsertBluetoothBatch_DkmOfflineCheckMapperSelectBluetoothBatchByHwDeviceSnReturnsNoItems() throws Exception {
        // Setup
        final List<DkmBluetooths> dkmBluetooths = Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey"));
        final PageResp expectedResult = PageResp.success("msg");
        when(mockDkmOfflineCheckMapper.selectBluetoothBatchByHwDeviceSn(Arrays.asList("value")))
                .thenReturn(Collections.emptyList());
        when(mockDkmOfflineCheckMapper.selectBluetoothBatchByBleMacAddress(Arrays.asList("value")))
                .thenReturn(Arrays.asList("value"));
        when(mockDkmOfflineCheckMapper.insertBluetoothBatch(Arrays.asList(
                new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                        "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey")))).thenReturn(0);

        // Run the test
        final PageResp result = dkmOfflineCheckServiceImplUnderTest.insertBluetoothBatch(dkmBluetooths);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testInsertBluetoothBatch_DkmOfflineCheckMapperSelectBluetoothBatchByBleMacAddressReturnsNoItems() throws Exception {
        // Setup
        DkmBluetooths dkmBluetooths1 = new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey");
        final List<DkmBluetooths> dkmBluetooths = Arrays.asList(dkmBluetooths1
               );
        final PageResp expectedResult = PageResp.success("msg");
        when(mockDkmOfflineCheckMapper.selectBluetoothBatchByHwDeviceSn(Arrays.asList("value")))
                .thenReturn(Arrays.asList("value"));
        when(mockDkmOfflineCheckMapper.selectBluetoothBatchByBleMacAddress(Arrays.asList("value")))
                .thenReturn(Collections.emptyList());
        when(mockDkmOfflineCheckMapper.insertBluetoothBatch(Arrays.asList(
                dkmBluetooths1))).thenReturn(0);

        // Run the test
        final PageResp result = dkmOfflineCheckServiceImplUnderTest.insertBluetoothBatch(dkmBluetooths);
        dkmBluetooths1.setHwDeviceSn(null);
        final List<DkmBluetooths> dkmBluetooths2 = Arrays.asList(dkmBluetooths1
        );
        final PageResp result1 = dkmOfflineCheckServiceImplUnderTest.insertBluetoothBatch(dkmBluetooths2);
        // Verify the results
        for (int i = 0; i < 50; i++) {
            dkmBluetooths2.add(dkmBluetooths1);
        }
        final PageResp result2 = dkmOfflineCheckServiceImplUnderTest.insertBluetoothBatch(dkmBluetooths2);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testInsertBluetoothBatch_UploadOverMaximumException() throws Exception {
        // Setup
        DkmBluetooths dkmBluetooths1 = new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey");
         List<DkmBluetooths> dkmBluetooths = new ArrayList<>();
        final PageResp expectedResult = PageResp.success("msg");
        when(mockDkmOfflineCheckMapper.selectBluetoothBatchByHwDeviceSn(Arrays.asList("value")))
                .thenReturn(Arrays.asList("value"));
        when(mockDkmOfflineCheckMapper.selectBluetoothBatchByBleMacAddress(Arrays.asList("value")))
                .thenReturn(Collections.emptyList());
        when(mockDkmOfflineCheckMapper.insertBluetoothBatch(Arrays.asList(
                dkmBluetooths1))).thenReturn(0);

        // Run the test


        // Verify the results
        for (int i = 0; i < 52; i++) {
            dkmBluetooths.add(dkmBluetooths1);
        }
        final PageResp result2 = dkmOfflineCheckServiceImplUnderTest.insertBluetoothBatch(dkmBluetooths);
        assertThat(result2).isEqualTo(expectedResult);
    }
    @Test
    void testInsertBluetoothBatch_ParameterValidationException() throws Exception {
        // Setup
        DkmBluetooths dkmBluetooths1 = new DkmBluetooths("hwDeviceSn", "searchNumber", "hwDeviceProviderNo", "dkSdkVersion", "dkSecUnitId",
                "bleMacAddress", "masterKey", "digKey", 0, 0, "pubKey");
        List<DkmBluetooths> dkmBluetooths = new ArrayList<>();
        final PageResp expectedResult = PageResp.success("msg");
        when(mockDkmOfflineCheckMapper.selectBluetoothBatchByHwDeviceSn(Arrays.asList("value")))
                .thenReturn(Arrays.asList("value"));
        when(mockDkmOfflineCheckMapper.selectBluetoothBatchByBleMacAddress(Arrays.asList("value")))
                .thenReturn(Collections.emptyList());
        when(mockDkmOfflineCheckMapper.insertBluetoothBatch(Arrays.asList(
                dkmBluetooths1))).thenReturn(0);

        // Run the test


        // Verify the results
        for (int i = 0; i < 20; i++) {
            dkmBluetooths.add(dkmBluetooths1);
        }
        final PageResp result2 = dkmOfflineCheckServiceImplUnderTest.insertBluetoothBatch(dkmBluetooths);
        assertThat(result2).isEqualTo(expectedResult);
    }
    @Test
    void testGetKeyLogDetail() {
        // Setup
        final KeyLogDetailVO keyLogDetailVO = new KeyLogDetailVO();
        keyLogDetailVO.setPageIndex(0);
        keyLogDetailVO.setPageSize(0);
        keyLogDetailVO.setVin("vin");
        keyLogDetailVO.setStartTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        keyLogDetailVO.setEndTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        keyLogDetailVO.setUserId("userId");
        keyLogDetailVO.setStatusCode("statusCode");

        final PageResp expectedResult = PageResp.success("msg");

        // Configure DkmKeyLogMapper.selectList(...).
        final DkmKeyLog dkmKeyLog = new DkmKeyLog();
        dkmKeyLog.setId(0L);
        dkmKeyLog.setVin("vin");
        dkmKeyLog.setKeyId("keyId");
        dkmKeyLog.setPhoneModel("phoneModel");
        dkmKeyLog.setUserId("userId");
        dkmKeyLog.setPhoneBrand("phoneBrand");
        dkmKeyLog.setFlag(0);
        dkmKeyLog.setFlagVO("flagVO");
        dkmKeyLog.setStatusCode("statusCode");
        dkmKeyLog.setErrorReason("errorReason");
        dkmKeyLog.setOperateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        dkmKeyLog.setCreator("creator");
        dkmKeyLog.setUpdator("updator");
        dkmKeyLog.setCreateTime("createTime");
        dkmKeyLog.setUpdateTime("updateTime");
        final List<DkmKeyLog> dkmKeyLogs = Arrays.asList(dkmKeyLog);
        when(mockDkmKeyLogMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmKeyLogs);

        when(mockDkmKeyLogMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(0L, 0L, 0L, false));

        // Run the test
        final PageResp result = dkmOfflineCheckServiceImplUnderTest.getKeyLogDetail(keyLogDetailVO);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetKeyLogDetail_DkmKeyLogMapperSelectListReturnsNoItems() {
        // Setup
        final KeyLogDetailVO keyLogDetailVO = new KeyLogDetailVO();
        keyLogDetailVO.setPageIndex(0);
        keyLogDetailVO.setPageSize(0);
        keyLogDetailVO.setVin("vin");
        keyLogDetailVO.setStartTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        keyLogDetailVO.setEndTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        keyLogDetailVO.setUserId("userId");
        keyLogDetailVO.setStatusCode("statusCode");

        final PageResp expectedResult = PageResp.success("msg");
        when(mockDkmKeyLogMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());
        when(mockDkmKeyLogMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(0L, 0L, 0L, false));

        // Run the test
        final PageResp result = dkmOfflineCheckServiceImplUnderTest.getKeyLogDetail(keyLogDetailVO);
        keyLogDetailVO.setPageIndex(null);
        dkmOfflineCheckServiceImplUnderTest.getKeyLogDetail(keyLogDetailVO);
        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetKeyData() {
        // Setup
        final KeyLogDataVO keyLogDataVO = new KeyLogDataVO();
        keyLogDataVO.setPageIndex(0);
        keyLogDataVO.setPageSize(0);
        keyLogDataVO.setVin("vin");
        keyLogDataVO.setStartTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        keyLogDataVO.setEndTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        keyLogDataVO.setUserId("userId");
        keyLogDataVO.setStatusCode("statusCode");
        keyLogDataVO.setDkState(0);
        keyLogDataVO.setStatus(0);

        final PageResp expectedResult = PageResp.success("msg");
        Page<DkmKey> objectPage = new Page<>(0L, 0L, 0L, false);
        final List<DkmKey> dkmKeys = Arrays.asList(
                new DkmKey("id", 0, "userId", "vin", 0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0, "kr", "ks", "phoneFingerprint",
                        0, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "0", 0,
                        "personalAndCalibration", 0L, 0, "keyResourceVO", 0, "keyClassificationVO", "keyType",
                        "deviceType", "accountIdHash", "endpointId", "slotId", "keyOptions", "devicePublicKey",
                        "vehiclePublicKey", "authorizedPublicKeys", "privateMailbox", "confidentialMailbox",
                        "friendDeviceHandle", "friendPublicKey", "sharingPasswordInformation", "profile",
                        "keyFriendlyName","bleMacAddress"));
        objectPage.setRecords(dkmKeys);
        when(mockDkmKeyMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(objectPage);

        // Run the test
        final PageResp result = dkmOfflineCheckServiceImplUnderTest.getKeyData(keyLogDataVO);
        keyLogDataVO.setEndTime(null);
        dkmOfflineCheckServiceImplUnderTest.getKeyData(keyLogDataVO);
        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }
}
