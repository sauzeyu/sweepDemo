package com.vecentek.back;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vecentek.back.entity.DkmAdmin;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.mapper.DkmAdminMapper;
import com.vecentek.back.mapper.DkmKeyMapper;
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

}
