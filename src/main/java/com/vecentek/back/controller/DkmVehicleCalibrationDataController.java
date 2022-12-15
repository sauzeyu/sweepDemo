package com.vecentek.back.controller;/**
 * @author liujz
 * @date 2022/10/21
 * @apiNote
 */

import com.vecentek.back.entity.DkmPhoneCalibrationData;
import com.vecentek.back.entity.DkmVehicleCalibrationData;
import com.vecentek.back.exception.VecentException;
import com.vecentek.back.service.impl.DkmPhoneCalibrationDataServiceImpl;
import com.vecentek.back.service.impl.DkmVehicleCalibrationDataServiceImpl;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @author liujz
 * @version 1.0
 * @description: TODO
 * @date 2022/10/21 14:18
 */
@RestController
@RequestMapping("/dkmVehicleCalibrationData")
public class DkmVehicleCalibrationDataController {

    @Resource
    private DkmPhoneCalibrationDataServiceImpl dkmPhoneCalibrationDataServiceImpl;

    @Resource
    private DkmVehicleCalibrationDataServiceImpl dkmVehicleCalibrationDataServiceImpl;
    /**
     * @description: TODO
     * @author liujz
     * @date 2022/10/21 14:19
     * @version 1.0
     */
    @GetMapping(value = "/selectForPage")
    public PageResp selectForPage(@RequestParam(name = "pageIndex") int index, @RequestParam(name = "pageSize") int limit, String vehicleModel,Integer level) {
        return this.dkmVehicleCalibrationDataServiceImpl.selectForPage(index, limit, vehicleModel,level);
    }

    /**
     * @description: TODO
     * @author liujz
     * @date 2022/10/21 14:19
     * @version 1.0
     */
    @PostMapping(value = "/updateById")
    public PageResp updateById(@RequestBody DkmVehicleCalibrationData dkmVehicleCalibrationData) throws VecentException {
        return this.dkmVehicleCalibrationDataServiceImpl.updateDkmVehicleCalibrationDataById(dkmVehicleCalibrationData);
    }

    /**
     * @description: TODO
     * @author liujz
     * @date 2022/10/21 14:19
     * @version 1.0
     */
    @PostMapping(value = "/importByExcel")
    public PageResp importByExcel(MultipartFile file) {
        return this.dkmVehicleCalibrationDataServiceImpl.importByExcel(file);
    }


    /**
     * @description: TODO
     * @author liujz
     * @date 2022/10/21 14:19
     * @version 1.0
     */
    @PostMapping(value = "/downloadCalibrationExcel")
    public void downloadCalibrationExcel(String vehicleModel, Integer level, Boolean isXlsx, HttpServletResponse response) throws UnsupportedEncodingException {
        this.dkmVehicleCalibrationDataServiceImpl.downloadCalibrationExcel(vehicleModel, level, isXlsx, response);
    }
}
