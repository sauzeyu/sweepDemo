package com.vecentek.back.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.service.DkmKeyLogHistoryExportService;
import com.vecentek.back.service.impl.DkmAftermarketReplacementServiceImpl;
import com.vecentek.back.service.impl.DkmKeyLogHistoryExportServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * (DkmKeyLogHistoryExport)表控制层
 *
 * @author makejava
 * @since 2022-06-23 13:44:32
 */
@RestController
@RequestMapping("dkmKeyLogHistoryExport")
public class DkmKeyLogHistoryExportController extends ApiController {
    @Resource
    private DkmKeyLogHistoryExportServiceImpl dkmKeyLogHistorysExportService;

    /**
     * 服务对象
     */
    @Resource
    private DkmKeyLogHistoryExportService dkmKeyLogHistoryExportService;

    /**
     * 分页查询所有数据
     *
     * @param page                   分页对象
     * @param dkmKeyLogHistoryExport 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<DkmKeyLogHistoryExport> page, DkmKeyLogHistoryExport dkmKeyLogHistoryExport) {
        return success(this.dkmKeyLogHistoryExportService.page(page, new QueryWrapper<>(dkmKeyLogHistoryExport)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.dkmKeyLogHistoryExportService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param dkmKeyLogHistoryExport 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody DkmKeyLogHistoryExport dkmKeyLogHistoryExport) {
        return success(this.dkmKeyLogHistoryExportService.save(dkmKeyLogHistoryExport));
    }

    /**
     * 修改数据
     *
     * @param dkmKeyLogHistoryExport 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody DkmKeyLogHistoryExport dkmKeyLogHistoryExport) {
        return success(this.dkmKeyLogHistoryExportService.updateById(dkmKeyLogHistoryExport));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.dkmKeyLogHistoryExportService.removeByIds(idList));
    }

    @PostMapping(value = "/downloadExcel")
    public void downloadExcel(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        this.dkmKeyLogHistorysExportService.downloadExcel(fileName, response);
    }
}

