package com.vecentek.back;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.vecentek.back.entity.DkmBluetooths;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.mapper.DkmAdminMapper;
import com.vecentek.back.mapper.DkmBluetoothsMapper;
import com.vecentek.back.mapper.DkmKeyMapper;
import com.vecentek.back.mapper.DkmVehicleMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
public class FakeDataTest {
    @Resource
    private DkmKeyMapper dkmKeyMapper;
    @Resource
    private DkmAdminMapper dkmAdminMapper;
    @Resource
    private DkmBluetoothsMapper dkmBluetoothsMapper;
    @Resource
    private DkmVehicleMapper dkmVehicleMapper;

    @Test
    public void insert() {

        for (int i = 300; i < 310; i++) {
            DkmKey key = new DkmKey();
            key.setApplyTime(new Date());
            key.setVehicleId(i);
            key.setUserId(String.valueOf(i));
            key.setDkState(RandomUtil.randomInt(1, 5));
            key.setPermissions(31);
            key.setPersonalAndCalibration("9FB915F9F66B482839A1106227DD82D14DB980B18A6C0375AFC776FC8CA9C03A036950686F6E650000000000006950686F6E6531322C38000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000000");
            key.setKr("00000000001F000000000000000034313131363335313230323230373134543137353532355A3230333230373134543137353532355A4F5259535559303450384D30344C57534E343137373032353664643735623536610000000000000033303431343633010000000000000000000000000000000001000530373134313735353235333036343737");
            key.setKs("52B43D2D5F3E88C817A4FC699772ED30");
            key.setPhoneFingerprint("c93b2989aa3a63");
            key.setParentId("2240007");
            key.setVin(RandomUtil.randomString(17).toUpperCase());
            dkmKeyMapper.insert(key);
        }

    }

    @Test
    public void select() {
//        DkmAdmin admin = dkmAdminMapper.selectOne(new QueryWrapper<DkmAdmin>().eq("username", "ADMIN"));
//        System.out.println(admin);

        System.out.println(StringUtils.capitalize("userRoles"));
    }

    /**
     * 同时制造蓝牙表和车辆表测试数据
     */
    @Test
    public void idTest() {
        int num = 0;
        for (int i = 0; i < 990; i++) {
            String s = IdUtil.simpleUUID();
            DkmBluetooths dkmBluetooths = new DkmBluetooths();
            DkmVehicle dkmVehicle = new DkmVehicle();
            String bleMac = RandomUtil.randomString(12).toUpperCase();
            String bleSn = "30323130" + s.toUpperCase();
            String searchNum = "503430" + s.toUpperCase();
            String pk = RandomUtil.randomString(98).toUpperCase() + s.toUpperCase();
            // 蓝牙
            dkmBluetooths.setDkSecUnitId("AAAAAAA"); // 本次制造假数据标识符
            dkmBluetooths.setHwDeviceSn(bleSn);
            dkmBluetooths.setSearchNumber(searchNum);
            dkmBluetooths.setHwDeviceProviderNo("P4001");
            dkmBluetooths.setDkSdkVersion("1");
            dkmBluetooths.setBleMacAddress(bleMac);
            dkmBluetooths.setPubKey(pk);
            dkmBluetooths.setCreateTime(new Date());
            dkmBluetooths.setDigKey(RandomUtil.randomNumbers(64));
            dkmBluetooths.setMasterKey(RandomUtil.randomNumbers(32));
            dkmBluetooths.setFlag(1);
            dkmBluetooths.setCreator("fakeDataTest");
            int insert = dkmBluetoothsMapper.insert(dkmBluetooths);
            // 车辆
            dkmVehicle.setBleMacAddress(bleMac);
            dkmVehicle.setHwDeviceProviderNo("P4001");
            dkmVehicle.setHwDeviceSn(bleSn);
            dkmVehicle.setDkSecUnitId("AAAAAAA");
            dkmVehicle.setSearchNumber(searchNum);
            dkmVehicle.setVin(RandomUtil.randomString(17).toUpperCase());
            dkmVehicle.setVehicleModel("P4");
            dkmVehicle.setVehicleBrand("福田");
            dkmVehicle.setVehicleType("大将军");
            dkmVehicle.setHwDeviceProviderNo("P4001");
            dkmVehicle.setPubKey(pk);
            dkmVehicle.setCreateTime(new Date());
            dkmVehicle.setCreator("fakeDataTest");
            int insert1 = dkmVehicleMapper.insert(dkmVehicle);

            if (insert == 1 && insert1 == 1) {
                num++;
            }
        }

        System.out.println("成功插入" + num + "条");
    }

}
