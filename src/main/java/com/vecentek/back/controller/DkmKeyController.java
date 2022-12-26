package com.vecentek.back.controller;

import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.mapper.DkmKeyLogHistoryExportMapper;
import com.vecentek.back.mapper.DkmKeyMapper;
import com.vecentek.back.service.impl.DkmKeyServiceImpl;
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

    @Resource
    private DkmKeyMapper dkmKeyMapper;

    @Resource
    private DkmKeyLogHistoryExportMapper dkmKeyLogHistoryExportMapper;


    @GetMapping(value = "/selectForPage")
    public PageResp selectForPage(@RequestParam(name = "pageIndex") int pageIndex,
                                  @RequestParam(name = "pageSize") int pageSize,
                                  String userId,
                                  String vin,
                                  Integer periodMin,
                                  Integer periodMax,
                                  String periodUnit,
                                  String applyStartTime,
                                  String applyEndTime,
                                  String valFromStartTime,
                                  String valFromEndTime,
                                  String valToStartTime,
                                  String valToEndTime,
                                  Integer keyType,
                                  Integer keyResource,
                                  Integer[] dkState,
                                  Integer[] keyClassification
    ) {
        return this.dkmKeyServiceImpl.selectForPage(pageIndex,
                pageSize,
                userId,
                vin,
                periodMin,
                periodMax,
                periodUnit,
                applyStartTime,
                applyEndTime,
                valFromStartTime,
                valFromEndTime,
                valToStartTime,
                valToEndTime,
                keyType,
                keyResource,
                dkState,
                keyClassification
                );
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
     *
     * @param keyId
     * @param dkState
     * @param userId
     * @return
     */
    @PostMapping(value = "/updateStateById")
    public PageResp updateStateById(@RequestParam String keyId, @RequestParam Integer dkState, @RequestParam String userId,@RequestParam String vin) {
        return this.dkmKeyServiceImpl.updateStateById(keyId, dkState, userId,vin);
    }


    public PageResp selectUserByKeyId(@RequestParam String keyId) {
        return this.dkmKeyServiceImpl.selectUserByKeyId(keyId);
    }

    /**
     * 吊销钥匙
     *
     * @param userId 用户id,vin 车辆标识符
     * @return 多条数据
     */

    @PostMapping(value = "/updateStateForRevokeById")
    public PageResp updateStateForRevokeById(@RequestParam String userId,@RequestParam String vin) {
        return this.dkmKeyServiceImpl.updateStateForRevokeById(userId,vin);
    }

    /**
     * 根据 生效时间 失效时间 状态 周期 分页查询
     *
     * @param pageIndex
     * @param pageSize
     * @param valFrom
     * @param valTo
     * @param period
     * @param dkState
     * @return
     */
    @GetMapping(value = "/selectForPageByVal")
    //TODO 查看前端是否使用
    public PageResp selectForPageByVal(@RequestParam(name = "pageIndex") int pageIndex, @RequestParam(name = "pageSize") int pageSize,
                                       @RequestParam String valFrom, @RequestParam String valTo, @RequestParam Long period, @RequestParam Long dkState) {
        return this.dkmKeyServiceImpl.selectForPageByVal(pageIndex, pageSize, valFrom, valTo, period, dkState);
    }

    /**
     * 开始导出钥匙信息excel
     *
     * @param vin
     * @param userId
     * @param keyType
     * @param applyStartTime
     * @param applyEndTime
     * @param periodMax
     * @param periodMin
     * @param periodUnit
     * @param valFromStartTime
     * @param valFromEndTime
     * @param valToStartTime
     * @param valToEndTime
     * @param dkState
     * @param creator
     * @return
     */
    @PostMapping(value = "/downloadKeyExcel")
    public PageResp startLoadKeyLogExcel(String vin,
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
                                         Integer[] dkState,
                                         Integer keyResource,
                                         String creator) {
        // 1.3形成文件名
        String excelName = "钥匙信息记录-" + System.currentTimeMillis();

        // 2向历史导出记录新增一条状态为导出中的数据
        DkmKeyLogHistoryExport build = DkmKeyLogHistoryExport.builder()
                .exportStatus(0)
                .missionName(excelName)
                .creator(creator)
                .createTime(new Date())
                .type(1)
                .build();
        dkmKeyLogHistoryExportMapper.insert(build);

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
                valToEndTime,
                dkState,
                keyResource,
                creator,
                excelName
                );
        return PageResp.success("正在导出");

    }

    /**
     * 开启其他线程异步导出钥匙信息excel
     *
     * @param vin
     * @param userId
     * @param keyType
     * @param applyStartTime
     * @param applyEndTime
     * @param periodMax
     * @param periodMin
     * @param periodUnit
     * @param valFromStartTime
     * @param valFromEndTime
     * @param valToStartTime
     * @param valToEndTime
     * @param dkStates
     * @param creator
     */
    @Async
    public void downloadKeyExcel(String vin,
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
                                 Integer keyResource,
                                 String creator,
                                 String excelName
                                 ) {
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
                valToEndTime,
                dkStates,
                keyResource,
                creator,
                excelName
                );


    }

}
