package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmUserVehicleServiceImpl;
import com.vecentek.back.vo.GetBluetoothVinVO;
import com.vecentek.back.vo.LogoutUserVehicleVO;
import com.vecentek.back.vo.RevokeKeyVO;
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
    public PageResp insertUserVehicle(@RequestBody UserVehicleVO userVehicle) {
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
    public PageResp logoutUserVehicle(@RequestBody LogoutUserVehicleVO logoutUserVehicle) {
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
    public PageResp revokeKey(@RequestBody RevokeKeyVO revokeKeyVO) {
        return dkmUserVehicleService.revokeKey(revokeKeyVO);
    }
}
