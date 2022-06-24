package com.vecentek.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmKeyLifecycle;
import com.vecentek.back.mapper.DkmKeyLifecycleMapper;
import com.vecentek.common.response.PageResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-19 14:39
 */
@Service
public class DkmDkmKeyLifecycleServiceImpl {
    @Resource
    private DkmKeyLifecycleMapper dkmKeyLifecycleMapper;

    public PageResp selectForPage(int pageIndex, int pageSize, String keyId) {
        Page<DkmKeyLifecycle> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<DkmKeyLifecycle> wrapper = Wrappers.<DkmKeyLifecycle>lambdaQuery()
                .eq(DkmKeyLifecycle::getKeyId, keyId)
                .orderByAsc(DkmKeyLifecycle::getKeyType);
        page = dkmKeyLifecycleMapper.selectPage(page, wrapper);
        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }
}
