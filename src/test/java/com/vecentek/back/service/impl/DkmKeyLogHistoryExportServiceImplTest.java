package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.mapper.DkmKeyLogHistoryExportMapper;
import com.vecentek.common.response.PageResp;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DkmKeyLogHistoryExportServiceImplTest {

    @Mock
    private DkmKeyLogHistoryExportMapper mockDkmKeyLogHistoryExportMapper;

    @InjectMocks
    private DkmKeyLogHistoryExportServiceImpl dkmKeyLogHistoryExportServiceImplUnderTest;
    @BeforeEach
    void setup() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DkmKeyLogHistoryExport.class);
    }
    @Test
    void testDownloadExcel() {
        // Setup
        final HttpServletResponse response = new MockHttpServletResponse();

        // Run the test
        dkmKeyLogHistoryExportServiceImplUnderTest.downloadExcel("fileName", response);

        // Verify the results
    }

    @Test
    void testCheckKeyUseLog() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");

        // Configure DkmKeyLogHistoryExportMapper.selectList(...).
        final List<DkmKeyLogHistoryExport> dkmKeyLogHistoryExports = Arrays.asList(
                new DkmKeyLogHistoryExport(0, "missionName", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), "creator", 0));
        when(mockDkmKeyLogHistoryExportMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(dkmKeyLogHistoryExports);

        // Run the test
        final PageResp result = dkmKeyLogHistoryExportServiceImplUnderTest.checkKeyUseLog("creator", 0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testCheckKeyUseLog_DkmKeyLogHistoryExportMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmKeyLogHistoryExportMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmKeyLogHistoryExportServiceImplUnderTest.checkKeyUseLog("creator", 0);

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }
}
