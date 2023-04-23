package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.vecentek.back.entity.DkmKeyLog;
import com.vecentek.back.entity.DkmSystemConfigurationExpired;
import com.vecentek.back.entity.DkmUser;
import com.vecentek.back.mapper.DkmUserMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmUserServiceImplTest {

    @Mock
    private DkmUserMapper mockDkmUserMapper;

    @InjectMocks
    private DkmUserServiceImpl dkmUserServiceImplUnderTest;
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmKeyLog.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmSystemConfigurationExpired.class);
    }
    @Test
    void testSelectById() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmUserMapper.selectById(0)).thenReturn(new DkmUser("id", "phone", "username"));

        // Run the test
        final PageResp result = dkmUserServiceImplUnderTest.selectById(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectById_DkmUserMapperReturnsNull() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询失败，用户不存在");
        when(mockDkmUserMapper.selectById(0)).thenReturn(null);

        // Run the test
        final PageResp result = dkmUserServiceImplUnderTest.selectById(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }
}
