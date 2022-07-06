package com.vecentek.back.controller;

import com.vecentek.back.service.impl.DkmKeyServiceImpl;
import com.vecentek.back.vo.DkmKeyVO;
import com.vecentek.back.vo.SelectKeyForPageVO;
import com.vecentek.common.response.PageResp;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 钥匙信息(DkmKey)表控制层
 *
 * @author EdgeYu
 * @version 1.0
 * @since 2021-11-30 17:30:08
 */
@Validated
@RestController
@RequestMapping("/dkmKey")
public class DkmKeyController {
    /**
     * 服务对象
     */
    @Resource
    private DkmKeyServiceImpl dkmKeyServiceImpl;



    @GetMapping(value = "/selectForPage")
    public PageResp selectForPage(@RequestParam(name = "pageIndex") int pageIndex, @RequestParam(name = "pageSize") int pageSize,
                                  String vin,
                                  String userId,
                                  Integer keyType,
                                  String applyStartTime,
                                  String applyEndTime,
                                  Integer periodMax,
                                  Integer periodMin,
                                  String periodUnit,
                                  String valFromStartTime,
                                  String valFromEndTime,
                                  String valToStartTime,
                                  String valToEndTime,
                                  Integer[] dkStates
    ) {
        return this.dkmKeyServiceImpl.selectForPage(pageIndex,
                pageSize,
                vin,
                userId,
                keyType,
                applyStartTime,
                applyEndTime,
                periodMax,
                periodMin,
                periodUnit,
                valFromStartTime,
                valFromEndTime,
                valToStartTime,
                valToEndTime
                , dkStates);
    }


    /**
     * 根据用户id对钥匙信息进行分页查询
     *
     * @param index 需要查询的页码
     * @param id    用户id
     * @param limit 分页数量
     * @return 钥匙信息列表
     */
    @GetMapping(value = "/selectForPageByUserId")
    public PageResp selectForPageByUserId(@RequestParam(name = "pageIndex") int index, @RequestParam(name = "pageSize") int limit, @RequestParam Integer id) {
        return this.dkmKeyServiceImpl.selectForPageByUserId(index, limit, id);
    }


    /**
     * 通过车辆id查询钥匙列表
     *
     * @param vehicleId 车辆id
     * @return 钥匙列表
     */
    @GetMapping(value = "/selectForPageByVehicleId")
    public PageResp selectForPageByVehicleId(@RequestParam(name = "pageIndex") int pageIndex, @RequestParam(name = "pageSize") int pageSize, @RequestParam Integer vehicleId) {
        return this.dkmKeyServiceImpl.selectForPageByVehicleId(pageIndex, pageSize, vehicleId);
    }


    /**
     * 解冻/冻结钥匙
     * @param keyId
     * @param dkState
     * @param userId
     * @return
     */
    @PostMapping(value = "/updateStateById")
    public PageResp updateStateById( @RequestParam String keyId, @RequestParam Integer dkState,@RequestParam String userId) {
        return this.dkmKeyServiceImpl.updateStateById(keyId, dkState,userId);
    }



    @GetMapping(value = "/selectUserByKeyId")
    public PageResp selectUserByKeyId( @RequestParam String keyId ) {
        return this.dkmKeyServiceImpl.selectUserByKeyId(keyId);
    }

    /**
     * 吊销钥匙
     *
     * @param id 钥匙id,钥匙 id 为16位字符串
     * @return 单条数据
     */

    @PostMapping(value = "/updateStateForRevokeById")
    public PageResp updateStateForRevokeById(@NotBlank(message = "id 不能为空") @RequestParam String id) {
        return this.dkmKeyServiceImpl.updateStateForRevokeById(id);
    }

    /**
     * 根据 生效时间 失效时间 状态 周期 分页查询
     * @param pageIndex
     * @param pageSize
     * @param valFrom
     * @param valTo
     * @param period
     * @param dkState
     * @return
     */
    @GetMapping(value = "/selectForPageByVal")
    public PageResp selectForPageByVal(@RequestParam(name = "pageIndex") int pageIndex, @RequestParam(name = "pageSize") int pageSize,
                                       @RequestParam String valFrom, @RequestParam String valTo, @RequestParam Long period,@RequestParam Long dkState) {
        return this.dkmKeyServiceImpl.selectForPageByVal(pageIndex, pageSize, valFrom, valTo, period, dkState);
    }

    /**
     * 开始导出
     * @param
     * @return
     */
    @PostMapping(value = "/downloadKeyExcel")
    public PageResp startLoadKeyLogExcel( String vin,
                                          String userId,
                                          Integer keyType,
                                          String applyStartTime,
                                          String applyEndTime,
                                          Integer periodMax,
                                          Integer periodMin,
                                          String periodUnit,
                                          String valFromStartTime,
                                          String valFromEndTime,
                                          String valToStartTime,
                                          String valToEndTime,
                                          Integer[] dkStates,
                                          String token)  {
        downloadKeyExcel(vin,
                userId,
                keyType,
                applyStartTime,
                applyEndTime,
                periodMax,
                periodMin,
                periodUnit,
                valFromStartTime,
                valFromEndTime,
                valToStartTime,
                valToEndTime
                , dkStates,token);
        return PageResp.success("正在导出");

    }

    /**
     * 异步导出

     */
    @Async
    public void downloadKeyExcel( String vin,
                                  String userId,
                                  Integer keyType,
                                  String applyStartTime,
                                  String applyEndTime,
                                  Integer periodMax,
                                  Integer periodMin,
                                  String periodUnit,
                                  String valFromStartTime,
                                  String valFromEndTime,
                                  String valToStartTime,
                                  String valToEndTime,
                                  Integer[] dkStates,
                                  String token)  {
        this.dkmKeyServiceImpl.downloadKeyLogExcel(
                vin,
                userId,
                keyType,
                applyStartTime,
                applyEndTime,
                periodMax,
                periodMin,
                periodUnit,
                valFromStartTime,
                valFromEndTime,
                valToStartTime,
                valToEndTime
                , dkStates,token);
    }

}
