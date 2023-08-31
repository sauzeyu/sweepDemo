package com.vecentek.back.controller;

import com.vecentek.back.entity.DkmUltraWideBandCalibrationData;
import com.vecentek.back.exception.DiagnosticLogsException;
import com.vecentek.back.exception.VecentException;
import com.vecentek.back.service.impl.DkmUltraWideBandCalibrationDataServiceImpl;
import com.vecentek.common.response.PageResp;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * DkmPhoneCalibrationData表控制层
 *
 * @author EdgeYu
 * @version 1.0
 * @since 2022-04-01 15:57:49
 */

@RestController
@RequestMapping("/dkmUltraWideBandCalibrationData")
public class DkmUltraWideBandCalibrationDataController {
    /**
     * 服务对象
     */
    @Resource
    private DkmUltraWideBandCalibrationDataServiceImpl dkmUltraWideBandCalibrationDataService;

    /**
     * 通过主键查询单条数据
     *
     * @return 单条数据
     */
    @GetMapping(value = "/selectForPage")
    public PageResp selectForPage(@RequestParam(name = "pageIndex") int index, @RequestParam(name = "pageSize") int limit, String phoneBrand, String vehicleModel, String vehicleType, String vehicleBrand) {
        return this.dkmUltraWideBandCalibrationDataService.selectForPage(index, limit, phoneBrand, vehicleModel, vehicleType, vehicleBrand);
    }

    /**
     * 通过主键更新单条数据
     *
     * @return 单条数据
     */
    @PostMapping(value = "/updateById")
    public PageResp updateById(@RequestBody DkmUltraWideBandCalibrationData dkmUltraWideBandCalibrationData, BindingResult result) throws VecentException, DiagnosticLogsException {
        boolean b = result.hasErrors();
        return this.dkmUltraWideBandCalibrationDataService.updateDkmUltraWideBandCalibrationDataById(dkmUltraWideBandCalibrationData);
    }

    @PostMapping(value = "/importByExcel")
    public PageResp importByExcel(MultipartFile file) {
        return this.dkmUltraWideBandCalibrationDataService.importByExcel(file);
    }


    @PostMapping(value = "/downloadCalibrationExcel")
    public void downloadCalibrationExcel(String phoneBrand, String vehicleModel, String vehicleType, String vehicleBrand, Boolean isXlsx, HttpServletResponse response) throws UnsupportedEncodingException {

        this.dkmUltraWideBandCalibrationDataService.downloadCalibrationExcel(phoneBrand, vehicleModel, vehicleType, vehicleBrand, isXlsx, response);

    }


}
