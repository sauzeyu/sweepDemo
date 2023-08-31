package com.vecentek.back.mapper;

import com.vecentek.back.entity.DkmBluetooths;
import com.vecentek.back.vo.VehicleBluetoothVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-01-11 17:16
 */


public interface DkmOfflineCheckMapper {

    /**
     * 批量插入蓝牙设备信息
     *
     * @param dkmBluetooths 蓝牙设备信息
     * @return 插入个数
     */

    int insertBluetoothBatch(@Param("dkmBluetooths") List<DkmBluetooths> dkmBluetooths);
    //
    // /**
    //  * 批量插入车辆信息
    //  *
    //  * @param dkmVehicles 车辆信息集合
    //  * @return 插入个数
    //  */
    // int insertVehicleBatch(@Param("dkmVehicles") List<UploadVehicleVO> dkmVehicles);
    //
    // /**
    //  * 批量更新车辆信息
    //  *
    //  * @param dkmVehicles 车辆信息集合
    //  * @return 更新个数
    //  */
    // int updateVehicleBatch(@Param("dkmVehicles") List<UploadVehicleVO> dkmVehicles);

    /**
     * 查询 vin 号是否存在
     *
     * @param vinList 需要更新/插入的车辆
     * @return 车辆 vin 号集合
     */
    List<String> selectVehicleByVin(@Param("vinList") List<VehicleBluetoothVO> vinList);

    /**
     * 批量查询 蓝牙设备序列号是否存在
     *
     * @param insertDeviceSnList 插入的蓝牙设备序列化列表
     * @return 已经存在的蓝牙设备序列号
     */
    List<String> selectBluetoothBatchByHwDeviceSn(@Param("deviceSnList") List<String> insertDeviceSnList);

    /**
     * 批量查询 蓝牙 Mac 地址是否存在
     *
     * @param insertMacList 插入的蓝牙 Mac 地址列表
     * @return 已经存在的蓝牙 Mac 地址
     */
    List<String> selectBluetoothBatchByBleMacAddress(@Param("macList") List<String> insertMacList);
}
