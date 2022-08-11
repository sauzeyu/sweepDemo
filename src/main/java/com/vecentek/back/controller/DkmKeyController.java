package com.vecentek.back.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.DkmKeyLog;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                                  Integer[] dkState
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
                dkState);
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
    public PageResp updateStateById(@RequestParam String keyId, @RequestParam Integer dkState, @RequestParam String userId) {
        return this.dkmKeyServiceImpl.updateStateById(keyId, dkState, userId);
    }


    @GetMapping(value = "/selectUserByKeyId")
    public PageResp selectUserByKeyId(@RequestParam String keyId) {
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
                                         String creator) {
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
                , dkState, creator);
        return PageResp.success("正在导出");

    }

    /**
     * 开启其他线程异步导出钥匙信息excel
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
                                 String creator) {
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
                creator);
    }
    @GetMapping("/add")
    public Object add() throws ParseException {
        DkmKey dkmKey = new DkmKey();
        //分表是跟进创建时间， 创建时间必须要有值， 也可以使用mybatis的自动填充功能
        dkmKey.setId(String.valueOf(IdUtil.getSnowflakeNextId()));
        dkmKey.setVehicleId(1);
        dkmKey.setDkState(2);
        dkmKey.setPermissions(4);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse("2022-09-01 00:00:00");
        dkmKey.setApplyTime(date);

        dkmKeyMapper.insert(dkmKey);
        return dkmKey;
    }
}
