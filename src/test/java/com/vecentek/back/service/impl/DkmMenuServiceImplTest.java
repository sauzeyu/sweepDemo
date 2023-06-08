package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmMenu;
import com.vecentek.back.mapper.DkmMenuMapper;
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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmMenuServiceImplTest {

    @Mock
    private DkmMenuMapper mockDkmMenuMapper;

    @InjectMocks
    private DkmMenuServiceImpl dkmMenuServiceImplUnderTest;
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmMenu.class);
    }

    @Test
    void testSelectAll() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmMenuMapper.selectList(...).
        final List<DkmMenu> dkmMenus = Arrays.asList(
                new DkmMenu(0, 0, "title", "icon", "href", "target", "isShow", 0, "dna"));
        when(mockDkmMenuMapper.selectList(any(Wrapper.class))).thenReturn(dkmMenus);

        // Run the test
        final PageResp result = dkmMenuServiceImplUnderTest.selectAll();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectAll_DkmMenuMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmMenuMapper.selectList(any(Wrapper.class))).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmMenuServiceImplUnderTest.selectAll();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectMenuByRoleId() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmMenuMapper.selectMenuByRoleId(0)).thenReturn(Arrays.asList("value"));

        // Run the test
        final PageResp result = dkmMenuServiceImplUnderTest.selectMenuByRoleId(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectMenuByRoleId_DkmMenuMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmMenuMapper.selectMenuByRoleId(0)).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmMenuServiceImplUnderTest.selectMenuByRoleId(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectForPage() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmMenuMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(0L, 0L, 0L, false));

        // Run the test
        final PageResp result = dkmMenuServiceImplUnderTest.selectForPage(0, 0, "title", "icon", "href");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectByParentId() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmMenuMapper.selectList(...).
        final List<DkmMenu> dkmMenus = Arrays.asList(
                new DkmMenu(0, 0, "title", "icon", "href", "target", "isShow", 0, "dna"));
        when(mockDkmMenuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(dkmMenus);

        // Run the test
        final PageResp result = dkmMenuServiceImplUnderTest.selectByParentId(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectByParentId_DkmMenuMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmMenuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmMenuServiceImplUnderTest.selectByParentId(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }
}
