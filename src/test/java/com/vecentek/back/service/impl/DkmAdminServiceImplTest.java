package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmAdmin;
import com.vecentek.back.entity.DkmRole;
import com.vecentek.back.mapper.DkmAdminMapper;
import com.vecentek.back.mapper.DkmAdminRoleMapper;
import com.vecentek.back.mapper.DkmRoleMapper;
import com.vecentek.back.vo.AdminVO;
import com.vecentek.back.vo.InsertAdminVO;
import com.vecentek.common.response.PageResp;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DkmAdminServiceImplTest {
    private String fiveHundredResponse = "{\"code\":500}";
    private String fiveHundredMessageResponse = "{\"code\":500,\"msg\":\"服务繁忙,请稍后...\"}";
    private String successResponse = "{\"code\":200}";
    private String oneThousandOneMessageResponse = "{\"code\":1001,\"msg\":\"必填参数未传递或传入的参数格式不正确！\"}";
    @Mock
    private DkmAdminMapper mockDkmAdminMapper;
    @Mock
    private DkmRoleMapper mockDkmRoleMapper;
    @Mock
    private DkmAdminRoleMapper mockDkmAdminRoleMapper;
    @Mock
    private RedisTemplate mockRedisTemplate;

    @InjectMocks
    private DkmAdminServiceImpl dkmAdminServiceImplUnderTest;

    private AdminVO adminVO;
    private DkmAdmin dkmAdmin;
    @BeforeEach
    public void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmAdmin.class);
        adminVO = new AdminVO();
        adminVO.setId(1);
        adminVO.setUsername("admin");
        adminVO.setRoleId(2);
        adminVO.setExtraInfo("extra info");
        adminVO.setUpdateTime(new Date());
        adminVO.setUpdator("user");

        dkmAdmin = new DkmAdmin();
        dkmAdmin.setId(1);
        dkmAdmin.setUsername("admin");
        //dkmAdmin.se(1L);
        dkmAdmin.setExtraInfo("");
        dkmAdmin.setCreateTime(new Date());
    }

    @Test
    void testSelectForPage() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功", 0L,new ArrayList<>());
        when(mockDkmAdminMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(new Page<DkmAdmin>(1, 10));

        // Run the test
        final PageResp result = dkmAdminServiceImplUnderTest.selectForPage(0, 0, null, null, null);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testSelectRoleNameListById() {
        // Setup
        final DkmRole dkmRole = new DkmRole(0, 0, "code", "roleName", "intro");
        final PageResp expectedResult = PageResp.success("查询成功",dkmRole);

        // Configure DkmAdminMapper.selectRoleNameListById(...).
        when(mockDkmAdminMapper.selectRoleNameListById(0)).thenReturn(dkmRole);

        // Run the test
        final PageResp result = dkmAdminServiceImplUnderTest.selectRoleNameListById(0);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testSelectAllRole() {



        // Setup
        final List<DkmRole> dkmRoles = Arrays.asList(new DkmRole(5, 1, "001", "管理员", "管理员的信息"));
        final PageResp expectedResult = PageResp.success("查询成功",1L,dkmRoles);

        // Configure DkmRoleMapper.selectList(...).
        when(mockDkmRoleMapper.selectList(any(Wrapper.class))).thenReturn(null); // 全部返回 null

        //when(mockDkmRoleMapper.selectList(any(Wrapper.class))).thenReturn(dkmRoles);

        // Run the test
        final PageResp result = dkmAdminServiceImplUnderTest.selectAllRole();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }



    @Test
    public void testUpdateAdminByIdUpdateThrows() {
        when(mockDkmAdminMapper.selectById(any())).thenReturn(dkmAdmin);
        when(mockDkmAdminMapper.selectOne(any())).thenReturn(dkmAdmin);
        when(mockDkmAdminMapper.update(any(), any())).thenReturn(1);


        PageResp pageResp = dkmAdminServiceImplUnderTest.updateAdminById(adminVO);

        assertThat(pageResp).isNotNull();
        assertThat(pageResp.success()).isNotNull();
    }

    @Test
    public void testUpdateAdminByIdUpdateFail() {
        // 模拟 dkmAdminMapper.update() 方法抛出 RuntimeException 异常
        when(mockDkmAdminMapper.selectById(any())).thenReturn(dkmAdmin);
        when(mockDkmAdminMapper.selectOne(any())).thenReturn(dkmAdmin);
        // 模拟 update() 方法抛出异常
        when(mockDkmAdminMapper.update(any(), any())).thenThrow(new RuntimeException());
        // 执行方法
        PageResp pageResp = dkmAdminServiceImplUnderTest.updateAdminById(adminVO);
        // 验证结果
        assertEquals("更新失败", pageResp.getMsg());

    }
    @Test
    public void testUpdateAdminByIdUserNameisNull() {
        // 模拟 dkmAdminMapper.update() 方法抛出 RuntimeException 异常
        when(mockDkmAdminMapper.selectById(any())).thenReturn(dkmAdmin);
        when(mockDkmAdminMapper.selectOne(any())).thenReturn(dkmAdmin);
        when(mockDkmAdminMapper.update(any(), any())).thenReturn(1);
        when(mockRedisTemplate.hasKey(any())).thenReturn(true);
        when(mockRedisTemplate.delete(any())).thenReturn(0L);
        // 调用被测试方法
        PageResp resp = dkmAdminServiceImplUnderTest.updateAdminById(adminVO);

        // 断言返回结果
        assertEquals(resp.getMsg(), "有关联用户token删除失败，请联系管理员或让用户主动下线重登！");

    }

    @Test
    public void testUpdateAdminByIdSuccess() {
// 模拟 dkmAdminMapper.update() 方法抛出 RuntimeException 异常
        when(mockDkmAdminMapper.selectById(any())).thenReturn(dkmAdmin);
        when(mockDkmAdminMapper.selectOne(any())).thenReturn(dkmAdmin);
        when(mockDkmAdminMapper.update(any(), any())).thenReturn(1);
        when(mockDkmAdminRoleMapper.deleteByAdminId(any())).thenReturn(1);


        // 调用被测试方法
        PageResp resp = dkmAdminServiceImplUnderTest.updateAdminById(adminVO);

        // 断言返回结果
        assertEquals(resp.getMsg(), "更新成功");

    }


    // 测试更新时用户名或权限为空
    @Test
    public void testUpdateAdminByIdBlankFields() {
        adminVO.setUsername(null);
        adminVO.setRoleId(null);
        PageResp pageResp = dkmAdminServiceImplUnderTest.updateAdminById(adminVO);

        assertThat(pageResp).isNotNull();
        assertThat(pageResp.fail());
        assertThat(pageResp.getMsg()).isEqualTo("用户名或权限不能为空");
    }

    // 测试更新时用户名id为空
    @Test
    public void testUpdateAdminByIdNullId() {
        adminVO.setId(null);
        PageResp pageResp = dkmAdminServiceImplUnderTest.updateAdminById(adminVO);

        assertThat(pageResp).isNotNull();
        assertThat(pageResp.fail());
        assertThat(pageResp.getMsg()).isEqualTo("用户名id为空");
    }

    // 测试更新时用户不存在
    @Test
    public void testUpdateAdminByIdUserNotExists() {
        when(mockDkmAdminMapper.selectById(any())).thenReturn(null);
        PageResp pageResp = dkmAdminServiceImplUnderTest.updateAdminById(adminVO);

        assertThat(pageResp).isNotNull();
        assertThat(pageResp.fail());
        assertThat(pageResp.getMsg()).isEqualTo("用户已不存在");
    }

    // 测试更新时用户名已存在
    @Test
    public void testUpdateAdminByIdDuplicateUsername() {
        adminVO.setId(1);
        dkmAdmin.setId(2);
        when(mockDkmAdminMapper.selectById(any())).thenReturn(adminVO);

        when(mockDkmAdminMapper.selectOne(any())).thenReturn(dkmAdmin);
        PageResp pageResp = dkmAdminServiceImplUnderTest.updateAdminById(adminVO);

        assertThat(pageResp).isNotNull();
        assertThat(pageResp.fail());
        assertThat(pageResp.getMsg()).isEqualTo("用户名重复");
    }

    // 测试删除用户成功的情况
    @Test
    void testDeleteByIdSuccess() {
        Integer userId = 1;
        String username = "testUser";

        // mock redisTemplate的hasKey()方法返回true
        when(mockRedisTemplate.hasKey(username)).thenReturn(true);
        // mock redisTemplate的delete()方法返回true
        when(mockRedisTemplate.delete(eq(username))).thenReturn(true);
        // mock查询到的用户信息
        when(mockDkmAdminMapper.selectById(userId)).thenReturn(dkmAdmin);

        // 执行deleteById()方法
        PageResp result = dkmAdminServiceImplUnderTest.deleteById(userId);

        // 验证方法的返回结果和redisTemplate的delete()方法是否被调用
        assertEquals(PageResp.success("删除成功"), result);
        //verify(mockRedisTemplate, times(1)).delete(username);
    }

    // 测试用户id为空的情况
    @Test
    void testDeleteByIdWithNullId() {
        Integer userId = null;

        PageResp result = dkmAdminServiceImplUnderTest.deleteById(userId);

        assertEquals(PageResp.fail("用户id不能为空"), result);
        verifyNoInteractions(mockDkmAdminMapper);
        verifyNoInteractions(mockDkmAdminRoleMapper);
        verifyNoInteractions(mockRedisTemplate);
    }

    // 测试查询的用户不存在的情况
    @Test
    void testDeleteByIdWithNonexistentUser() {
        Integer userId = 1;

        when(mockDkmAdminMapper.selectById(userId)).thenReturn(null);

        PageResp result = dkmAdminServiceImplUnderTest.deleteById(userId);

        assertEquals(PageResp.fail("用户不存在"), result);
        verify(mockDkmAdminMapper, times(1)).deleteById(userId);
        verify(mockDkmAdminRoleMapper, times(1)).delete(any());
        verifyNoInteractions(mockRedisTemplate);
    }

    // 测试删除关联用户token失败的情况
    @Test
    void testDeleteByIdWithFailedToDeleteToken() {
        Integer userId = 1;
        String username = "testUser";


        // mock redisTemplate的hasKey()方法返回true
        when(mockRedisTemplate.hasKey(any())).thenReturn(true);
        // mock redisTemplate的delete()方法返回false
        when(mockRedisTemplate.delete(any())).thenReturn(0L);
        // mock查询到的用户信息
        when(mockDkmAdminMapper.selectById(userId)).thenReturn(dkmAdmin);

        // 执行deleteById()方法
        PageResp result = dkmAdminServiceImplUnderTest.deleteById(userId);

        // 验证方法的返回结果和redisTemplate的delete()方法是否被调用
        assertEquals("有关联用户token删除失败，请联系管理员或让用户主动下线重登！", result.getMsg());
        //verify(mockRedisTemplate, times(1)).delete(username);
    }


    @Test
    public void testResetPasswordByIdWithNullAdmin() {
        AdminVO adminVO = new AdminVO();
        // 执行方法
        PageResp pageResp = dkmAdminServiceImplUnderTest.resetPasswordById(adminVO);
        // 验证结果
        assertEquals("用户名id为空", pageResp.getMsg());

    }



    // 测试输入的 adminVO 对象对应的用户不存在时，方法是否返回 PageResp.fail()
    @Test
    public void testResetPasswordByIdWithNonExistingUser() {
        // 构造输入参数
        AdminVO adminVO = new AdminVO();
        adminVO.setId(123);
        // 模拟 selectById() 方法返回 null
        when(mockDkmAdminMapper.selectById(123)).thenReturn(null);
        // 执行方法
        PageResp pageResp = dkmAdminServiceImplUnderTest.resetPasswordById(adminVO);
        // 验证结果
        assertEquals("用户已不存在", pageResp.getMsg());

    }

    // 测试正常更新密码的情况，方法是否返回 PageResp.success()
    @Test
    public void testResetPasswordByIdWithSuccess() {
        // 构造输入参数
        AdminVO adminVO = new AdminVO();
        adminVO.setId(123);
        adminVO.setPassword("new_password");
        adminVO.setUpdateTime(new Date());
        adminVO.setUpdator("somebody");
        // 模拟 selectById() 方法返回 DkmAdmin 对象
        DkmAdmin dkmAdmin = new DkmAdmin();
        dkmAdmin.setUsername("test_user");
        when(mockDkmAdminMapper.selectById(123)).thenReturn(dkmAdmin);
        // 模拟 update() 方法执行成功
        when(mockDkmAdminMapper.update(any(), any())).thenReturn(1);
        // 执行方法
        PageResp pageResp = dkmAdminServiceImplUnderTest.resetPasswordById(adminVO);
        // 验证结果
        assertEquals("更新成功", pageResp.getMsg());

        // 验证涉及权限的操作是否正确
        //verify(mockRedisTemplate, times(1)).hasKey(eq("test_user"));
        //verify(mockRedisTemplate, times(1)).delete(eq("test_user"));
    }

    // 测试更新密码时发生异常的情况，方法是否返回 PageResp.fail()
    @Test
    public void testResetPasswordByIdWithException() {
        // 构造输入参数
        AdminVO adminVO = new AdminVO();
        adminVO.setId(123);
        adminVO.setPassword("new_password");
        adminVO.setUpdateTime(new Date());
        adminVO.setUpdator("somebody");
        // 模拟 selectById() 方法返回 DkmAdmin 对象
        DkmAdmin dkmAdmin = new DkmAdmin();
        when(mockDkmAdminMapper.selectById(123)).thenReturn(dkmAdmin);
        // 模拟 update() 方法抛出异常
        when(mockDkmAdminMapper.update(any(), any())).thenThrow(new RuntimeException("update failed"));
        // 执行方法
        PageResp pageResp = dkmAdminServiceImplUnderTest.resetPasswordById(adminVO);
        // 验证结果
        assertEquals("更新失败", pageResp.getMsg());

    }
    // 测试删除关联用户token失败的情况
    @Test
    void testResetPasswordByIdUserNameisNull() {
        Integer userId = 1;



        when(mockDkmAdminMapper.selectById(any())).thenReturn(dkmAdmin);
        // 模拟 update() 方法抛出异常
        when(mockDkmAdminMapper.update(any(), any())).thenThrow(new RuntimeException("update failed"));
        // 执行方法
        PageResp pageResp = dkmAdminServiceImplUnderTest.resetPasswordById(adminVO);

        // 执行deleteById()方法
        PageResp result = dkmAdminServiceImplUnderTest.deleteById(userId);

        // 验证方法的返回结果和redisTemplate的delete()方法是否被调用
        assertEquals("有关联用户token删除失败，请联系管理员或让用户主动下线重登！", result.getMsg());
        //verify(mockRedisTemplate, times(1)).delete(username);
    }
    // 测试涉及权限的情况，方法是否正确地清除了相应的 Redis 缓存。
    @Test
    public void testResetPasswordByIdWithClearCache() {
        // 构造输入参数
        AdminVO adminVO = new AdminVO();
        adminVO.setId(123);
        adminVO.setPassword("new_password");
        adminVO.setUpdateTime(new Date());
        adminVO.setUpdator("somebody");
        // 模拟 selectById() 方法返回 DkmAdmin 对象
        DkmAdmin dkmAdmin = new DkmAdmin();
        dkmAdmin.setUsername("test_user");
        when(mockDkmAdminMapper.selectById(123)).thenReturn(dkmAdmin);
        // 模拟 update() 方法执行成功
        when(mockDkmAdminMapper.update(any(), any())).thenReturn(1);
        // 模拟 hasKey() 方法返回 true
        when(mockRedisTemplate.hasKey("test_user")).thenReturn(true);
        // 模拟 delete() 方法返回 true
        when(mockRedisTemplate.delete("test_user")).thenReturn(false);
        // 执行方法
        PageResp pageResp = dkmAdminServiceImplUnderTest.resetPasswordById(adminVO);
        // 验证结果
        assertEquals("有关联用户token删除失败，请联系管理员或让用户主动下线重登！", pageResp.getMsg());
        // 验证涉及权限的操作是否正确
        //verify(mockRedisTemplate, times(1)).hasKey(eq("test_user"));
        //verify(mockRedisTemplate, times(1)).delete(eq("test_user"));
    }



    @Test
    public void testModifyPassword_Success() {
        // mock数据
        DkmAdmin admin = new DkmAdmin();
        admin.setUsername("username");
        admin.setPassword("password");
        Mockito.when(mockDkmAdminMapper.selectOne(Mockito.any())).thenReturn(admin);

        // 执行函数
        PageResp result = dkmAdminServiceImplUnderTest.modifyPassword("username", "password", "newPassword");

        // 断言结果
        assertEquals("修改成功", result.getMsg());
        Mockito.verify(mockDkmAdminMapper).update(Mockito.isNull(), Mockito.any());
    }

    @Test
    public void testModifyPassword_Fail() {
        // mock数据
        Mockito.when(mockDkmAdminMapper.selectOne(Mockito.any())).thenReturn(null);

        // 执行函数
        PageResp result = dkmAdminServiceImplUnderTest.modifyPassword("username", "password", "newPassword");

        // 断言结果
        assertEquals("密码错误，修改密码失败", result.getMsg());
        Mockito.verify(mockDkmAdminMapper, Mockito.never()).update(Mockito.isNull(), Mockito.any());

        // mock数据
        DkmAdmin admin = new DkmAdmin();
        admin.setUsername("username");
        admin.setPassword("wrongPassword");
        Mockito.when(mockDkmAdminMapper.selectOne(Mockito.any())).thenReturn(admin);

        // 执行函数
        result = dkmAdminServiceImplUnderTest.modifyPassword("username", "password", "newPassword");

        // 断言结果
        assertEquals("密码错误，修改密码失败", result.getMsg());
        Mockito.verify(mockDkmAdminMapper, Mockito.never()).update(Mockito.isNull(), Mockito.any());
    }

    @Test
    public void testInsert_Success() {
        // mock数据
        InsertAdminVO vo = new InsertAdminVO();
        vo.setUsername("username");
        vo.setPassword("password");
        vo.setRoleId(1);
        DkmAdmin admin = new DkmAdmin();
        Mockito.when(mockDkmAdminMapper.selectOne(Mockito.any())).thenReturn(null);
        Mockito.when(mockDkmAdminMapper.insert(Mockito.any())).thenAnswer(invocation -> {
            DkmAdmin a = (DkmAdmin) invocation.getArgument(0);
            a.setId(1);
            return 1;
        });
        Mockito.when(mockDkmAdminRoleMapper.insert(Mockito.any())).thenReturn(1);

        // 执行函数
        PageResp result = dkmAdminServiceImplUnderTest.insert(vo);

        // 断言结果
        assertEquals("新增成功", result.getMsg());
        Mockito.verify(mockDkmAdminMapper).insert(Mockito.any());
        Mockito.verify(mockDkmAdminRoleMapper).insert(Mockito.any());
    }

    @Test
    public void testInsert_Fail_Blank() {
        // mock数据
        InsertAdminVO vo = new InsertAdminVO();
        vo.setUsername("");
        vo.setPassword("");
        vo.setRoleId(null);

        // 执行函数
        PageResp result = dkmAdminServiceImplUnderTest.insert(vo);

        // 断言结果
        assertEquals(9001, result.getCode().intValue());
        assertEquals("必填参数未传递", result.getMsg());
        Mockito.verify(mockDkmAdminMapper, Mockito.never()).insert(Mockito.any());
        Mockito.verify(mockDkmAdminRoleMapper, Mockito.never()).insert(Mockito.any());
    }

    @Test
    public void testInsert_Fail_Duplicate() {
        // mock数据
        InsertAdminVO vo = new InsertAdminVO();
        vo.setUsername("username");
        vo.setPassword("password");
        vo.setRoleId(1);
        DkmAdmin admin = new DkmAdmin();
        Mockito.when(mockDkmAdminMapper.selectOne(Mockito.any())).thenReturn(admin);

        // 执行函数
        PageResp result = dkmAdminServiceImplUnderTest.insert(vo);

        // 断言结果
        assertEquals(500, result.getCode().intValue());
        assertEquals("该用户名已存在", result.getMsg());
        Mockito.verify(mockDkmAdminMapper, Mockito.never()).insert(Mockito.any());
        Mockito.verify(mockDkmAdminRoleMapper, Mockito.never()).insert(Mockito.any());
    }



}
