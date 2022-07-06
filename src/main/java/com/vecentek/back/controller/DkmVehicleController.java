package com.vecentek.back.controller;

import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.service.impl.DkmVehicleServiceImpl;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * 车辆(DkmVehicle)表控制层
 *
 * @author EdgeYu
 * @version 1.0
 * @since 2021-11-30 17:26:35
 */

@RestController
@RequestMapping("/dkmVehicle")
public class DkmVehicleController {
    /**
     * 服务对象
     */
    @Resource
    private DkmVehicleServiceImpl dkmVehicleServiceImpl;


    /**
     * 通过车辆id查询一个车辆
     *
     * @param id 主键
     * @return DkmVehicleDTO
     */
    @GetMapping(value = "/selectById")
    public PageResp selectById(Integer id) {
        return this.dkmVehicleServiceImpl.selectById(id);
    }


    @GetMapping(value = "/selectUserByVehicleId")
    public PageResp selectUserByVehicleId(@RequestParam Integer vehicleId) {
        return this.dkmVehicleServiceImpl.selectUserByVehicleId(vehicleId);
    }

    /**
     * 分页查询车辆信息
     *
     * @param pageIndex  需要查询的页码
     * @param vin        车辆vin号
     * @param hwDeviceSn 蓝牙编号
     * @param pageSize   分页数量
     * @return 车辆信息列表
     */
    @GetMapping(value = "/selectForPage")
    public PageResp selectForPage(@RequestParam(name = "pageIndex") int pageIndex,
                                  @RequestParam(name = "pageSize") int pageSize,
                                  String vin,
                                  String hwDeviceSn,
                                  String vehicleModel,
                                  String vehicleBrand) {
        return this.dkmVehicleServiceImpl.selectForPage(pageIndex, pageSize, vin, hwDeviceSn,vehicleModel,vehicleBrand);
    }

    /**
     * 通过用户id进行分页查询车辆信息
     *
     * @param index 需要查询的页码
     * @param id    用户id
     * @param limit 分页数量
     * @return 车辆信息列表
     */
    @GetMapping(value = "/selectForPageByUserId")
    public PageResp selectForPageByUserId(@RequestParam(name = "pageIndex") int index, @RequestParam(name = "pageSize") int limit, @RequestParam Integer id) {
        return this.dkmVehicleServiceImpl.selectForPageByUserId(index, limit, id);
    }

    /**
     * 通过一个pojo对象新增单条数据
     *
     * @param dkmVehicle 一个数据库对应的POJO数据对象
     * @return 返回插入的主键id
     */
    @PostMapping(value = "/insert")
    public PageResp insert(@RequestBody DkmVehicle dkmVehicle) {
        return this.dkmVehicleServiceImpl.insert(dkmVehicle);
    }

    @PostMapping(value = "/updateById")
    public PageResp updateById(@RequestBody DkmVehicle dkmVehicle) {
        return this.dkmVehicleServiceImpl.updateById(dkmVehicle);
    }

    @PostMapping(value = "/downloadDkmVehicle")
    public void downloadDkmVehicle(String vin,
                                               String hwDeviceSn,
                                               String vehicleModel,
                                               String vehicleBrand,
                                               HttpServletResponse response)  {
        this.dkmVehicleServiceImpl.downloadDkmVehicle(vin, hwDeviceSn, vehicleModel,vehicleBrand,response);
    }
}
