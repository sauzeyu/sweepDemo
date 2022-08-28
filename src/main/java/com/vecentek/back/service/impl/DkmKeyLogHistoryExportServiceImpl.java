package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vecentek.back.constant.ExcelConstant;
import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.mapper.DkmKeyLogHistoryExportMapper;
import com.vecentek.back.service.DkmKeyLogHistoryExportService;
import com.vecentek.common.response.PageResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * (DkmKeyLogHistoryExport)表服务实现类
 *
 * @since 2022-06-23 13:44:33
 */
@Service("dkmKeyLogHistoryExportService")
public class DkmKeyLogHistoryExportServiceImpl extends ServiceImpl<DkmKeyLogHistoryExportMapper, DkmKeyLogHistoryExport> implements DkmKeyLogHistoryExportService {
@Resource
DkmKeyLogHistoryExportMapper dkmKeyLogHistoryExportMapper;
    public void downloadExcel(String fileName, HttpServletResponse response) {

        // 设置响应头信息
        try {
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8") + ExcelConstant.EXCEL_SUFFIX_XLSX);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel");

        String filePath = ("/excel/" + fileName + ExcelConstant.EXCEL_SUFFIX_XLSX);
        //String filePath = ("/excel/" + fileName + ExcelConstant.EXCEL_SUFFIX_XLSX);
        //ExcelWriter writer = ExcelUtil.getBigWriter(filePath);
        FileInputStream input = null;

        ServletOutputStream out = null;

        try {
            out = response.getOutputStream();
            input = new FileInputStream(filePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = input.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据具体用户和excel类型查询历史下载记录列表
     * @param creator
     * @param type
     * @return
     */
    public PageResp checkKeyUseLog(String creator, Integer type) {
        LambdaQueryWrapper<DkmKeyLogHistoryExport> dkmKeyLogHistoryExportLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dkmKeyLogHistoryExportLambdaQueryWrapper.eq(creator != null, DkmKeyLogHistoryExport::getCreator, creator)
                .eq(type != null, DkmKeyLogHistoryExport::getType, type)
                .orderByDesc(DkmKeyLogHistoryExport::getCreateTime)
        ;
        List<DkmKeyLogHistoryExport> dkmKeyLogHistoryExports = dkmKeyLogHistoryExportMapper.selectList(dkmKeyLogHistoryExportLambdaQueryWrapper);
        return PageResp.success("查询成功", (long) dkmKeyLogHistoryExports.size(), dkmKeyLogHistoryExports);
    }
}

