package com.vecentek.back.service.impl;


import com.vecentek.back.mapper.DkmFunctionalAbnormalMapper;
import com.vecentek.common.response.PageResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-03-11 10:38
 */
@Service
public class DkmFunctionalAbnormalServiceImpl {

    @Resource
    private DkmFunctionalAbnormalMapper dkmFunctionalAbnormalMapper;


    public PageResp selectBusiness() {
        List<Map> dkmFunctionalAbnormal = dkmFunctionalAbnormalMapper.selectBusiness();
        return PageResp.success("查询成功",dkmFunctionalAbnormal);
    }
}
