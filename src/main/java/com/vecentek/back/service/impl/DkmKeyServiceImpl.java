package com.vecentek.back.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.dto.DkmKeyDTO;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.DkmKeyLifecycle;
import com.vecentek.back.entity.DkmUser;
import com.vecentek.back.mapper.DkmKeyLifecycleMapper;
import com.vecentek.back.mapper.DkmKeyMapper;
import com.vecentek.back.mapper.DkmUserMapper;
import com.vecentek.common.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 钥匙信息(DkmKey)表服务实现类
 *
 * @author EdgeYu
 * @version 1.0
 * @since 2021-11-30 17:30:08
 */

@Slf4j
@Service("dkmKeyService")
public class DkmKeyServiceImpl {
    @Resource
    private DkmKeyMapper dkmKeyMapper;
    @Resource
    private DkmUserMapper dkmUserMapper;
    @Resource
    private DkmKeyLifecycleMapper dkmKeyLifecycleMapper;


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    public PageResp selectById(String id) {
        DkmKey dkmKey = this.dkmKeyMapper.selectById(id);

        return PageResp.success("查询成功", dkmKey);
    }

    /**
     * 通过车辆id查询钥匙列表
     *
     * @param vehicleId 车辆 id
     * @return 钥匙列表
     */
    public PageResp selectForPageByVehicleId(int pageIndex, int pageSize, Integer vehicleId) {
        Page<DkmKey> page = new Page<>(pageIndex, pageSize);
        page = dkmKeyMapper.selectPage(page, Wrappers.<DkmKey>lambdaQuery()
                .eq(true, DkmKey::getVehicleId, vehicleId)
                .orderByDesc(DkmKey::getApplyTime)
                .orderByAsc(DkmKey::getParentId));

        ArrayList<DkmKeyDTO> keyList = new ArrayList<>();

        page.getRecords().forEach(dkmKey -> {
            DkmKeyDTO dkmKeyDTO = new DkmKeyDTO();
            DkmUser dkmUser = dkmUserMapper.selectById(dkmKey.getUserId());
            BeanUtil.copyProperties(dkmKey, dkmKeyDTO);
            if (dkmUser != null) {
                dkmKeyDTO.setPhone(dkmUser.getPhone());
            }
            keyList.add(dkmKeyDTO);
        });

        return PageResp.success("查询成功", page.getTotal(), keyList);
    }

    public PageResp selectForPage(Integer pageIndex,
                                  Integer pageSize,
                                  String vin,
                                  String userId,
                                  Integer keyType,
                                  String applyStartTime,
                                  String applyEndTime,
                                  Integer periodMax,
                                  Integer periodMin,
                                  String periodUnit,
                                  String valFromStartTime,
                                  String valFromEndTime,
                                  String valToStartTime,
                                  String valToEndTime,
                                  List<Integer> dkStates) {
        if (keyType == null) {
            keyType = 3;
        }
        // 是否需要period条件
        boolean periodBool = false;
        int periodMaxFormat = 0;
        int periodMinFormat = 0;
        if (periodMax != null && periodMin != null && periodUnit != null){
            // 根据单元转换时间周期
            if (Objects.equals(periodUnit,"minute")){ // 分钟
                periodMaxFormat = periodMax;
                periodMinFormat = periodMin;
            }else if (Objects.equals(periodUnit,"hour")){
                periodMaxFormat = periodMax * 60;
                periodMinFormat = periodMin * 60;
            }else if (Objects.equals(periodUnit,"day")){
                periodMaxFormat = periodMax * 60 * 24;
                periodMinFormat = periodMin * 60 * 24;
            }
            periodBool = true;
        }
        // 是否需要dkStates条件
        if (dkStates != null && dkStates.size() > 0){

        }

        Page<DkmKey> page = new Page<>(pageIndex, pageSize);
        page = dkmKeyMapper.selectPage(page, Wrappers.<DkmKey>lambdaQuery()
                .like(StrUtil.isNotBlank(vin), DkmKey::getVin, vin)
                .like(StrUtil.isNotBlank(userId), DkmKey::getUserId, userId)
                .ge(StrUtil.isNotBlank(applyStartTime), DkmKey::getApplyTime, applyStartTime)
                .le(StrUtil.isNotBlank(applyEndTime), DkmKey::getApplyTime, applyEndTime)
                .ge(StrUtil.isNotBlank(valFromStartTime), DkmKey::getValFrom, valFromStartTime)
                .le(StrUtil.isNotBlank(valFromEndTime), DkmKey::getValFrom, valFromEndTime)
                .ge(StrUtil.isNotBlank(valToStartTime), DkmKey::getValTo, valToStartTime)
                .le(StrUtil.isNotBlank(valToEndTime), DkmKey::getValTo, valToEndTime)
                .ge(periodBool, DkmKey::getPeriod, periodMinFormat)
                .le(periodBool, DkmKey::getPeriod, periodMaxFormat)
                .eq(keyType == 1, DkmKey::getParentId, "0")
                .ne(keyType == 2, DkmKey::getParentId, "0")
                .orderByDesc(DkmKey::getVin)
                .orderByAsc(DkmKey::getParentId)
                .orderByDesc(DkmKey::getApplyTime)
        );
        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }

    /**
     * 根据用户id对钥匙信息进行分页查询
     *
     * @param id 用户id
     * @return 钥匙信息列表
     */
    public PageResp selectForPageByUserId(int pageIndex, int pageSize, Integer id) {
        Page<DkmKey> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<DkmKey> queryWrapper = Wrappers.<DkmKey>lambdaQuery()
                .eq(DkmKey::getUserId, id)
                .orderByDesc(DkmKey::getApplyTime);
        page = dkmKeyMapper.selectPage(page, queryWrapper);
        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }

