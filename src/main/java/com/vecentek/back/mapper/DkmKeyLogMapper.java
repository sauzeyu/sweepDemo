package com.vecentek.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vecentek.back.dto.CountDTO;
import com.vecentek.back.dto.MonthCountDTO;
import com.vecentek.back.entity.DkmKeyLog;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-06-10 18:21
 */
public interface DkmKeyLogMapper extends BaseMapper<DkmKeyLog> {


    /**
     * 查询每月的钥匙使用次数
     *
     * @return {@link List<MonthCountDTO>}
     * @author EdgeYu
     * @date 2022-06-01 14:02
     */
    @Select("select DATE_FORMAT(operate_time, '%Y-%m') as month, count(1) as count from dkm_key_log  \n" +
            "where  flag = 1  and operate_time > #{startTime} and\n" +
            "operate_time <  #{endTime}\n" +
            "group by month")
    List<MonthCountDTO> selectUseLogCountByMonth(String startTime, String endTime);

    /**
     * 查询每月的钥匙故障次数
     *
     * @return {@link List<MonthCountDTO>}
     * @author EdgeYu
     * @date 2022-06-01 14:02
     */
    @Select("select DATE_FORMAT(operate_time, '%Y-%m') as month, count(1) as count from dkm_key_log  \n" +
            "where flag = 0 and operate_time > #{startTime} and\n" +
            "operate_time <  #{endTime}\n" +
            "group by month")
    List<MonthCountDTO> selectErrorLogCountByMonth(String startTime, String endTime);

    /**
     * 按时间查询钥匙使用次数及状态码
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<CountDTO>}
     * @author EdgeYu
     * @date 2022-06-02 15:55
     */
    @Select("select count(1) as value , status_code as name \n" +
            "from dkm_key_log\n" +
            "where flag=1 and operate_time >= #{startTime} and operate_time <= #{endTime}\n" +
            "group by status_code")
    List<CountDTO> selectUseCountByTime(String startTime, String endTime);

    /**
     * 按时间查询钥匙故障次数及状态码
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List<CountDTO>}
     * @author EdgeYu
     * @date 2022-06-02 15:55
     */
    @Select("select count(1) as value , status_code as name \n" +
            "from dkm_key_log\n" +
            "where flag=0 and operate_time >= #{startTime} and operate_time <= #{endTime} and flag=0\n" +
            "group by status_code")
    List<CountDTO> selectErrorCountByTime(String startTime, String endTime);

    @Select("SELECT error_reason as name,COUNT( 1 ) as value FROM dkm_key_log WHERE (flag = 0 AND phone_brand = #{phoneBrand})  and operate_time >= #{startTime}  and operate_time <= #{endTime} GROUP BY error_reason")
    List<CountDTO> selectKeyErrorLogByPhoneBrand(String phoneBrand, Date startTime, Date endTime);

    @Select("SELECT error_reason as name,COUNT( 1 ) as value FROM dkm_key_log WHERE flag = 0  and operate_time >= #{startTime}  and operate_time <= #{endTime}  GROUP BY error_reason")
    List<CountDTO> selectKeyErrorLog(Date startTime, Date endTime);

    @Select("select count(1) as count from dkm_key_log where\n" +
            " operate_time >= #{lastDay}\n " +
            "and operate_time <= #{now}\n" +
            "and flag = 1")
    int countUseToday(String now, String lastDay);

    @Select("select count(1) as count from dkm_key_log where  operate_time >= #{lastDay} and operate_time <= #{now} and flag = 0")
    int countErrorToday(String now, String lastDay);

    @Select("select count(1) as value , phone_brand as name \n" +
            "from dkm_key_log\n" +
            "where flag = 0 " +
            "and operate_time >= #{startTime}\n " +
            "and operate_time <= #{endTime}\n" +
            "group by phone_brand")
    List<CountDTO> selectPhoneErrorCountByTime(Date startTime, Date endTime);

    @Select("select count(1) as value , status_code as name \n" +
            "from dkm_key_log\n" +
            "where flag = 0 " +
            "and operate_time >= #{startTime}\n " +
            "and operate_time <= #{endTime}\n" +
            "group by status_code")
    List<CountDTO> selectStatusErrorCountByTime(Date startTime, Date endTime);

    @Select("SELECT count(1) as value ,v.vehicle_model as name \n" +
            "FROM dkm_vehicle v LEFT JOIN dkm_key_log k \n" +
            "ON v.vin = k.vin\n" +
            "WHERE flag = 0\n" +
            "and k.operate_time >= #{startTime}\n" +
            "and k.operate_time <= #{endTime}\n" +
            "group by v.vehicle_model")
    List<CountDTO> selectVehicleErrorCountByTime(Date startTime, Date endTime);

    @Select("SELECT phone_brand as name,COUNT( 1 ) as value FROM dkm_key_log WHERE flag = 0   and operate_time >= #{startTime}  and operate_time <= #{endTime} GROUP BY phone_brand")
    List<CountDTO> selectKeyErrorLogByAllPhoneBrand(Date startTime, Date endTime);
}

