package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.mapper.DkmKeyLogHistoryExportMapper;
import com.vecentek.common.response.PageResp;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liujz
 * @version 1.0
 * @since 2023/4/13 17:23
 */
@SpringBootTest
public class DkmKeyLogHistoryExportServiceImplTest {
    @MockBean
    private DkmKeyLogHistoryExportMapper dkmKeyLogHistoryExportMapper;

    @Autowired
    private DkmKeyLogHistoryExportServiceImpl dkmKeyLogHistoryExportService;

    @Test
    public void testCheckKeyUseLog() {
        // 创建假数据
        List<DkmKeyLogHistoryExport> mockList = new ArrayList<>();
        DkmKeyLogHistoryExport export = new DkmKeyLogHistoryExport();
        export.setId(1L);
        export.setCreator("test");
        mockList.add(export);
        // 设置模拟行为
        LambdaQueryWrapper<DkmKeyLogHistoryExport> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DkmKeyLogHistoryExport::getCreator, "test").eq(DkmKeyLogHistoryExport::getType, 1).orderByDesc(DkmKeyLogHistoryExport::getCreateTime);
        Mockito.when(dkmKeyLogHistoryExportMapper.selectList(queryWrapper)).thenReturn(mockList);
        // 调用接口
        PageResp pageResp = dkmKeyLogHistoryExportService.checkKeyUseLog("test", 1);
        // 断言
        Assert.assertEquals("查询成功", pageResp.getMsg());
        Assert.assertEquals(1L, pageResp.getTotal());
        Assert.assertEquals(1, pageResp.getData().size());
        Assert.assertEquals("test", ((DkmKeyLogHistoryExport)pageResp.getData().get(0)).getCreator());
    }

    @Test
    public void testDownloadExcel() {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        // 调用接口
        dkmKeyLogHistoryExportService.downloadExcel("test", response);
        // 断言
        Mockito.verify(response, Mockito.times(1)).setHeader(Mockito.eq("Content-Disposition"), Mockito.anyString());
        Mockito.verify(response, Mockito.times(1)).setCharacterEncoding(Mockito.eq("utf-8"));
        Mockito.verify(response, Mockito.times(1)).setContentType(Mockito.eq("application/vnd.ms-excel"));
    }
}
