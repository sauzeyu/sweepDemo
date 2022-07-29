package com.vecentek.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vecentek.back.entity.DkmUserVehicle;
import com.vecentek.back.vo.UserChangeVO;
import com.vecentek.back.vo.UserVehicleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author EdgeYu
 * @version 1.0
 * @since 2022-01-14 17:26:35
 */
// TODO 清理未使用代码
public interface DkmUserVehicleMapper extends BaseMapper<DkmUserVehicle> {

    /**
     * 查询车辆表中 vin号是否存在
     *
     * @param userVehicles 需要查询的 vin
     * @return 查询结果
     */
    List<String> selectVehicleByVin(@Param("userVehicles") List<UserVehicleVO> userVehicles);

    /**
     * 查询 vin号 是否在 用户车辆表中重复
     *
     * @param userVehicles 需要查询的 vin
     * @return 查询结果
     */
    List<String> selectUserVehicleByVin(@Param("userVehicles") List<UserVehicleVO> userVehicles);

    /**
     * 批量插入
     *
     * @param userVehicles 需要插入的集合
     * @return 成功插入条数
     */
    int insertUserVehicleBatch(@Param("userVehicles") List<UserVehicleVO> userVehicles);

    /**
     * 过户，更新用户车辆表
     *
     * @param userChange 过户信息
     * @return 更新条数
     */
    int updateUserVehicle(@Param("userChange") UserChangeVO userChange);
}
