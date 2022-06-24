package com.vecentek.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vecentek.back.entity.DkmMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-18 16:18
 */

public interface DkmMenuMapper extends BaseMapper<DkmMenu> {


    /**
     * 根据角色id查询菜单权限
     *
     * @param id 角色id
     * @return 菜单权限集合
     */
    @Select("select dm.id\n" +
            "from dkm_role dr\n" +
            "         left join dkm_role_menu drm\n" +
            "                   on drm.role_id = dr.id\n" +
            "         left join dkm_menu dm on dm.id = drm.menu_id\n" +
            "where dr.id = #{id}")
    List<String> selectMenuByRoleId(@Param("id") Integer id);
}
