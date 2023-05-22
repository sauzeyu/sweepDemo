package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmAdmin;
import com.vecentek.back.entity.DkmAdminRole;
import com.vecentek.back.entity.DkmRole;
import com.vecentek.back.entity.DkmRoleMenu;
import com.vecentek.back.mapper.DkmAdminMapper;
import com.vecentek.back.mapper.DkmAdminRoleMapper;
import com.vecentek.back.mapper.DkmMenuMapper;
import com.vecentek.back.mapper.DkmRoleMapper;
import com.vecentek.back.mapper.DkmRoleMenuMapper;
import com.vecentek.back.vo.InsertRoleVO;
import com.vecentek.back.vo.RoleDTO;
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
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmRoleServiceImplTest {

    @Mock
    private DkmAdminMapper mockDkmAdminMapper;
    @Mock
    private DkmRoleMapper mockDkmRoleMapper;
    @Mock
    private DkmMenuMapper mockDkmMenuMapper;
    @Mock
    private DkmRoleMenuMapper mockDkmRoleMenuMapper;
    @Mock
    private DkmAdminRoleMapper mockDkmAdminRoleMapper;
    @Mock
    private RedisTemplate mockRedisTemplate;

    @InjectMocks
    private DkmRoleServiceImpl dkmRoleServiceImplUnderTest;
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmRole.class);
    }
    @Test
    void testSelectForPage() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        Page<DkmRole> dkmRolePage = new Page<>(0L, 0L, 0L, false);
        DkmRole dkmRole = new DkmRole();
        dkmRole.setId(1);
        when(mockDkmRoleMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(dkmRolePage);
        when(mockDkmMenuMapper.selectMenuByRoleId(0)).thenReturn(Arrays.asList("value"));

        // Run the test
        final PageResp result = dkmRoleServiceImplUnderTest.selectForPage(0, 0, "roleName", "code", "startTime",
                "endTime");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectForPage_DkmMenuMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmRoleMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<>(0L, 0L, 0L, false));
        when(mockDkmMenuMapper.selectMenuByRoleId(0)).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmRoleServiceImplUnderTest.selectForPage(0, 0, "roleName", "code", "startTime",
                "endTime");

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testDeleteById() {
        // Setup
        final PageResp expectedResult = PageResp.success("删除成功");
        when(mockDkmRoleMapper.deleteById("value")).thenReturn(0);
        when(mockDkmAdminRoleMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Arrays.asList(new DkmAdminRole(0, 0, 0)));

        // Configure DkmAdminMapper.selectById(...).
        final DkmAdmin dkmAdmin = new DkmAdmin(0, "username", "password", "extraInfo");
        when(mockDkmAdminMapper.selectById(0)).thenReturn(dkmAdmin);

        when(mockRedisTemplate.hasKey("username")).thenReturn(false);
        when(mockRedisTemplate.delete("username")).thenReturn(false);
        when(mockDkmRoleMenuMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmAdminRoleMapper.deleteByRoleId(0)).thenReturn(0);

        // Run the test
        final PageResp result = dkmRoleServiceImplUnderTest.deleteById(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        verify(mockDkmRoleMapper).deleteById("value");
        verify(mockDkmRoleMenuMapper).delete(any(LambdaQueryWrapper.class));
        verify(mockDkmAdminRoleMapper).deleteByRoleId(0);
    }

    @Test
    void testDeleteById_DkmAdminRoleMapperSelectListReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("操作成功");
        when(mockDkmRoleMapper.deleteById("value")).thenReturn(0);
        when(mockDkmAdminRoleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Configure DkmAdminMapper.selectById(...).
        final DkmAdmin dkmAdmin = new DkmAdmin(0, "username", "password", "extraInfo");
        when(mockDkmAdminMapper.selectById(0)).thenReturn(dkmAdmin);

        when(mockRedisTemplate.hasKey(any())).thenReturn(false);
        when(mockRedisTemplate.delete(any())).thenReturn(1L);
        when(mockDkmRoleMenuMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmAdminRoleMapper.deleteByRoleId(0)).thenReturn(0);

        // Run the test
        final PageResp result = dkmRoleServiceImplUnderTest.deleteById(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        //verify(mockDkmRoleMapper).deleteById("value");
        //verify(mockDkmRoleMenuMapper).delete(any(LambdaQueryWrapper.class));
        //verify(mockDkmAdminRoleMapper).deleteByRoleId(0);
    }

    @Test
    void testDeleteById_RedisTemplateHasKeyReturnsNull() {
        // Setup
        final PageResp expectedResult = PageResp.success("操作成功");
        when(mockDkmRoleMapper.deleteById("value")).thenReturn(0);
        when(mockDkmAdminRoleMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Arrays.asList(new DkmAdminRole(0, 0, 0)));

        // Configure DkmAdminMapper.selectById(...).
        final DkmAdmin dkmAdmin = new DkmAdmin(0, "username", "password", "extraInfo");
        when(mockDkmAdminMapper.selectById(0)).thenReturn(dkmAdmin);

        when(mockRedisTemplate.hasKey(any())).thenReturn(null);
        when(mockRedisTemplate.delete(any())).thenReturn(1L);
        when(mockDkmRoleMenuMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmAdminRoleMapper.deleteByRoleId(0)).thenReturn(0);

        // Run the test
        final PageResp result = dkmRoleServiceImplUnderTest.deleteById(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        verify(mockDkmRoleMapper).deleteById("value");
        verify(mockDkmRoleMenuMapper).delete(any(LambdaQueryWrapper.class));
        verify(mockDkmAdminRoleMapper).deleteByRoleId(0);
    }

    @Test
    void testDeleteById_RedisTemplateDeleteReturnsNull() {
        // Setup
        final PageResp expectedResult = PageResp.success("操作成功");
        when(mockDkmRoleMapper.deleteById("value")).thenReturn(0);
        when(mockDkmAdminRoleMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Arrays.asList(new DkmAdminRole(0, 0, 0)));

        // Configure DkmAdminMapper.selectById(...).
        final DkmAdmin dkmAdmin = new DkmAdmin(0, "username", "password", "extraInfo");
        when(mockDkmAdminMapper.selectById(0)).thenReturn(dkmAdmin);

        when(mockRedisTemplate.hasKey(any())).thenReturn(null);
        when(mockRedisTemplate.delete(any())).thenReturn(1L);
        when(mockDkmRoleMenuMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmAdminRoleMapper.deleteByRoleId(0)).thenReturn(0);

        // Run the test
        final PageResp result = dkmRoleServiceImplUnderTest.deleteById(0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        verify(mockDkmRoleMapper).deleteById("value");
        verify(mockDkmRoleMenuMapper).delete(any(LambdaQueryWrapper.class));
        verify(mockDkmAdminRoleMapper).deleteByRoleId(0);
    }

    @Test
    void testUpdateRoleById() {
        // Setup
        final RoleDTO role = new RoleDTO(Arrays.asList("123"));
        role.setId(123);
        role.setRoleName("123");
        role.setCode("123");
        final PageResp expectedResult = PageResp.success("操作成功");

        // Configure DkmRoleMapper.selectOne(...).
        final DkmRole dkmRole = new DkmRole(123, 0, "123", "roleName", "intro");
        when(mockDkmRoleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmRole);

        when(mockDkmRoleMapper.update(eq(new DkmRole(123, 0, "123", "roleName", "intro")),
                any(LambdaUpdateWrapper.class))).thenReturn(0);
        when(mockDkmRoleMenuMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmRoleMenuMapper.insert(new DkmRoleMenu(0, 0, 0))).thenReturn(0);
        when(mockDkmAdminRoleMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Arrays.asList(new DkmAdminRole(0, 0, 0)));

        // Configure DkmAdminMapper.selectById(...).
        final DkmAdmin dkmAdmin = new DkmAdmin(0, "username", "password", "extraInfo");
        when(mockDkmAdminMapper.selectById(0)).thenReturn(dkmAdmin);

        when(mockRedisTemplate.hasKey("username")).thenReturn(false);
        when(mockRedisTemplate.delete("username")).thenReturn(false);

        // Run the test
        final PageResp result = dkmRoleServiceImplUnderTest.updateRoleById(role);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        //verify(mockDkmRoleMapper).update(eq(new DkmRole(0, 0, "code", "roleName", "intro")),
        //        any(LambdaUpdateWrapper.class));
        //verify(mockDkmRoleMenuMapper).delete(any(LambdaQueryWrapper.class));
        //verify(mockDkmRoleMenuMapper).insert(new DkmRoleMenu(0, 0, 0));
    }

    @Test
    void testUpdateRoleById_DkmAdminRoleMapperReturnsNoItems() {
        // Setup
        final RoleDTO role = new RoleDTO(Arrays.asList("value"));
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmRoleMapper.selectOne(...).
        final DkmRole dkmRole = new DkmRole(0, 0, "code", "roleName", "intro");
        when(mockDkmRoleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmRole);

        when(mockDkmRoleMapper.update(eq(new DkmRole(0, 0, "code", "roleName", "intro")),
                any(LambdaUpdateWrapper.class))).thenReturn(0);
        when(mockDkmRoleMenuMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmRoleMenuMapper.insert(new DkmRoleMenu(0, 0, 0))).thenReturn(0);
        when(mockDkmAdminRoleMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        // Configure DkmAdminMapper.selectById(...).
        final DkmAdmin dkmAdmin = new DkmAdmin(0, "username", "password", "extraInfo");
        when(mockDkmAdminMapper.selectById(0)).thenReturn(dkmAdmin);

        when(mockRedisTemplate.hasKey("username")).thenReturn(false);
        when(mockRedisTemplate.delete("username")).thenReturn(false);

        // Run the test
        final PageResp result = dkmRoleServiceImplUnderTest.updateRoleById(role);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        verify(mockDkmRoleMapper).update(eq(new DkmRole(0, 0, "code", "roleName", "intro")),
                any(LambdaUpdateWrapper.class));
        verify(mockDkmRoleMenuMapper).delete(any(LambdaQueryWrapper.class));
        verify(mockDkmRoleMenuMapper).insert(new DkmRoleMenu(0, 0, 0));
    }

    @Test
    void testUpdateRoleById_RedisTemplateHasKeyReturnsNull() {
        // Setup
        final RoleDTO role = new RoleDTO(Arrays.asList("value"));
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmRoleMapper.selectOne(...).
        final DkmRole dkmRole = new DkmRole(0, 0, "code", "roleName", "intro");
        when(mockDkmRoleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmRole);

        when(mockDkmRoleMapper.update(eq(new DkmRole(0, 0, "code", "roleName", "intro")),
                any(LambdaUpdateWrapper.class))).thenReturn(0);
        when(mockDkmRoleMenuMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmRoleMenuMapper.insert(new DkmRoleMenu(0, 0, 0))).thenReturn(0);
        when(mockDkmAdminRoleMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Arrays.asList(new DkmAdminRole(0, 0, 0)));

        // Configure DkmAdminMapper.selectById(...).
        final DkmAdmin dkmAdmin = new DkmAdmin(0, "username", "password", "extraInfo");
        when(mockDkmAdminMapper.selectById(0)).thenReturn(dkmAdmin);

        when(mockRedisTemplate.hasKey("username")).thenReturn(null);
        when(mockRedisTemplate.delete("username")).thenReturn(false);

        // Run the test
        final PageResp result = dkmRoleServiceImplUnderTest.updateRoleById(role);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        verify(mockDkmRoleMapper).update(eq(new DkmRole(0, 0, "code", "roleName", "intro")),
                any(LambdaUpdateWrapper.class));
        verify(mockDkmRoleMenuMapper).delete(any(LambdaQueryWrapper.class));
        verify(mockDkmRoleMenuMapper).insert(new DkmRoleMenu(0, 0, 0));
    }

    @Test
    void testUpdateRoleById_RedisTemplateDeleteReturnsNull() {
        // Setup
        final RoleDTO role = new RoleDTO(Arrays.asList("value"));
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmRoleMapper.selectOne(...).
        final DkmRole dkmRole = new DkmRole(0, 0, "code", "roleName", "intro");
        when(mockDkmRoleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(dkmRole);

        when(mockDkmRoleMapper.update(eq(new DkmRole(0, 0, "code", "roleName", "intro")),
                any(LambdaUpdateWrapper.class))).thenReturn(0);
        when(mockDkmRoleMenuMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(0);
        when(mockDkmRoleMenuMapper.insert(new DkmRoleMenu(0, 0, 0))).thenReturn(0);
        when(mockDkmAdminRoleMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Arrays.asList(new DkmAdminRole(0, 0, 0)));

        // Configure DkmAdminMapper.selectById(...).
        final DkmAdmin dkmAdmin = new DkmAdmin(0, "username", "password", "extraInfo");
        when(mockDkmAdminMapper.selectById(0)).thenReturn(dkmAdmin);

        when(mockRedisTemplate.hasKey("username")).thenReturn(false);
        when(mockRedisTemplate.delete("username")).thenReturn(null);

        // Run the test
        final PageResp result = dkmRoleServiceImplUnderTest.updateRoleById(role);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        verify(mockDkmRoleMapper).update(eq(new DkmRole(0, 0, "code", "roleName", "intro")),
                any(LambdaUpdateWrapper.class));
        verify(mockDkmRoleMenuMapper).delete(any(LambdaQueryWrapper.class));
        verify(mockDkmRoleMenuMapper).insert(new DkmRoleMenu(0, 0, 0));
    }

    @Test
    void testInsert() {
        // Setup
        final InsertRoleVO roleVO = new InsertRoleVO();
        roleVO.setCreator("creator");
        roleVO.setCreateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        roleVO.setId(0);
        roleVO.setCode("code");
        roleVO.setRoleName("roleName");
        roleVO.setIntro("intro");
        roleVO.setMenuList(Arrays.asList("123"));

        final PageResp expectedResult = PageResp.success("新增成功");

        // Configure DkmRoleMapper.selectOne(...).
        final DkmRole dkmRole = new DkmRole(0, 0, "code", "roleName", "intro");
        when(mockDkmRoleMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        when(mockDkmRoleMapper.insert(new DkmRole(0, 0, "code", "roleName", "intro"))).thenReturn(0);
        when(mockDkmRoleMenuMapper.insert(new DkmRoleMenu(0, 0, 0))).thenReturn(0);

        // Run the test
        final PageResp result = dkmRoleServiceImplUnderTest.insert(roleVO);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
        //verify(mockDkmRoleMapper).insert(new DkmRole(0, 0, "code", "roleName", "intro"));
        //verify(mockDkmRoleMenuMapper).insert(new DkmRoleMenu(0, 0, 0));
    }
}
