package com.vecentek.back;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.dto.CountDTO;
import com.vecentek.back.entity.DkmAdmin;
import com.vecentek.back.mapper.DkmAdminMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-02-23 16:39
 */
@SpringBootTest
public class LogControllerTest {
    @Resource
    private DkmAdminMapper dkmAdminMapper;


    @Test
    public void testMapper() {
        Page<DkmAdmin> dkmAdminPage = dkmAdminMapper.selectPage(new Page<>(1, 10), null);
        System.out.println(dkmAdminPage);
    }

    @Test
    public void test1() {
        String s = "0400";
        if (ObjectUtil.equals(StrUtil.sub(s, 0, 2), "04")
                && ObjectUtil.equals(StrUtil.sub(s, 3, 4), "00")) { // 发动机开启
            System.out.println(123);

        }
        System.out.println(StrUtil.sub(s, 0, 2));
        System.out.println(StrUtil.sub(s, 2, 4));
    }

    @Test
    public void test3() {
        CountDTO countDTO = new CountDTO();
        countDTO.setName("0400");
        countDTO.setValue(2);
        List<Map> maps = test2(countDTO, "04");
        System.out.println(maps);
    }

    List<Map> test2(CountDTO countDTO, String statusCode) {
        String type = "";
        if (ObjectUtil.equals(statusCode, "04")) {
            type = "发动机";
        }
        List<Map> data = new ArrayList<>();
        int engineOpenInt = 0; // 发动机开启总条数
        if (ObjectUtil.equals(StrUtil.sub(countDTO.getName(), 0, 2), statusCode)
                && ObjectUtil.equals(StrUtil.sub(countDTO.getName(), 2, 4), "00")) { // 发动机关闭
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", type + "关闭");
            map.put("value", countDTO.getValue());
            data.add(map);
        } else if (ObjectUtil.equals(StrUtil.sub(countDTO.getName(), 0, 2), statusCode)
                && ObjectUtil.notEqual(StrUtil.sub(countDTO.getName(), 2, 4), "00")) { // 发动机开启
            Integer value = countDTO.getValue();
            int intValue = value.intValue();
            engineOpenInt += intValue;
        }
        if (engineOpenInt != 0) {
            HashMap<String, Object> engineOpenMap = new HashMap<>();
            engineOpenMap.put("name", type + "开启");
            engineOpenMap.put("value", engineOpenInt);
            data.add(engineOpenMap);
        }
        return data;
    }


}
