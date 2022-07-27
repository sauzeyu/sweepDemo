package com.vecentek.back.controller;

import com.vecentek.back.entity.DkmPhoneCalibrationData;
import com.vecentek.back.exception.VecentException;
import com.vecentek.back.service.impl.DkmPhoneCalibrationDataServiceImpl;
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
 * DkmPhoneCalibrationData表控制层
 *
 * @author EdgeYu
 * @version 1.0
 * @since 2022-04-01 15:57:49
 */

@RestController
@RequestMapping("/dkmPhoneCalibrationData")
public class DkmPhoneCalibrationDataController {
    /**
     * 服务对象
     */
    @Resource
    private DkmPhoneCalibrationDataServiceImpl dkmPhoneCalibrationDataServiceImpl;

    /**
     * 通过主键查询单条数据
     *
     * @return 单条数据
     */
    @GetMapping(value = "/selectForPage")
    public PageResp selectForPage(@RequestParam(name = "pageIndex") int index, @RequestParam(name = "pageSize") int limit, String phoneBrand, String vehicleModel, String vehicleType, String vehicleBrand) {
        return this.dkmPhoneCalibrationDataServiceImpl.selectForPage(index, limit, phoneBrand, vehicleModel,vehicleType,vehicleBrand);
    }

    /**
     * 通过主键更新单条数据
     *
     * @return 单条数据
     */
    @PostMapping(value = "/updateById")
    public PageResp updateById(@RequestBody DkmPhoneCalibrationData dkmPhoneCalibrationData) throws VecentException {
        return this.dkmPhoneCalibrationDataServiceImpl.updateDkmPhoneCalibrationDataById(dkmPhoneCalibrationData);
    }

    @PostMapping(value = "/importByExcel")
    public PageResp importByExcel(MultipartFile file)  {
        return this.dkmPhoneCalibrationDataServiceImpl.importByExcel(file);
    }

    // @PostMapping(value = "/downloadTemplate")
    // public PageResp downloadTemplate(MultipartFile file) throws VecentException, IOException {
    //     return this.dkmPhoneCalibrationDataServiceImpl.importByExcel(file);
    // }

    @PostMapping(value = "/downloadCalibrationExcel")
    public void downloadCalibrationExcel(String phoneBrand, String vehicleModel,String vehicleType, String vehicleBrand, Boolean isXlsx, HttpServletResponse response) throws UnsupportedEncodingException {
        this.dkmPhoneCalibrationDataServiceImpl.downloadCalibrationExcel(phoneBrand, vehicleModel,vehicleType, vehicleBrand,isXlsx, response);
    }


}
