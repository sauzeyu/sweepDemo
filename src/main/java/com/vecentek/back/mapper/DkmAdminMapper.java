package com.vecentek.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vecentek.back.entity.DkmAdmin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-11 10:43
 */
public interface DkmAdminMapper extends BaseMapper<DkmAdmin> {

    /**
     * 通过管理员id 查询角色列表
     *
     * @param id 管理员id
     * @return 角色列表
     */
    @Select("select dr.role_name \n" +
            "from dkm_admin da\n" +
            "         left join dkm_admin_role dar on da.id = dar.admin_id\n" +
            "         left join dkm_role dr on dr.id = dar.role_id where da.id=#{id}")
    List<String> selectRoleNameListById(@Param("id") int id);


}
