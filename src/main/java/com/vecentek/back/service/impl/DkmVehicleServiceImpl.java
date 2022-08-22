package com.vecentek.back.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.dto.DkmVehicleDTO;
import com.vecentek.back.entity.DkmUser;
import com.vecentek.back.entity.DkmUserVehicle;
import com.vecentek.back.entity.DkmVehicle;
import com.vecentek.back.mapper.DkmUserMapper;
import com.vecentek.back.mapper.DkmUserVehicleMapper;
import com.vecentek.back.mapper.DkmVehicleMapper;
import com.vecentek.common.response.PageResp;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 车辆(DkmVehicle)表服务实现类
 *
 * @author EdgeYu
 * @version 1.0
 * @since 2021-11-30 17:26:35
 */

@Service("dkmVehicleService")
public class DkmVehicleServiceImpl {
    @Resource
    private DkmVehicleMapper dkmVehicleMapper;
    @Resource
    private DkmUserVehicleMapper dkmUserVehicleMapper;
    @Resource
    private DkmUserMapper dkmUserMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    public PageResp selectById(Integer id) {
        DkmVehicle vehicle = this.dkmVehicleMapper.selectById(id);
        return PageResp.success("查询成功", vehicle);
    }

    /**
     * 多条件分页查询车辆信息
     *
     * @param pageIndex    起始页
     * @param pageSize     分页大小
     * @param vin          车辆vin码
     * @param hwDeviceSn   蓝牙设备序列号
     * @param vehicleModel 车辆型号
     * @param vehicleBrand 车辆品牌
     * @return {@link PageResp}
     * @author EdgeYu
     * @date 2022-06-10 14:09
     */
    public PageResp selectForPage(int pageIndex, int pageSize, String vin, String hwDeviceSn, String vehicleModel, String vehicleBrand, String vehicleType) {
        Page<DkmVehicle> page = new Page<>(pageIndex, pageSize);
        page = dkmVehicleMapper.selectPage(page, Wrappers.<DkmVehicle>lambdaQuery()
                .like(StrUtil.isNotBlank(vin), DkmVehicle::getVin, vin)
                .like(StrUtil.isNotBlank(hwDeviceSn), DkmVehicle::getHwDeviceSn, hwDeviceSn)
                .like(StrUtil.isNotBlank(vehicleModel), DkmVehicle::getVehicleModel, vehicleModel)
                .like(StrUtil.isNotBlank(vehicleBrand), DkmVehicle::getVehicleBrand, vehicleBrand)
                .like(StrUtil.isNotBlank(vehicleType), DkmVehicle::getVehicleType, vehicleType)
                .orderByDesc(DkmVehicle::getCreateTime));

        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }

    /**
     * 通过用户id进行分页查询车辆信息
     *
     * @param pageIndex 需要查询的页码
     * @param pageSize  分页数量
     * @param userId    用户id
     * @return 车辆信息列表
     */
    public PageResp selectForPageByUserId(int pageIndex, int pageSize, Integer userId) {
        long total = this.dkmVehicleMapper.selectForCountByUserId(userId);
        pageIndex = (pageIndex - 1) * pageSize;

        List<DkmVehicleDTO> vehicleList = this.dkmVehicleMapper.selectForPageByUserId(pageIndex, pageSize, userId);
        return PageResp.success("查询成功", total, vehicleList);
    }

    /**
     * 新增数据
     *
     * @param dkmVehicle 实例对象
     * @return 实例对象
     */
    public PageResp insert(DkmVehicle dkmVehicle) {
        dkmVehicle.setCreateTime(new Date());
        int insert = this.dkmVehicleMapper.insert(dkmVehicle);
        if (insert == 1) {
            return PageResp.success("新增成功");
        }
        return PageResp.fail("新增失败");
    }


    public PageResp updateById(DkmVehicle dkmVehicle) {
        dkmVehicle.setUpdateTime(new Date());
        int update = this.dkmVehicleMapper.updateById(dkmVehicle);
        if (update == 1) {
            return PageResp.success("更新成功");
        }
        return PageResp.fail("更新失败");
    }


    public PageResp selectUserByVehicleId(Integer vehicleId) {
        LambdaQueryWrapper<DkmUserVehicle> wrapper = Wrappers.<DkmUserVehicle>lambdaQuery().eq(DkmUserVehicle::getVehicleId, vehicleId);
        DkmUserVehicle dkmUserVehicle = dkmUserVehicleMapper.selectOne(wrapper);
        if (dkmUserVehicle != null) {
            DkmUser dkmUser = dkmUserMapper.selectById(dkmUserVehicle.getUserId());
            return PageResp.success("查询成功", dkmUser);
        }
        return PageResp.fail("查询失败");
    }

    /**
     * 下载车辆信息excel
     *
     * @param vin
     * @param hwDeviceSn
     * @param vehicleModel
     * @param vehicleBrand
     * @param vehicleType
     * @param response
     */
    public void downloadDkmVehicle(String vin, String hwDeviceSn, String vehicleModel, String vehicleBrand, String vehicleType, HttpServletResponse response) {

        LambdaQueryWrapper<DkmVehicle> queryWrapper = Wrappers.<DkmVehicle>lambdaQuery()
                .like(StrUtil.isNotBlank(vin), DkmVehicle::getVin, vin)
                .like(StrUtil.isNotBlank(hwDeviceSn), DkmVehicle::getHwDeviceSn, hwDeviceSn)
                .like(StrUtil.isNotBlank(vehicleModel), DkmVehicle::getVehicleModel, vehicleModel)
                .like(StrUtil.isNotBlank(vehicleBrand), DkmVehicle::getVehicleBrand, vehicleBrand)
                .like(StrUtil.isNotBlank(vehicleType), DkmVehicle::getVehicleType, vehicleType)
                .orderByDesc(DkmVehicle::getCreateTime);
        List<DkmVehicle> dkmVehicles = dkmVehicleMapper.selectList(queryWrapper);
        // 设置响应头信息
        try {
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + URLEncoder.encode("车辆信息", "UTF-8") + ".xlsx");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel");


        Boolean isXlsx = true;
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

        writer.setColumnWidth(0, 20);
        writer.setColumnWidth(1, 20);
        writer.setColumnWidth(2, 20);
        writer.setColumnWidth(3, 20);
        writer.setColumnWidth(4, 70);
        writer.setColumnWidth(5, 50);
        writer.setColumnWidth(6, 20);
        writer.setColumnWidth(7, 40);

        writer.addHeaderAlias("vin", "车辆vin号");
        writer.addHeaderAlias("vehicleBrand", "车辆品牌");
        writer.addHeaderAlias("vehicleModel", "车辆型号");
        writer.addHeaderAlias("vehicleType", "车型");
        writer.addHeaderAlias("hwDeviceSn", "蓝牙设备序列号");
        writer.addHeaderAlias("searchNumber", "蓝牙检索号");
        writer.addHeaderAlias("hwDeviceProviderNo", "蓝牙供应商编号");
        writer.addHeaderAlias("bleMacAddress", "蓝牙Mac地址");


        writer.write(dkmVehicles, true);

        try {
            writer.flush(response.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

}
