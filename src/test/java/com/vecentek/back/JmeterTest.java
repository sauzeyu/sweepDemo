package com.vecentek.back;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvBuilder;
import com.payneteasy.tlv.HexUtil;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.DkmUser;
import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.mapper.DkmKeyMapper;
import com.vecentek.back.mapper.DkmUserMapper;
import com.vecentek.back.mapper.DkmVehicleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-30 14:13
 */
@SpringBootTest
public class JmeterTest {

    @Resource
    private DkmUserMapper dkmUserMapper;
    @Resource
    private DkmKeyMapper dkmKeyMapper;
    @Resource
    private DkmVehicleMapper dkmVehicleMapper;

    public static void main(String[] args) {

        BerTlvBuilder tlv = new BerTlvBuilder();


        tlv.addBytes(new BerTag(0X9F, 0X1F), "18715795031".getBytes(StandardCharsets.UTF_8));
        tlv.addBytes(new BerTag(0X1C), "123".getBytes(StandardCharsets.UTF_8));
        tlv.addBytes(new BerTag(0X1D), "a".getBytes(StandardCharsets.UTF_8));
        byte[] bytes = tlv.buildArray();
        System.out.println(Arrays.toString(bytes));
        System.err.println(HexUtil.toHexString(bytes));

    }

    public static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    @Test
    public void test() {

        dkmKeyMapper.selectList(null).forEach(key -> {
            DkmVehicle dkmVehicle = dkmVehicleMapper.selectById(key.getVehicleId());
            if (dkmVehicle != null) {
                key.setVin(dkmVehicle.getVin());
            }
            dkmKeyMapper.updateById(key);
        });
    }

    @Test
    public void login() throws IOException {
        LambdaQueryWrapper<DkmUser> queryWrapper = Wrappers.<DkmUser>lambdaQuery().last("limit 10000");
        List<DkmUser> dkmUsers = dkmUserMapper.selectList(queryWrapper);
        System.out.println(dkmUsers);
        for (DkmUser dkmUser : dkmUsers) {

            OutputStream os = Files.newOutputStream(Paths.get("G:\\JmeterTest3\\data\\login\\login" + dkmUser.getId()));

            BerTlvBuilder tlv = new BerTlvBuilder();

            tlv.addBytes(new BerTag(0x9f, 0x1f), dkmUser.getPhone().getBytes(StandardCharsets.UTF_8));
            // tlv.addBytes(new BerTag(0x1c), dkmUser.getPassword().getBytes(StandardCharsets.UTF_8));
            // tlv.addBytes(new BerTag(0x1d), dkmUser.getPhoneFingerprint().getBytes(StandardCharsets.UTF_8));

            byte[] bytes = tlv.buildArray();

            os.write(bytes);
            os.flush();
            os.close();
        }
    }

    @Test
    public void signRTC() throws IOException {
        LambdaQueryWrapper<DkmUser> queryWrapper = Wrappers.<DkmUser>lambdaQuery().last("limit 10000");
        List<DkmUser> dkmUsers = dkmUserMapper.selectList(queryWrapper);
        System.out.println(dkmUsers);
        for (DkmUser dkmUser : dkmUsers) {

            OutputStream os = new FileOutputStream(new File("G:\\JmeterTest3\\data\\login\\login" + dkmUser.getId()));

            BerTlvBuilder tlv = new BerTlvBuilder();

            tlv.addBytes(new BerTag(0x9f, 0x1f), dkmUser.getPhone().getBytes(StandardCharsets.UTF_8));
            // tlv.addBytes(new BerTag(0x1c), dkmUser.getPassword().getBytes(StandardCharsets.UTF_8));
            // tlv.addBytes(new BerTag(0x1d), dkmUser.getPhoneFingerprint().getBytes(StandardCharsets.UTF_8));

            byte[] bytes = tlv.buildArray();

            os.write(bytes);
            os.flush();
            os.close();
        }
    }

