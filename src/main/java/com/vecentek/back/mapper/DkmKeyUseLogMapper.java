package com.vecentek.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vecentek.back.dto.CountDTO;
import com.vecentek.back.dto.MonthCountDTO;
import com.vecentek.back.entity.DkmKeyUseLog;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author EdgeYu
 * @version 1.0
 * @since 2022-05-24 16:32
 */
public interface DkmKeyUseLogMapper extends BaseMapper<DkmKeyUseLog> {

    /**
     * 查询每月的钥匙使用次数
     *
     * @return {@link List<MonthCountDTO>}
     * @author EdgeYu
     * @date 2022-06-01 14:02
     */
    @Select("select DATE_FORMAT(operate_time, '%m') as month, count(1) as count\n" +
            "from dkm_key_use_log\n" +
            "where year(operate_time) = year(now())\n" +
            "group by month;")
    List<MonthCountDTO> selectCountByMonth();

    /**
     * 按时间查询钥匙使用次数及状态码
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List< CountDTO >}
     * @author EdgeYu
     * @date 2022-06-02 15:55
     */
    @Select("select count(1) as value , status_code as name \n" +
            "from dkm_key_use_log\n" +
            "where operate_time >= #{startTime} and operate_time <= #{endTime}\n" +
            "group by status_code;")
    List<CountDTO> selectUseCountByTime(String startTime, String endTime);
}
