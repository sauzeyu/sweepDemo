package com.vecentek.back.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmBluetooths;
import com.vecentek.back.mapper.DkmBluetoothsMapper;
import com.vecentek.common.response.PageResp;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

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
     * @param pageIndex    页码
     * @param pageSize     每页数量
     * @param hwDeviceSn   设备序列号
     * @param flag         设备状态
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

    /**
     * 下载蓝牙信息excel
     *
     * @param hwDeviceSn
     * @param searchNumber
     * @param flag
     * @param response
     */
    @Transactional(rollbackFor = Exception.class)
    public void downloadDkmBluetooths(String hwDeviceSn, String searchNumber, Integer flag, HttpServletResponse response) {

        LambdaQueryWrapper<DkmBluetooths> queryWrapper = Wrappers.<DkmBluetooths>lambdaQuery()
                .eq(ObjectUtil.isNotNull(flag), DkmBluetooths::getFlag, flag)
                .like(StrUtil.isNotBlank(searchNumber), DkmBluetooths::getSearchNumber, searchNumber)
                .like(StrUtil.isNotBlank(hwDeviceSn), DkmBluetooths::getHwDeviceSn, hwDeviceSn)
                .orderByDesc(DkmBluetooths::getCreateTime);

        List<DkmBluetooths> dkmBluetooths = dkmBluetoothsMapper.selectList(queryWrapper);
        dkmBluetooths.forEach(bluetooths -> {
            if (bluetooths.getFlag() != null) {
                if (1 == bluetooths.getFlag()) {
                    bluetooths.setDkSdkVersion("正常");
                } else {
                    bluetooths.setDkSdkVersion("报废");
                }
            }
        });
        // 设置响应头信息
        try {
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + URLEncoder.encode("蓝牙信息", "UTF-8") + ".xlsx");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel");


        boolean isXlsx = true;
        ExcelWriter writer = ExcelUtil.getWriter(isXlsx);


        writer.setOnlyAlias(true);

        Font headFont = writer.createFont();
        Font cellFont = writer.createFont();

        cellFont.setFontName("宋体");
        headFont.setFontName("宋体");
        headFont.setBold(true);

        CellStyle headCellStyle = writer.getHeadCellStyle();
        headCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headCellStyle.setFont(headFont);
        headCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());

        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setFont(cellFont);

        writer.setColumnWidth(0, 40);
        writer.setColumnWidth(1, 40);
        writer.setColumnWidth(2, 20);
        writer.setColumnWidth(3, 20);
        writer.setColumnWidth(4, 40);
        writer.setColumnWidth(5, 20);


        writer.addHeaderAlias("hwDeviceSn", "设备序列号");
        writer.addHeaderAlias("searchNumber", "设备检索号");
        writer.addHeaderAlias("hwDeviceProviderNo", "设备供应商编号");
        writer.addHeaderAlias("bleMacAddress", "蓝牙MAC地址");
        writer.addHeaderAlias("dkSecUnitId", "安全芯片SEID");
        writer.addHeaderAlias("dkSdkVersion", "设备状态");


        writer.write(dkmBluetooths, true);

        try {
            writer.flush(response.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
}
