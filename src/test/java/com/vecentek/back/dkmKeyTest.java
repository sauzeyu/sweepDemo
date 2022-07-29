package com.vecentek.back;

import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.mapper.DkmKeyMapper;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author liujz
 * @date 2022/7/4
 * @apiNote
 */
@SpringBootTest
public class dkmKeyTest {
    private static DkmKeyMapper dkmKeyMapper;

    public static void main(String[] args) {
        testInsert();
    }

    @Test
    public static void testInsert() {

        for (int i = 0; i < 100000; i++) {
            Date randomDate = randomDate("2020-09-20", "2022-09-22");
            DkmKey dkmKey = new DkmKey();
            dkmKey.setVehicleId(getRandom2(10));
            dkmKey.setDkState(getRandom2(1));
            dkmKey.setPermissions(getRandom2(1));
            dkmKey.setApplyTime(randomDate);
            dkmKeyMapper.insert(dkmKey);
        }
    }

    public static Integer getRandom2(int len) {
        Random r = new Random();
        StringBuilder rs = new StringBuilder();
        for (int i = 0; i < len; i++) {
            rs.append(r.nextInt(10));
        }
        return Integer.parseInt(rs.toString());

    }

    /**
     * 3.org.apache.commons.lang包下有一个RandomStringUtils类，
     * 其中有一个randomAlphanumeric(int length)函数，可以随机生成一个长度为length的字符串。
     *
     * @param length
     * @return
     */
    public static String createRandomStr3(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }


    /**
     * 生成随机时间
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static Date randomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);// 构造开始日期
            Date end = format.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }

    public static void setDkmKeyMapper(DkmKeyMapper dkmKeyMapper) {
        dkmKeyTest.dkmKeyMapper = dkmKeyMapper;
    }
}