    @Test
    public void openKey() throws IOException {
        LambdaQueryWrapper<DkmUser> queryWrapper = Wrappers.<DkmUser>lambdaQuery()
                .ge(DkmUser::getId, 1000001)
                .le(DkmUser::getId, 1000999)
                .last("limit 999");
        List<DkmUser> dkmUsers = dkmUserMapper.selectList(queryWrapper);
        System.out.println(dkmUsers);
        for (DkmUser dkmUser : dkmUsers) {
            OutputStream os = new FileOutputStream(new File("G:\\JmeterTest3\\data\\openKey\\openKey" + dkmUser.getId()));
            BerTlvBuilder tlv = new BerTlvBuilder();
            tlv.addBytes(new BerTag(0X1B), dkmUser.getId().getBytes(StandardCharsets.UTF_8));
            // tlv.addBytes(new BerTag(0X1D), dkmUser.getPhoneFingerprint().getBytes(StandardCharsets.UTF_8));
            tlv.addBytes(new BerTag(0X9F, 0X62), dkmUser.getId().getBytes(StandardCharsets.UTF_8));
            byte[] bytes = tlv.buildArray();
            os.write(bytes);
            os.flush();
            os.close();
        }
    }

    @Test
    public void register() throws IOException {
        for (int i = 0; i < 100000; i++) {
            OutputStream os = new FileOutputStream(new File("G:\\JmeterTest3\\data\\register\\register" + i));
            BerTlvBuilder tlv = new BerTlvBuilder();
            String phone = "20220506" + i;
            String name = "20220506" + i;
            String password = "test";
            tlv.addBytes(new BerTag(0x9f, 0x1f), phone.getBytes(StandardCharsets.UTF_8));
            tlv.addBytes(new BerTag(0x1c), password.getBytes(StandardCharsets.UTF_8));
            tlv.addBytes(new BerTag(0x9f, 0x6b), name.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = tlv.buildArray();
            os.write(bytes);
            os.flush();
            os.close();
        }
    }

    @Test
    public void cancelKey() throws IOException {
        LambdaQueryWrapper<DkmKey> queryWrapper = Wrappers.<DkmKey>lambdaQuery()
                .ge(DkmKey::getUserId, 1000001)
                .le(DkmKey::getUserId, 1000999)
                .last("limit 999");
        List<DkmKey> dkmKeys = dkmKeyMapper.selectList(queryWrapper);
        System.out.println(dkmKeys);
        for (DkmKey dkmKey : dkmKeys) {

            OutputStream os = new FileOutputStream(new File("G:\\JmeterTest3\\data\\cancelKey\\cancelKey" + dkmKey.getId()));

            BerTlvBuilder tlv = new BerTlvBuilder();

            tlv.addBytes(new BerTag(0X07), (dkmKey.getId() + "").getBytes(StandardCharsets.UTF_8));

            byte[] bytes = tlv.buildArray();

            os.write(bytes);
            os.flush();
            os.close();
        }
    }

    @Test
    public void shareKey() throws IOException {
        for (int i = 1000000; i <= 1001000; i++) {

            OutputStream os = new FileOutputStream(new File("G:\\JmeterTest3\\data\\shareKey\\shareKey" + i));
            byte[] permission = {0, 0, 0, 0, 0, 31};
            BerTlvBuilder tlv = new BerTlvBuilder();
            tlv.addBytes(new BerTag(0X1B), (i + "").getBytes(StandardCharsets.UTF_8));
            tlv.addBytes(new BerTag(0X9F, 0X64), permission);
            tlv.addBytes(new BerTag(0X9F, 0X65), "20220406T163929Z".getBytes(StandardCharsets.UTF_8));
            tlv.addBytes(new BerTag(0X9F, 0X66), "20220601T163931Z".getBytes(StandardCharsets.UTF_8));
            tlv.addBytes(new BerTag(0X9F, 0X68), (i + 1 + "").getBytes(StandardCharsets.UTF_8));
            tlv.addBytes(new BerTag(0X1D), ("aaazzz").getBytes(StandardCharsets.UTF_8));
            tlv.addBytes(new BerTag(0X9F, 0X62), (i + "").getBytes(StandardCharsets.UTF_8));
            tlv.addBytes(new BerTag(0X07), (i + "").getBytes(StandardCharsets.UTF_8));

            byte[] bytes = tlv.buildArray();

            os.write(bytes);
            os.flush();
            os.close();
        }
    }

    @Test
    public void refreshKey() throws IOException {
        for (int i = 1000000; i <= 1001000; i++) {

            OutputStream os = new FileOutputStream(new File("G:\\JmeterTest3\\data\\refreshKey\\refreshKey" + i));
            BerTlvBuilder tlv = new BerTlvBuilder();
            tlv.addBytes(new BerTag(0x1b), (i + "").getBytes(StandardCharsets.UTF_8));
            tlv.addBytes(new BerTag(0x09),
                    "044DE55BA73D0B5BEF447AD516F142554ED07ADFE87736321A6C355AC4A3BF8B0A326BDA3BB7CCB374ADAC5CAF8459176ECF060968A6E1C8291D380880C9D0BEA8".getBytes(
                            StandardCharsets.UTF_8));
            byte[] bytes = tlv.buildArray();

            os.write(bytes);
            os.flush();
            os.close();
        }
    }

    @Test
    public void openKeyT() throws IOException {
        OutputStream os = new FileOutputStream(new File("G:\\JmeterTest\\openKey\\openKey"));


        os.write(toBytes(
                "1101141B01391D10333735653966376400000000000000009F62114B5042425854424A39445331344D5552579F6081C865794A68624763694F694A49557A49314E694973496E523563434936496B705856434A392E65794A7759584E7A643239795A434936496A49315A445531595751794F444E68595451774D47466D4E445930597A63325A4463784D324D774E32466B496977695A586877496A6F784E6A51344E7A55324D6A63304C434A3163325679626D46745A534936496A45314E7A41774D6A49314D7A6B78496E302E6432497639504455423153783246695941384137305A31415656334E324459687441556467535365466267"));
        os.flush();
        os.close();
    }

    @Test
    public void hmac() {
        byte[] k1 = "32323232323232323232323232323232".getBytes(StandardCharsets.UTF_8);
        byte[] bytes = toBytes(
                "00000000001F000000000000000000363339313331373230323230333331543130353032345A3230333230333331543130353032345A4B5042425854424A39445331344D555257333735653966376400000000000000000000000000000000000000000039010000000000000000000000000000000001000530333331313035303234303731363332");
        HMac hMac = new HMac(HmacAlgorithm.HmacSHA256, HexUtil.parseHex("32323232323232323232323232323232"));
        String s = hMac.digestHex(HexUtil.parseHex(
                "00000000001F000000000000000000363339313331373230323230333331543130353032345A3230333230333331543130353032345A4B5042425854424A39445331344D555257333735653966376400000000000000000000000000000000000000000039010000000000000000000000000000000001000530333331313035303234303731363332"));
        System.out.println(s);

    }

    /**
     * 制造上传钥匙使用日志测试数据
     */
    @Test
    public void uploadKeyLog() throws IOException {
        for (int i = 0; i <= 250; i++) {
            // 使用日志 statusCode 1 byte
            // 使用日志 statusCode 2 byte
            // 错误日志 statusCode 1 byte
            // 错误日志 statusCode 2 byte
            OutputStream os = new FileOutputStream(new File("E:\\uploadKeyLog\\uploadKeyLog_useLog_1byte" + i));
            BerTlvBuilder tlv = new BerTlvBuilder();
            // VIN
            tlv.addBytes(new BerTag(0X9F, 0X62),
                    RandomUtil.randomString(17).toUpperCase().getBytes(StandardCharsets.UTF_8));
            // keyId
            tlv.addBytes(new BerTag(0X07), (i + "").getBytes(StandardCharsets.UTF_8));
            // statusCode 1个byte
            ArrayList<String> list = CollUtil.newArrayList("08",
                    "0A",
                    "0B",
                    "0C",
                    "0D",
                    "03",
                    "04",
                    "05",
                    "06",
                    "09",
                    "0E");
            tlv.addBytes(new BerTag(0X9F, 0X3B), RandomUtil.randomEle(list).getBytes(StandardCharsets.UTF_8));
            // operateTime
            tlv.addBytes(new BerTag(0X9F, 0X3D), "20220406T163929Z".getBytes(StandardCharsets.UTF_8));
            // userId
            tlv.addBytes(new BerTag(0X1b), (i + "").getBytes(StandardCharsets.UTF_8));
            // phoneBrand
            tlv.addBytes(new BerTag(0X9F, 0X16), ("压测手机").getBytes(StandardCharsets.UTF_8));
            // phoneModel
            tlv.addBytes(new BerTag(0X9F, 0X17), ("压测手机").getBytes(StandardCharsets.UTF_8));
            // errorReason 为空 为使用日志
//            tlv.addBytes(new BerTag(0X9F, 0X3C), ("压测手机").getBytes(StandardCharsets.UTF_8));
            byte[] bytes = tlv.buildArray();
            os.write(bytes);
            os.flush();
            os.close();
        }
    }

    @Test
    public void uploadKeyLog2() throws IOException {
        for (int i = 0; i <= 250; i++) {
            // 使用日志 statusCode 1 byte
            // 使用日志 statusCode 2 byte
            // 错误日志 statusCode 1 byte
            // 错误日志 statusCode 2 byte
            OutputStream os = new FileOutputStream(new File("E:\\uploadKeyLog\\uploadKeyLog_useLog_2byte" + i));
            BerTlvBuilder tlv = new BerTlvBuilder();
            // VIN
            tlv.addBytes(new BerTag(0X9F, 0X62),
                    RandomUtil.randomString(17).toUpperCase().getBytes(StandardCharsets.UTF_8));
            // keyId
            tlv.addBytes(new BerTag(0X07), (i + "").getBytes(StandardCharsets.UTF_8));
            // statusCode 1个byte
            ArrayList<String> list = CollUtil.newArrayList("0400",
                    "0401",
                    "0500",
                    "0501",
                    "0600",
                    "0601",
                    "0700",
                    "0701",
                    "0800",
                    "0801");
            tlv.addBytes(new BerTag(0X9F, 0X3B), RandomUtil.randomEle(list).getBytes(StandardCharsets.UTF_8));
            // operateTime
            tlv.addBytes(new BerTag(0X9F, 0X3D), "20220406T163929Z".getBytes(StandardCharsets.UTF_8));
            // userId
            tlv.addBytes(new BerTag(0X1b), (i + "").getBytes(StandardCharsets.UTF_8));
            // phoneBrand
            tlv.addBytes(new BerTag(0X9F, 0X16), ("压测手机").getBytes(StandardCharsets.UTF_8));
            // phoneModel
            tlv.addBytes(new BerTag(0X9F, 0X17), ("压测手机").getBytes(StandardCharsets.UTF_8));
            // errorReason 为空 为使用日志
//            tlv.addBytes(new BerTag(0X9F, 0X3C), ("压测手机").getBytes(StandardCharsets.UTF_8));
            byte[] bytes = tlv.buildArray();
            os.write(bytes);
            os.flush();
            os.close();
        }
    }

    @Test
    public void rand() {
        ArrayList<String> list = CollUtil.newArrayList("08",
                "0A",
                "0B",
                "0C",
                "0D",
                "03",
                "04",
                "05",
                "06",
                "09",
                "0E");
        String s = RandomUtil.randomEle(list);
        System.out.println(s);
    }
}
