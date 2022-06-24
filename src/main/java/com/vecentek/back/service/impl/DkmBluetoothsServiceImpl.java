package com.vecentek.back.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmBluetooths;
import com.vecentek.back.mapper.DkmBluetoothsMapper;
import com.vecentek.common.response.PageResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 蓝牙数据(DkmBluetooths)表服务实现类
 *
 * @author EdgeYu
 * @version 1.0
 * @since 2022-01-10 14:48:00
 */

@Service("dkmBluetoothsService")
public class DkmBluetoothsServiceImpl {
    @Resource
    private DkmBluetoothsMapper dkmBluetoothsMapper;

    /**
     * 蓝牙信息分页查询
     *
     * @param pageIndex  页码
     * @param pageSize   每页数量
     * @param hwDeviceSn 设备序列号
     * @param flag 设备状态
     * @param searchNumber 设备检索号
     * @return 分页结果
     */
    public PageResp selectForPage(int pageIndex, int pageSize, String hwDeviceSn, String searchNumber, Integer flag) {
        Page<DkmBluetooths> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<DkmBluetooths> queryWrapper = Wrappers.<DkmBluetooths>lambdaQuery()
                .eq(flag != null, DkmBluetooths::getFlag, flag)
                .like(StrUtil.isNotBlank(searchNumber), DkmBluetooths::getSearchNumber, searchNumber)
                .like(StrUtil.isNotBlank(hwDeviceSn), DkmBluetooths::getHwDeviceSn, hwDeviceSn)
                .orderByDesc(DkmBluetooths::getCreateTime);
        Page<DkmBluetooths> dkmBluetoothsPage = dkmBluetoothsMapper.selectPage(page, queryWrapper);
        return PageResp.success("查询成功", page.getTotal(), dkmBluetoothsPage.getRecords());
    }


    /**
     * 通过主键删除数据
     *
     * @param hwDeviceSn 主键
     * @return 是否成功
     */
    public PageResp deleteById(String hwDeviceSn) {
        this.dkmBluetoothsMapper.delete(Wrappers.<DkmBluetooths>lambdaQuery().eq(DkmBluetooths::getHwDeviceSn, hwDeviceSn));
        return PageResp.success("删除成功");
    }
}
