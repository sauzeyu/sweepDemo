package com.vecentek.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vecentek.back.entity.DkmPhoneCalibrationData;
import com.vecentek.back.entity.DkmVehicleCalibrationData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * DkmPhoneCalibrationData表数据库访问层
 *
 * @author tangc
 * @version 1.0
 * @since 2022-04-01 15:57:49
 */

public interface DkmVehicleCalibrationDataMapper extends BaseMapper<DkmVehicleCalibrationData> {

    /**
     * 批量插入手机标定数据
     *
     * @param list 手机标定数据列表
     * @return 插入的行数
     */
    int insertPhoneCalibrationDataBatch(@Param("list") List<DkmVehicleCalibrationData> list);
}
