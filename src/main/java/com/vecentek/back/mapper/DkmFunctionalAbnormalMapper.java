package com.vecentek.back.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-11 10:43
 */
public interface DkmFunctionalAbnormalMapper {

    /**
     * 查询所有业务id和业务
     *
     * @return 业务id和业务
     */
    @Select("select distinct business_id businessId, business from dkm_functional_abnormal ")
    List<Map> selectBusiness();

    /**
     * 查询所有业务id和业务
     *
     * @return 业务id和业务
     */
    @Select("SELECT * FROM `dkm_functional_abnormal` where terminal_id = '03'")
    List<Map> selectAll();
}
