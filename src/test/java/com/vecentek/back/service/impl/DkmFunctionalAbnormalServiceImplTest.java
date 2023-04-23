package com.vecentek.back.service.impl;

import com.vecentek.back.mapper.DkmFunctionalAbnormalMapper;
import com.vecentek.common.response.PageResp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DkmFunctionalAbnormalServiceImplTest {

    @Mock
    private DkmFunctionalAbnormalMapper mockDkmFunctionalAbnormalMapper;

    @InjectMocks
    private DkmFunctionalAbnormalServiceImpl dkmFunctionalAbnormalServiceImplUnderTest;

    @Test
    void testSelectBusiness() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmFunctionalAbnormalMapper.selectBusiness()).thenReturn(Arrays.asList(new HashMap<>()));

        // Run the test
        final PageResp result = dkmFunctionalAbnormalServiceImplUnderTest.selectBusiness();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }

    @Test
    void testSelectBusiness_DkmFunctionalAbnormalMapperReturnsNoItems() {
        // Setup
        final PageResp expectedResult = PageResp.success("查询成功");
        when(mockDkmFunctionalAbnormalMapper.selectBusiness()).thenReturn(Collections.emptyList());

        // Run the test
        final PageResp result = dkmFunctionalAbnormalServiceImplUnderTest.selectBusiness();

        // Verify the results
        assertThat(result.getMsg()).isEqualTo(expectedResult.getMsg());
    }
}
