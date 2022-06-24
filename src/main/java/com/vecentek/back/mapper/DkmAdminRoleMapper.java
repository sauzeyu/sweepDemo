package com.vecentek.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vecentek.back.entity.DkmAdminRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-16 15:04
 */
public interface DkmAdminRoleMapper extends BaseMapper<DkmAdminRole> {


    /**
     * 删除管理员时，删除中间表
     *
     * @param id 管理员id
     * @return 删除结果
     */
    @Delete("delete from dkm_admin_role  where admin_id =#{id}  ")
    Integer deleteByAdminId(@Param("id") Integer id);

    /**
     * 删除角色时，删除中间表
     *
     * @param id 角色id
     * @return 删除结果
     */
    @Delete("delete from dkm_admin_role  where role_id =#{id}  ")
    Integer deleteByRoleId(@Param("id") Integer id);

}
