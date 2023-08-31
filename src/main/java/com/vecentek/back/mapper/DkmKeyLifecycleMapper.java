package com.vecentek.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vecentek.back.dto.CountDTO;
import com.vecentek.back.entity.DkmKeyLifecycle;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-19 14:39
 */

public interface DkmKeyLifecycleMapper extends BaseMapper<DkmKeyLifecycle> {

    /**
     * 按照
     *
     * @return {@link List<CountDTO>}
     * @author EdgeYu
     * @date 2022-06-08 14:36
     */
    @Select("select count(1) as value , key_status as name\n" +
            "from dkm_key_lifecycle\n" +
            "group by key_status;")
    List<CountDTO> selectCountByStatus();
}
