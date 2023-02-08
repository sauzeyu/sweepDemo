package com.vecentek.back.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.constant.ExcelConstant;
import com.vecentek.back.constant.KeyClassificationConstant;
import com.vecentek.back.constant.KeyResourceConstant;
import com.vecentek.back.constant.KeyStatusEnum;
import com.vecentek.back.dto.DkmKeyDTO;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.DkmKeyLifecycle;
import com.vecentek.back.entity.DkmKeyLogHistoryExport;
import com.vecentek.back.entity.DkmUser;
import com.vecentek.back.mapper.DkmKeyLifecycleMapper;
import com.vecentek.back.mapper.DkmKeyLogHistoryExportMapper;
import com.vecentek.back.mapper.DkmKeyMapper;
import com.vecentek.back.mapper.DkmUserMapper;
import com.vecentek.back.util.KeyLifecycleUtil;
import com.vecentek.common.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
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
    @Resource
    private DkmKeyLogHistoryExportMapper dkmKeyLogHistoryExportMapper;


    @Resource
    private KeyLifecycleUtil keyLifecycleUtil;

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
                .orderByDesc(DkmKey::getVin)
                .orderByAsc(DkmKey::getParentId)
                .orderByAsc(DkmKey::getDkState)
                .orderByDesc(DkmKey::getApplyTime));

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

    private int checkTimeUnit(Integer time, String timeUnit) {
        // TODO 缺少 default 值
        switch (timeUnit) {
            case "minute":
                break;
            case "hour":
                time = time * 60;
                break;
            case "day":
                time = time * 60 * 24;
                break;
        }
        if (time < 0) {
            time = Integer.MAX_VALUE;
        }
        return time;

    }

    public PageResp selectForPage(Integer pageIndex,
                                  Integer pageSize,
                                  String userId,
                                  String vin,
                                  Integer periodMin,
                                  Integer periodMax,
                                  String periodUnit,
                                  String applyStartTime,
                                  String applyEndTime,
                                  String valFromStartTime,
                                  String valFromEndTime,
                                  String valToStartTime,
                                  String valToEndTime,
                                  Integer keyType,
                                  Integer keyResource,
                                  Integer[] dkState,
                                  Integer[] keyClassification
    ) {
        if (keyType == null) {
            keyType = 3;
        }
        if (periodMax != null) {
            periodMax = checkTimeUnit(periodMax, periodUnit);
        }
        if (periodMin != null) {
            periodMin = checkTimeUnit(periodMin, periodUnit);
        }

        LambdaQueryWrapper<DkmKey> queryWrapper = Wrappers.lambdaQuery();
        Page<DkmKey> page = new Page<>(pageIndex, pageSize);
        // 是否需要dkStates条件
        if (dkState != null && dkState.length > 0) {
            queryWrapper.and(wrapper -> {
                wrapper.eq(DkmKey::getDkState, dkState[0]);
                if (dkState.length > 1) {
                    for (int i = 1; i < dkState.length; i++) {
                        wrapper.or().eq(DkmKey::getDkState, dkState[i]);
                    }
                }
            });
        }
        if (keyClassification != null && keyClassification.length > 0) {
            queryWrapper.and(wrapper -> {
                wrapper.eq(DkmKey::getKeyClassification, keyClassification[0]);
                if (keyClassification.length > 1) {
                    for (int i = 1; i < keyClassification.length; i++) {
                        wrapper.or().eq(DkmKey::getKeyClassification, keyClassification[i]);
                    }
                }
            });
        }

        page = dkmKeyMapper.selectPage(page, queryWrapper
                .like(StrUtil.isNotBlank(vin), DkmKey::getVin, vin)
                .like(StrUtil.isNotBlank(userId), DkmKey::getUserId, userId)
                .ge(StrUtil.isNotBlank(applyStartTime), DkmKey::getApplyTime, applyStartTime)
                .le(StrUtil.isNotBlank(applyEndTime), DkmKey::getApplyTime, applyEndTime)
                .ge(StrUtil.isNotBlank(valFromStartTime), DkmKey::getValFrom, valFromStartTime)
                .le(StrUtil.isNotBlank(valFromEndTime), DkmKey::getValFrom, valFromEndTime)
                .ge(StrUtil.isNotBlank(valToStartTime), DkmKey::getValTo, valToStartTime)
                .le(StrUtil.isNotBlank(valToEndTime), DkmKey::getValTo, valToEndTime)
                .ge(periodMin != null, DkmKey::getPeriod, periodMin)
                .le(periodMax != null, DkmKey::getPeriod, periodMax)
                .eq(keyType == 1, DkmKey::getParentId, "0")
                .ne(keyType == 2, DkmKey::getParentId, "0")
                .eq(keyResource != null, DkmKey::getKeyResource, keyResource)
                .orderByDesc(DkmKey::getVin)
                .orderByAsc(DkmKey::getParentId)
                .orderByAsc(DkmKey::getDkState)
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
    public PageResp updateStateById(String keyId, Integer dkState, String userId, String vin) {
        // 新增判断条件一个userId和一个vin号对应只能有一把钥匙
        if (dkState == null) {
            return PageResp.fail(500, "钥匙状态未传递");
        }
        if (dkState == 3) {
            //冻结
            return freezeKeys(dkState, userId, vin);
        } else if (dkState == 1) {
            //解冻
            return unfreezeKeys(dkState, userId, vin);
        } else {
            return PageResp.success("钥匙状态传参错误");
        }

    }

    private PageResp freezeKeys(Integer dkState, String userId, String vin) {
        List<DkmKey> dkmKeys = dkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery()
                .eq(DkmKey::getDkState, 1)
                .eq(DkmKey::getUserId, userId)
                .eq(DkmKey::getVin, vin)
        );

        for (int i = 0; i < dkmKeys.size(); i++) {
            DkmKey key = dkmKeys.get(i);
            key.setDkState(dkState);
            //key.setUpdateTime(new Date());
            int count = dkmKeyMapper.updateById(key);
            if (count == 0) {
                throw new RuntimeException("Failed to update key in database");
            }
            if (Objects.equals("0", key.getParentId())) {
                keyLifecycleUtil.insert(key, 1, 0, 2);
            } else {
                keyLifecycleUtil.insert(key, 2, 0, 2);
            }
        }
        return PageResp.success("更新成功");

    }

    private PageResp unfreezeKeys(Integer dkState, String userId, String vin) {
        // 查询是否有钥匙正在使用
        List<DkmKey> runDkmKeys = dkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery()
                .eq(DkmKey::getDkState, dkState)
                .eq(DkmKey::getUserId, userId)
                .eq(DkmKey::getVin, vin)
        );
        if (runDkmKeys.size() > 0) {
            return PageResp.success("更新失败，当前用户车辆已存在钥匙正在使用，不可解冻选中钥匙");
        }
        // 解冻状态为"冻结"的钥匙
        List<DkmKey> dkmKeys = dkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery()
                .eq(DkmKey::getDkState, 3)
                .eq(DkmKey::getUserId, userId)
                .eq(DkmKey::getVin, vin)
        );

        for (int i = 0; i < dkmKeys.size(); i++) {
            DkmKey key = dkmKeys.get(i);
            key.setDkState(1);
            int count = dkmKeyMapper.updateById(key);
            if (count == 0) {
                throw new RuntimeException("Failed to update key in database");
            }
            if (Objects.equals("0", key.getParentId())) {
                keyLifecycleUtil.insert(key, 1, 0, KeyStatusEnum.FREEZE.getCode());
            } else {
                keyLifecycleUtil.insert(key, 2, 0, KeyStatusEnum.FREEZE.getCode());
            }
        }
        return PageResp.success("更新成功");
    }

    /**
     * 吊销钥匙,如为车主钥匙,则所有子钥匙均不可用
     *
     * @param
     * @return 更新是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public PageResp updateStateForRevokeById(String userId, String vin) {
        List<DkmKey> keys = dkmKeyMapper.selectList(Wrappers.<DkmKey>lambdaQuery()
                .eq(DkmKey::getUserId, userId)
                .eq(DkmKey::getDkState, KeyStatusEnum.ACTIVATED.getCode())
                .eq(DkmKey::getVin, vin));
        for (DkmKey dkmKey : keys) {
            String id = dkmKey.getId();
            if (dkmKey != null) {
                int update = dkmKeyMapper.update(null, Wrappers.<DkmKey>lambdaUpdate()
                        .set(DkmKey::getDkState, KeyStatusEnum.REVOKE.getCode())
                        .eq(DkmKey::getId,dkmKey.getId())
                );
                if (update > 0) {
                    // 生命周期
                    DkmKeyLifecycle dkmKeyLifecycle = new DkmKeyLifecycle();
                    dkmKeyLifecycle.setKeyId(id);
                    dkmKeyLifecycle.setCreateTime(new Date());
                    dkmKeyLifecycle.setKeySource(1); // WEB页面
                    if (Objects.equals(dkmKey.getParentId(), "0")) {
                        dkmKeyLifecycle.setKeyType(1);
                    } else {
                        dkmKeyLifecycle.setKeyType(2);
                    }
                    dkmKeyLifecycle.setVin(dkmKey.getVin());
                    // 吊销
                    dkmKeyLifecycle.setKeyStatus(5);
                    dkmKeyLifecycleMapper.insert(dkmKeyLifecycle);
                    // 检查是否为父钥匙，吊销全部分享钥匙
                    if (Objects.equals(dkmKey.getParentId(), "0")) {
                        List<DkmKey> dkmKeys = dkmKeyMapper.selectList(new LambdaQueryWrapper<DkmKey>().eq(DkmKey::getParentId, id)
                                .eq(DkmKey::getDkState, KeyStatusEnum.ACTIVATED.getCode())
                                .or()
                                .eq(DkmKey::getDkState, KeyStatusEnum.FREEZE.getCode()));
                        for (DkmKey child : dkmKeys) {
                            String childId = child.getId();
                            child.setDkState(KeyStatusEnum.REVOKE.getCode());
                        child.setDkState(5);
                            dkmKeyMapper.updateById(child);
                            // 生命周期
                            DkmKeyLifecycle dkmKeyLifecycle1 = new DkmKeyLifecycle();
                            dkmKeyLifecycle1.setKeyId(childId);
                            dkmKeyLifecycle1.setCreateTime(new Date());
                            // WEB页面
                            dkmKeyLifecycle1.setKeySource(1);
                            dkmKeyLifecycle1.setKeyType(2);
                            dkmKeyLifecycle1.setVin(dkmKey.getVin());
                            // 吊销
                            dkmKeyLifecycle1.setKeyStatus(5);
                            dkmKeyLifecycleMapper.insert(dkmKeyLifecycle1);
                        }
                    }

                }
            }
        }
        return PageResp.success("吊销成功");

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

    public PageResp selectForPageByVal(int pageIndex, int pageSize, String valFrom, String valTo, Long
            period, Long dkState) {
        Page<DkmKey> page = new Page<>(pageIndex, pageSize);
        page = dkmKeyMapper.selectPage(page, Wrappers.<DkmKey>lambdaQuery()
                .eq(ObjectUtil.isNotNull(dkState), DkmKey::getDkState, dkState)
                .le(StrUtil.isNotBlank(valFrom), DkmKey::getValFrom, valFrom)
                .ge(StrUtil.isNotBlank(valTo), DkmKey::getValTo, valTo)
                .le(ObjectUtil.isNotNull(period), DkmKey::getPeriod, period));
        return PageResp.success("查询成功", page.getTotal(), page.getRecords());
    }

    /**
     * 单表excel分页导出
     *
     * @param vin
     * @param userId
     * @param keyType
     * @param applyStartTime
     * @param applyEndTime
     * @param periodMax
     * @param periodMin
     * @param periodUnit
     * @param valFromStartTime
     * @param valFromEndTime
     * @param valToStartTime
     * @param valToEndTime
     * @param dkState
     * @param creator
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void downloadKeyLogExcel(String vin,
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
                                    Integer[] dkState,
                                    Integer keyResource,
                                    String creator,
                                    String excelName,
                                    Integer[] keyClassification
    ) {


        // 1.5 使用1.1处文件名(时间戳)进行文件命名 并指定到服务器路径
//        String filePath = ("D:/excel/" + excelName + ExcelConstant.EXCEL_SUFFIX_XLSX); // 本地测试
        String filePath = (File.separatorChar + "excel" + File.separatorChar + excelName + ExcelConstant.EXCEL_SUFFIX_XLSX);

        // 是否有重名文件
        if (FileUtil.isFile(filePath)) {
            FileUtil.del(filePath);
        }

        ExcelWriter writer = ExcelUtil.getWriter(filePath);


        // 3.1所有数据量
        if (keyType == null) {
            keyType = 3;
        }
        if (periodMax != null) {
            periodMax = checkTimeUnit(periodMax, periodUnit);
        }
        if (periodMin != null) {
            periodMin = checkTimeUnit(periodMin, periodUnit);
        }
        LambdaQueryWrapper<DkmKey> queryWrapper = Wrappers.lambdaQuery();
        // 是否需要dkStates条件
        if (dkState != null && dkState.length > 0) {
            queryWrapper.and(wrapper -> {
                wrapper.eq(DkmKey::getDkState, dkState[0]);
                if (dkState.length > 1) {
                    for (int i = 1; i < dkState.length; i++) {
                        wrapper.or().eq(DkmKey::getDkState, dkState[i]);
                    }
                }
            });
        }
        if (keyClassification != null && keyClassification.length > 0) {
            queryWrapper.and(wrapper -> {
                wrapper.eq(DkmKey::getKeyClassification, keyClassification[0]);
                if (keyClassification.length > 1) {
                    for (int i = 1; i < keyClassification.length; i++) {
                        wrapper.or().eq(DkmKey::getKeyClassification, keyClassification[i]);
                    }
                }
            });
        }

        queryWrapper.like(StrUtil.isNotBlank(vin), DkmKey::getVin, vin)
                .like(StrUtil.isNotBlank(userId), DkmKey::getUserId, userId)
                .ge(StrUtil.isNotBlank(applyStartTime), DkmKey::getApplyTime, applyStartTime)
                .le(StrUtil.isNotBlank(applyEndTime), DkmKey::getApplyTime, applyEndTime)
                .ge(StrUtil.isNotBlank(valFromStartTime), DkmKey::getValFrom, valFromStartTime)
                .le(StrUtil.isNotBlank(valFromEndTime), DkmKey::getValFrom, valFromEndTime)
                .ge(StrUtil.isNotBlank(valToStartTime), DkmKey::getValTo, valToStartTime)
                .le(StrUtil.isNotBlank(valToEndTime), DkmKey::getValTo, valToEndTime)
                .ge(periodMin != null, DkmKey::getPeriod, periodMin)
                .le(periodMax != null, DkmKey::getPeriod, periodMax)
                .eq(keyType == 1, DkmKey::getParentId, "0")
                .ne(keyType == 2, DkmKey::getParentId, "0")
                .eq(keyResource != null, DkmKey::getKeyResource, keyResource)
                .orderByDesc(DkmKey::getVin)
                .orderByAsc(DkmKey::getParentId)
                .orderByDesc(DkmKey::getApplyTime);

        Integer sum = dkmKeyMapper.selectCount(queryWrapper);


        // 3.2每次分页数据量10W (SXXSF 最大分页100W)
        Integer end = 100000;

        List<DkmKey> dkmKeys;

        // 4将数据库查询和单个sheet导出操作视为原子操作 按数据总量和递增值计算原子操作数
        try {

            for (int i = 0; i <= sum / end; i++) {
                int start = (i * end);

                // 4.1分页查询数据 否则会OOM
                dkmKeys = selectDkmKeyLogs(vin,
                        userId,
                        keyType,
                        applyStartTime,
                        applyEndTime,
                        periodMax,
                        periodMin,
                        periodUnit,
                        valFromStartTime,
                        valFromEndTime,
                        valToStartTime,
                        valToEndTime,
                        dkState,
                        keyResource,
                        keyClassification,
                        start,
                        end);

                // 4.2首个sheet需要重新命名
                if (i == 0) {
                    writer.renameSheet("初始表");
                    // 4.3写入新sheet会刷新样式 每次都需要重新设置单元格样式
                    extracted(writer);
                    // 4.4一次性写出内容，使用默认样式，强制输出标题
                    writer.write(dkmKeys, true);
                    continue;
                }
                writer.setSheet("表" + (i + 1));
                extracted(writer);
                writer.write(dkmKeys, true);
            }


        } catch (Exception e) {
            e.printStackTrace();
            dkmKeyLogHistoryExportMapper.update(null,
                    Wrappers.<DkmKeyLogHistoryExport>lambdaUpdate().set(DkmKeyLogHistoryExport::getExportStatus, 2).eq(DkmKeyLogHistoryExport::getMissionName, excelName));
        } finally {
            writer.close();
        }

        // 5将历史记录中该条数据记录根据导出情况进行修改
        dkmKeyLogHistoryExportMapper.update(null,
                Wrappers.<DkmKeyLogHistoryExport>lambdaUpdate().set(DkmKeyLogHistoryExport::getExportStatus, 1).eq(DkmKeyLogHistoryExport::getMissionName, excelName));

    }

    /**
     * BigExcelWriter设置单元格样式
     *
     * @param writer
     */
    private void extracted(ExcelWriter writer) {
        // 4.3.1表头只显示取别名的字段
        writer.setOnlyAlias(true);

        // 4.3.2设置单元格与字体的样式
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

        writer.setColumnWidth(0, 30);
        writer.setColumnWidth(1, 20);
        writer.setColumnWidth(2, 20);
        writer.setColumnWidth(3, 20);
        writer.setColumnWidth(4, 20);
        writer.setColumnWidth(5, 20);
        writer.setColumnWidth(6, 20);
        writer.setColumnWidth(7, 20);
        writer.setColumnWidth(8, 20);


        writer.addHeaderAlias("parentId", "钥匙类型");
        writer.addHeaderAlias("userId", "用户id");
        writer.addHeaderAlias("vin", "车辆标识符");
        writer.addHeaderAlias("personalAndCalibration", "钥匙状态");
        writer.addHeaderAlias("valFrom", "生效时间");
        writer.addHeaderAlias("valTo", "失效时间");
        writer.addHeaderAlias("applyTime", "申请时间");
        writer.addHeaderAlias("keyClassification", "钥匙分类");
        writer.addHeaderAlias("keyResourceVO", "钥匙来源");
    }

    /**
     * 根据分页条件去查询钥匙记录
     *
     * @return
     */
    private List<DkmKey> selectDkmKeyLogs(String vin,
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
                                          Integer[] dkState,
                                          Integer keyResource,
                                          Integer[] keyClassification,
                                          Integer start,
                                          Integer end) {


        LambdaQueryWrapper<DkmKey> queryWrapper = Wrappers.lambdaQuery();
        // 是否需要dkStates条件
        if (dkState != null && dkState.length > 0) {
            queryWrapper.and(wrapper -> {
                wrapper.eq(DkmKey::getDkState, dkState[0]);
                if (dkState.length > 1) {
                    for (int i = 1; i < dkState.length; i++) {
                        wrapper.or().eq(DkmKey::getDkState, dkState[i]);
                    }
                }
            });
        }
        if (keyClassification != null && keyClassification.length > 0) {
            queryWrapper.and(wrapper -> {
                wrapper.eq(DkmKey::getKeyClassification, keyClassification[0]);
                if (keyClassification.length > 1) {
                    for (int i = 1; i < keyClassification.length; i++) {
                        wrapper.or().eq(DkmKey::getKeyClassification, keyClassification[i]);
                    }
                }
            });
        }
        queryWrapper.like(StrUtil.isNotBlank(vin), DkmKey::getVin, vin)
                .like(StrUtil.isNotBlank(userId), DkmKey::getUserId, userId)
                .ge(StrUtil.isNotBlank(applyStartTime), DkmKey::getApplyTime, applyStartTime)
                .le(StrUtil.isNotBlank(applyEndTime), DkmKey::getApplyTime, applyEndTime)
                .ge(StrUtil.isNotBlank(valFromStartTime), DkmKey::getValFrom, valFromStartTime)
                .le(StrUtil.isNotBlank(valFromEndTime), DkmKey::getValFrom, valFromEndTime)
                .ge(StrUtil.isNotBlank(valToStartTime), DkmKey::getValTo, valToStartTime)
                .le(StrUtil.isNotBlank(valToEndTime), DkmKey::getValTo, valToEndTime)
                .ge(periodMin != null, DkmKey::getPeriod, periodMin)
                .le(periodMax != null, DkmKey::getPeriod, periodMax)
                .eq(keyType == 1, DkmKey::getParentId, "0")
                .ne(keyType == 2, DkmKey::getParentId, "0")
                .eq(keyResource != null, DkmKey::getKeyResource, keyResource)
                .orderByDesc(DkmKey::getVin)
                .orderByAsc(DkmKey::getParentId)
                .orderByDesc(DkmKey::getApplyTime)
                .last("limit " + start + "," + end);
        List<DkmKey> keyList = dkmKeyMapper.selectList(queryWrapper);

        // 4.1.2执行库中部分字段二次封装
        if (!keyList.isEmpty()) {

            keyList.forEach(key -> {
                if (!Objects.equals(key.getParentId(), "0")) {
                    key.setParentId("分享钥匙");
                }
                if (Objects.equals(key.getParentId(), "0")) {
                    key.setParentId("车主钥匙");
                }
                if (KeyResourceConstant.APP.equals(key.getKeyResource())) {
                    key.setKeyResourceVO("APP");
                }
                if (KeyResourceConstant.SMALLPROGRAM.equals(key.getKeyResource())) {
                    key.setKeyResourceVO("小程序");
                }
                if (KeyClassificationConstant.ICCE.equals(key.getKeyClassification())) {
                    key.setKeyClassificationVO("ICCE");
                }
                if (KeyClassificationConstant.CCC.equals(key.getKeyClassification())) {
                    key.setKeyClassificationVO("CCC");
                }
                key.setPersonalAndCalibration(KeyStatusEnum.matchName(key.getDkState()));
            });

        }

        return keyList;
    }

}
