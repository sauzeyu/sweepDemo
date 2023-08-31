package com.vecentek.back.controller;

import com.vecentek.back.exception.DiagnosticLogsException;
import com.vecentek.back.service.impl.DkmUserVehicleServiceImpl;
import com.vecentek.back.vo.GetBluetoothVinVO;
import com.vecentek.back.vo.LogoutUserVehicleVO;
import com.vecentek.back.vo.RevokeKeyVO;
import com.vecentek.back.vo.ShareKeyVO;
import com.vecentek.back.vo.UserVehicleVO;
import com.vecentek.common.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-01-14 14:55
 */
@RestController
@RequestMapping("/api/userVehicle")
@Slf4j
public class DkmUserVehicleController {
    @Resource
    private DkmUserVehicleServiceImpl dkmUserVehicleService;

    @RequestMapping(value = "/insertUserVehicle", method = RequestMethod.POST)
    public PageResp insertUserVehicle(@RequestBody UserVehicleVO userVehicle) throws DiagnosticLogsException {
        return dkmUserVehicleService.insertUserVehicle(userVehicle);
    }

    //@RequestMapping(value = "/updateUserVehicle", method = RequestMethod.POST)
    //public PageResp updateUserVehicle(@RequestBody UserChangeVO userChange) throws ParameterValidationException {
    //    return dkmUserVehicleService.updateUserVehicle(userChange);
    //}

    /**
     * 车主注销(解除人车绑定关系)
     * 用于钥匙平台从APP后台获取车辆变更车主数据。
     *
     * @param logoutUserVehicle
     * @return
     */
    @RequestMapping(value = "/logoutUserVehicle", method = RequestMethod.POST)
    public PageResp logoutUserVehicle(@RequestBody LogoutUserVehicleVO logoutUserVehicle) throws DiagnosticLogsException {
        return dkmUserVehicleService.logoutUserVehicle(logoutUserVehicle);
    }

    /**
     * 获取车辆是否具有蓝牙钥匙功能
     * 用于APP后台从数字钥匙平台获取车辆是否具有蓝牙钥匙功能。
     *
     * @param getBluetoothVinVO
     * @return
     */
    @RequestMapping(value = "/getBluetoothVin", method = RequestMethod.POST)
    public PageResp getBluetoothVin(@RequestBody GetBluetoothVinVO getBluetoothVinVO) {
        return dkmUserVehicleService.getBluetoothVin(getBluetoothVinVO);
    }

    /**
     * 吊销钥匙
     * APP识别到用户更换手机之后，主动调用钥匙平台接口，吊销用户的所有钥匙信息（适用于车主用户和非车主用户）
     *
     * @param revokeKeyVO
     * @return
     */
    @RequestMapping(value = "/revokeKey", method = RequestMethod.POST)
    public PageResp revokeKey(@RequestBody RevokeKeyVO revokeKeyVO) throws DiagnosticLogsException {
        return dkmUserVehicleService.revokeKey(revokeKeyVO);
    }

    /**
     * 网页链接Link的形式的分享钥匙
     * 车主钥匙用户对目标用户进行分享操作时，TSP平台会通知钥匙平台创建分享钥匙。如果识别到分享用户没有注册，待分享用户注册成功之后，在调用此接口生成分享钥匙信息
     *
     * @param shareKeyVO
     * @return
     */
    @RequestMapping(value = "/shareKey", method = RequestMethod.POST)
    public PageResp shareKey(@RequestBody ShareKeyVO shareKeyVO) throws DiagnosticLogsException {
        return dkmUserVehicleService.shareKey(shareKeyVO);
    }

    /**
     * 生成AppScheme接口
     * 浏览器拉活App
     *
     * @return
     */
//    @RequestMapping(value = "/getAppScheme", method = RequestMethod.POST)
//    public String getAppScheme(@RequestBody SchemeVO schemeVO) {
// /       return dkmUserVehicleService.getAppScheme(schemeVO);
//    }

//    @RequestMapping(value = "/getAppScheme", method = RequestMethod.POST)
//    public String getAppScheme(@RequestBody ShareKeyVO shareKeyVO) {
//        return dkmUserVehicleService.getAppScheme(shareKeyVO);
//    }

    /**
     * 安卓拉活
     * @return
     */
    @RequestMapping(value = "/getAppScheme", method = RequestMethod.GET)
    public String getAppScheme() {
        return dkmUserVehicleService.getAppScheme();
    }

    /**
     * IOS拉活
     * @return
     */
    @RequestMapping(value = "/getIOSScheme", method = RequestMethod.GET)
    public String getIOSScheme() {
        return dkmUserVehicleService.getIOSScheme();
    }
}