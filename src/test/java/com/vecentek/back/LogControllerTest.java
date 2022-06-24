package com.vecentek.back;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmAdmin;
import com.vecentek.back.mapper.DkmAdminMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

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


}
