package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmBluetoothsServiceImpl;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 蓝牙数据(DkmBluetooths)表控制层
 *
 * @author EdgeYu
 * @version 1.0
 * @since 2022-01-10 14:48:00
 */

@RestController
@RequestMapping("/dkmBluetooths")
public class DkmBluetoothsController {
    /**
     * 服务对象
     */
    @Resource
    private DkmBluetoothsServiceImpl dkmBluetoothsServiceImpl;

    /**
     * 蓝牙信息分页查询
     *
     * @param pageIndex    页码
     * @param pageSize     每页数量
     * @param hwDeviceSn   设备序列号
     * @param flag         设备状态
     * @param searchNumber 设备检索号
     * @return 分页结果
     */
    @GetMapping(value = "/selectForPage")
    public PageResp selectForPage(@RequestParam(name = "pageIndex") int pageIndex, @RequestParam(name = "pageSize") int pageSize, String hwDeviceSn, String searchNumber, Integer flag) {
        return this.dkmBluetoothsServiceImpl.selectForPage(pageIndex, pageSize, hwDeviceSn, searchNumber, flag);
    }

    /**
     * 通过主键删除单条数据
     *
     * @param hwDeviceSn 主键
     * @return 单条数据
     */
    @PostMapping(value = "/deleteById")
    public PageResp deleteById(@RequestParam("hwDeviceSn") String hwDeviceSn) {
        return this.dkmBluetoothsServiceImpl.deleteById(hwDeviceSn);
    }

    @PostMapping(value = "/downloadDkmBluetooths")
    public void downloadDkmBluetooths(String hwDeviceSn, String searchNumber, Integer flag, HttpServletResponse response) {
        this.dkmBluetoothsServiceImpl.downloadDkmBluetooths(hwDeviceSn, searchNumber, flag, response);
    }
}
