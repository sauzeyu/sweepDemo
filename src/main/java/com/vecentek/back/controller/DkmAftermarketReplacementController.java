package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmAftermarketReplacementServiceImpl;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * 售后换件 Controller
 *
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-12 10:54
 */
@RestController
@RequestMapping("/dkmAftermarketReplacement")
public class DkmAftermarketReplacementController {

    @Resource
    private DkmAftermarketReplacementServiceImpl dkmAftermarketReplacementService;


    @GetMapping(value = "/selectForPage")
    public PageResp selectForPage(@RequestParam(name = "pageIndex") int pageIndex,
                                  @RequestParam(name = "pageSize") int pageSize,
                                  String vin,
                                  String startTime,
                                  String endTime) {
        return dkmAftermarketReplacementService.selectForPage(pageIndex, pageSize, vin, startTime, endTime);
    }

    @GetMapping(value = "/selectByVin")
    public PageResp selectByVin(@RequestParam(name = "pageIndex") int pageIndex,
                                @RequestParam(name = "pageSize") int pageSize,
                                @RequestParam String vin) {
        return dkmAftermarketReplacementService.selectByVin(pageIndex, pageSize, vin);
    }


    @GetMapping(value = "/selectVehicleByVin")
    public PageResp selectVehicleByVin(@RequestParam String vin) {
        return dkmAftermarketReplacementService.selectVehicleByVin(vin);
    }

    /**
     * 下载换件信息excel
     * @param vin
     * @param startTime
     * @param endTime
     * @param isXls
     * @param creator
     * @param response
     * @throws UnsupportedEncodingException
     */
    @PostMapping(value = "/downloadAftermarketReplacement")
    public void downloadAftermarketReplacement(String vin,
                                               String startTime,
                                               String endTime,
                                               Boolean isXls,
                                               String creator,
                                               HttpServletResponse response) throws UnsupportedEncodingException {
        this.dkmAftermarketReplacementService.downloadAftermarketReplacement(vin, startTime, endTime, isXls, creator, response);
    }
}
