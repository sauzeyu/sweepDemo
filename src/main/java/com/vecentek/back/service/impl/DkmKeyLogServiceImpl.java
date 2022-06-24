package com.vecentek.back.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.constant.BluetoothErrorReasonEnum;
import com.vecentek.back.constant.KeyErrorReasonEnum;
import com.vecentek.back.constant.KeyStatusCodeEnum;
import com.vecentek.back.entity.DkmKeyLog;
import com.vecentek.back.mapper.DkmKeyLogMapper;
import com.vecentek.common.response.PageResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-24 17:11
 */
@Service
public class DkmKeyLogServiceImpl {
    @Resource
    private DkmKeyLogMapper dkmKeyLogMapper;


    public PageResp selectForPage(int pageIndex, int pageSize, String vin, String userId, String startTime, String endTime, String phoneBrand,String phoneModel,String statusCode,String errorReason) {
        Page<DkmKeyLog> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<DkmKeyLog> wrapper = Wrappers.<DkmKeyLog>lambdaQuery()
                .like(StrUtil.isNotBlank(statusCode), DkmKeyLog::getStatusCode, statusCode)
                .like(StrUtil.isNotBlank(errorReason), DkmKeyLog::getErrorReason, errorReason)
                .like(StrUtil.isNotBlank(phoneModel), DkmKeyLog::getPhoneModel, phoneModel)
                .like(StrUtil.isNotBlank(phoneBrand), DkmKeyLog::getPhoneBrand, phoneBrand)
                .like(StrUtil.isNotBlank(userId), DkmKeyLog::getUserId, userId)
                .like(StrUtil.isNotBlank(vin), DkmKeyLog::getVin, vin)
                .ge(StrUtil.isNotBlank(startTime), DkmKeyLog::getOperateTime, startTime)
                .le(StrUtil.isNotBlank(endTime), DkmKeyLog::getOperateTime, endTime)
                .orderByDesc(DkmKeyLog::getOperateTime);
        page = dkmKeyLogMapper.selectPage(page, wrapper);

        if (page.getRecords().size() > 0) {
            page.getRecords().forEach(keyLog -> {
                keyLog.setStatusCode(KeyStatusCodeEnum.matchName(keyLog.getStatusCode()));
                if (keyLog.getFlag() != null && keyLog.getFlag() == 0) {
                    if (KeyStatusCodeEnum.SAFE_BLUETOOTH_DISCONNECT.getName().equals(keyLog.getStatusCode())) {
                        keyLog.setErrorReason(BluetoothErrorReasonEnum.matchReason(keyLog.getErrorReason()));
                    } else {
                        keyLog.setErrorReason(KeyErrorReasonEnum.matchReason(keyLog.getErrorReason()));
                    }
                }
            });
        }

        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }
}