    /**
     * 启用/停用钥匙
     *
     * @param keyId 钥匙id,dkState 钥匙状态 ，userId 用户id
     * @return 更新是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public PageResp updateStateById(String keyId, Integer dkState, String userId) {
        // 新增判断条件一个userId和一个vin号对应只能有一把钥匙dkState=1
        if (dkState == null) {
            return PageResp.fail(500, "钥匙状态未传递");
        }
        // 冻结
        if (dkState == 3) {
            // 更新状态
            DkmKey key = dkmKeyMapper.selectById(keyId);
            key.setDkState(dkState);
            key.setUpdateTime(new Date());
            int isSuccess = dkmKeyMapper.updateById(key);
            // 生命周期
            DkmKeyLifecycle dkmKeyLifecycle = new DkmKeyLifecycle();
            dkmKeyLifecycle.setKeyId(keyId);
            dkmKeyLifecycle.setCreateTime(new Date());
            dkmKeyLifecycle.setKeySource(3);
            if (Objects.equals(key.getParentId(), "0")) {
                dkmKeyLifecycle.setKeyType(1);
            } else {
                dkmKeyLifecycle.setKeyType(2);
            }
            dkmKeyLifecycle.setVin(key.getVin());
            // 冻结
            dkmKeyLifecycle.setKeyStatus(2);
            dkmKeyLifecycleMapper.insert(dkmKeyLifecycle);
            if (isSuccess == 1) {
                return PageResp.success("更新成功");
            }
            return PageResp.success("更新失败");
        }
        // 解冻
        if (dkState == 1) {
            DkmKey dkmKey = dkmKeyMapper.selectById(keyId);
            String vin = dkmKey.getVin();
            List<DkmKey> dkmKeyList = dkmKeyMapper.selectList(new QueryWrapper<DkmKey>().lambda()
                    .eq(DkmKey::getVin, vin)
                    .eq(DkmKey::getDkState, dkState)
                    .eq(DkmKey::getUserId, userId));
            if (dkmKeyList.size() >= 1) {
                return PageResp.success("更新失败，当前用户车辆已存在钥匙正在使用，不可解冻选中钥匙");
            } else {
                // 可以解冻 更新状态
                dkmKey.setDkState(dkState);
                dkmKey.setUpdateTime(new Date());
                int isSuccess = dkmKeyMapper.updateById(dkmKey);
                // 生命周期
                DkmKeyLifecycle dkmKeyLifecycle = new DkmKeyLifecycle();
                dkmKeyLifecycle.setKeyId(keyId);
                dkmKeyLifecycle.setCreateTime(new Date());
                dkmKeyLifecycle.setKeySource(3);
                if (Objects.equals(dkmKey.getParentId(), "0")) {
                    dkmKeyLifecycle.setKeyType(1);
                } else {
                    dkmKeyLifecycle.setKeyType(2);
                }
                dkmKeyLifecycle.setVin(dkmKey.getVin());
                // 解冻
                dkmKeyLifecycle.setKeyStatus(3);
                dkmKeyLifecycleMapper.insert(dkmKeyLifecycle);
                if (isSuccess == 1) {
                    return PageResp.success("更新成功");
                }
                return PageResp.success("更新失败");
            }
        }
        return PageResp.success("钥匙状态传参错误");
    }

    /**
     * 吊销钥匙,如为车主钥匙,则所有子钥匙均不可用
     *
     * @param id 钥匙id,钥匙 id 为16位字符串
     * @return 更新是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public PageResp updateStateForRevokeById(String id) {
        DkmKey dkmKey = this.dkmKeyMapper.selectById(id);
        log.info("吊销钥匙id: {}", id);
        if (dkmKey != null) {
            int update = dkmKeyMapper.update(dkmKey, Wrappers.<DkmKey>lambdaUpdate()
                    .set(DkmKey::getDkState, 5)
                    .eq(DkmKey::getId, id)
                    .or()
                    .eq(dkmKey.getParentId() == null, DkmKey::getParentId, id));
            if (update > 0) {
                return PageResp.success("吊销成功");
            }
        }

        return PageResp.fail("吊销失败");

    }

    public PageResp selectUserByKeyId(String keyId) {
        DkmKey dkmKey = dkmKeyMapper.selectById(keyId);
        if (dkmKey != null && dkmKey.getUserId() != null) {
            DkmUser dkmUser = dkmUserMapper.selectById(dkmKey.getUserId());
            dkmUser.setPhone(DesensitizedUtil.mobilePhone(dkmUser.getPhone()));
            return PageResp.fail("查询成功");
        }
        return PageResp.fail("查询失败");
    }

    public PageResp selectForPageByVal(int pageIndex, int pageSize, String valFrom, String valTo, Long period, Long dkState) {
        Page<DkmKey> page = new Page<>(pageIndex, pageSize);
        page = dkmKeyMapper.selectPage(page, Wrappers.<DkmKey>lambdaQuery()
                .eq(ObjectUtil.isNotNull(dkState), DkmKey::getDkState, dkState)
                .le(StrUtil.isNotBlank(valFrom), DkmKey::getValFrom, valFrom)
                .ge(StrUtil.isNotBlank(valTo), DkmKey::getValTo, valTo)
                .le(ObjectUtil.isNotNull(period), DkmKey::getPeriod, period));
        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }
}
