package com.vecentek.back.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmAftermarketReplacement;
import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.mapper.DkmAftermarketReplacementMapper;
import com.vecentek.back.mapper.DkmBluetoothsMapper;
import com.vecentek.back.mapper.DkmVehicleMapper;
import com.vecentek.common.response.PageResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-16 14:54
 */
@Service
public class DkmAftermarketReplacementServiceImpl {
    @Resource
    private DkmAftermarketReplacementMapper dkmAftermarketReplacementMapper;
    @Resource
    private DkmBluetoothsMapper dkmBluetoothsMapper;
    @Resource
    private DkmVehicleMapper dkmVehicleMapper;

    public PageResp selectForPage(int pageIndex, int pageSize, String vin, String startTime, String endTime) {
        Page<DkmAftermarketReplacement> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<DkmAftermarketReplacement> queryWrapper = Wrappers.<DkmAftermarketReplacement>lambdaQuery()
                .like(StrUtil.isNotBlank(vin), DkmAftermarketReplacement::getVin, vin)
                .ge(StrUtil.isNotBlank(startTime), DkmAftermarketReplacement::getReplacementTime, startTime)
                .le(StrUtil.isNotBlank(endTime), DkmAftermarketReplacement::getReplacementTime, endTime)
                .orderByDesc(DkmAftermarketReplacement::getReplacementTime);
        page = dkmAftermarketReplacementMapper.selectPage(page, queryWrapper);
        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }
    public PageResp selectByVin(int pageIndex, int pageSize, String vin) {
        Page<DkmAftermarketReplacement> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<DkmAftermarketReplacement> queryWrapper = Wrappers.<DkmAftermarketReplacement>lambdaQuery()
                .like(StrUtil.isNotBlank(vin), DkmAftermarketReplacement::getVin, vin)
                .orderByDesc(DkmAftermarketReplacement::getReplacementTime);
        page = dkmAftermarketReplacementMapper.selectPage(page, queryWrapper);
        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }

    public PageResp selectVehicleByVin(String vin) {
        LambdaQueryWrapper<DkmVehicle> wrapper = Wrappers.<DkmVehicle>lambdaQuery().eq(DkmVehicle::getVin, vin);
        DkmVehicle dkmVehicle = dkmVehicleMapper.selectOne(wrapper);
        return PageResp.success("查询成功", dkmVehicle);
    }
}
