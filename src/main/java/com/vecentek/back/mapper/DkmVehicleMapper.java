package com.vecentek.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vecentek.back.dto.DkmVehicleDTO;
import com.vecentek.back.dto.MonthCountDTO;
import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.util.DownLoadUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 车辆(DkmVehicle)表数据库访问层
 *
 * @author EdgeYu
 * @version 1.0
 * @since 2021-11-30 17:26:35
 */
public interface DkmVehicleMapper extends BaseMapper<DkmVehicle> {


    /**
     * 根据用户id查询用户的车辆个数
     *
     * @param userId 用户id
     * @return 车辆数目
     */
    @Select("select count(v.id)\n" +
            "from dkm_vehicle v\n" +
            "         left join dkm_user_vehicle duv on v.id = duv.vehicle_id\n" +
            "where duv.user_id = #{userId}")
    long selectForCountByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户id查询车辆列表分页查询
     *
     * @param index  查询起始位置
     * @param limit  分页数量
     * @param userId 用户id
     * @return 对象列表
     */
    @Select("select v.id,\n" +
            "       v.factory_no,\n" +
            "       v.vin,\n" +
            "       v.vehicle_model,\n" +
            "       v.vehicle_brand,\n" +
            "       v.hw_device_sn,\n" +
            "       v.search_number,\n" +
            "       v.hw_device_provider_no,\n" +
            "       v.ble_mac_address,\n" +
            "       v.pub_key,\n" +
            "       v.dk_sec_unit_id,\n" +
            "       v.create_time,\n" +
            "       v.update_time,\n" +
            "       v.updator,\n" +
            "       v.creator,\n" +
            "       duv.vehicle_type,\n" +
            "       duv.license\n" +
            "from dkm_vehicle v\n" +
            "         left join dkm_user_vehicle duv on v.id = duv.vehicle_id\n" +
            "where duv.user_id" +
            " = #{userId}\n" +
            "order by v.create_time desc  \n" +
            "limit #{index}, #{limit}")
    List<DkmVehicleDTO> selectForPageByUserId(@Param("index") int index, @Param("limit") int limit, @Param("userId") Integer userId);

    @Select("select DATE_FORMAT(create_time, '%m') as month, count(1) as count from dkm_vehicle where year(create_time) = year(now()) group by month")
    List<MonthCountDTO> selectCountByMonth();

    @Select("select count(1) as count from dkm_vehicle where day(create_time) = day(now())")
    Integer selectNewToday();

    //@Select("select DATE_FORMAT(operate_time, '%m') as month, count(1) as count from dkm_key_log where flag = 1 and year(operate_time) = year(now()) group by month")
    @Select("select DATE_FORMAT(operate_time, '%m') as month, count(1) as count from dkm_key_log where flag = 1 and operate_time >=  #{yearFirstDay} and operate_time <=  #{yearLastDay} group by month")
    List<MonthCountDTO> countUseByMonth(String yearFirstDay,String yearLastDay);

    @Select("select DATE_FORMAT(operate_time, '%m') as month, count(1) as count from dkm_key_log where flag = 0 and operate_time >=  #{yearFirstDay} and operate_time <=  #{yearLastDay} group by month")
    List<MonthCountDTO> countErrorByMonth(String yearFirstDay,String yearLastDay);

}
