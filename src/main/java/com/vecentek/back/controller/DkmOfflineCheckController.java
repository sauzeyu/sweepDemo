package com.vecentek.back.controller;

import com.vecentek.back.entity.DkmBluetooths;
import com.vecentek.back.exception.ParameterValidationException;
import com.vecentek.back.exception.UploadOverMaximumException;
import com.vecentek.back.exception.VecentException;
import com.vecentek.back.service.impl.DkmOfflineCheckServiceImpl;
import com.vecentek.back.vo.KeyLogDataVO;
import com.vecentek.back.vo.KeyLogDetailVO;
import com.vecentek.back.vo.VehicleBluetoothVO;
import com.vecentek.common.response.PageResp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 下线检测接口控制层
 *
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-01-11 11:46
 */

@RestController
@RequestMapping(value = "/api/offlineCheck")
public class DkmOfflineCheckController {

    @Resource
    private DkmOfflineCheckServiceImpl dkmOfflineCheckServiceImpl;

    @RequestMapping(value = "/insertBluetoothBatch", method = RequestMethod.POST)
    public PageResp insertBluetoothBatch(@RequestBody List<DkmBluetooths> dkmBluetooths) throws ParameterValidationException, UploadOverMaximumException {
        return dkmOfflineCheckServiceImpl.insertBluetoothBatch(dkmBluetooths);
    }

    @RequestMapping(value = "/insertOrUpdateVehicleBatch", method = RequestMethod.POST)
    public PageResp insertOrUpdateVehicleBatch(@RequestBody List<VehicleBluetoothVO> dkmVehicles) throws VecentException {
        return dkmOfflineCheckServiceImpl.insertOrUpdateVehicleBatch(dkmVehicles);
    }

    /**
     * 根据时间段和车辆VIN号等信息获取钥匙的使用记录
     * @param keyLogDetailVO
     * @return
     */
    @RequestMapping(value = "/getKeyLogDetail", method = RequestMethod.POST)
    public PageResp getKeyLogDetail(@RequestBody List<KeyLogDetailVO> keyLogDetailVO) {
        return dkmOfflineCheckServiceImpl.getKeyLogDetail(keyLogDetailVO);
    }

    /**
     * 按照时间段统计不同型号手机失败率、不同车型失败率、不同模块失败率
     * 不同手机型号的故障率,不同车型的故障率,不同故障码的故障率
     * @return
     */
    @RequestMapping(value = "/getKeyData", method = RequestMethod.POST)
    public PageResp getKeyData(@RequestBody KeyLogDataVO keyLogDataVO) {
        return dkmOfflineCheckServiceImpl.getKeyData(keyLogDataVO);
    }
}
