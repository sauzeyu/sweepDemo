package com.vecentek.back.exception;

import com.vecentek.back.entity.DkmFunctionalAbnormal;
import com.vecentek.back.mapper.DkmFunctionalAbnormalMapper;
import com.vecentek.common.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author EdgeYu
 * @version 1.0
 * @since 2021-11-30 17:27:19
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Map<Integer, DkmFunctionalAbnormal> unfunctionalMap = new HashMap<>(512);
    private static final Logger esLog = LoggerFactory.getLogger("esLog");

    @Resource
    DkmFunctionalAbnormalMapper dkmFunctionalAbnormalMapper;

    @ExceptionHandler(SQLException.class)
    public PageResp sqlException(SQLException e) {
        return errorResult("SQL语句异常！", e);
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public PageResp fileSizeLimitExceededException(FileSizeLimitExceededException e) {
        return errorResult("上传文件过大!请勿超过100MB", e);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public PageResp maxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return errorResult("上传文件过大!请勿超过100MB", e);
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public PageResp sizeLimitExceededException(SizeLimitExceededException e) {
        return errorResult("上传文件过大!请勿超过100MB", e);
    }

    @ExceptionHandler(VecentException.class)
    public PageResp vecentException(VecentException e) {
        return errorResult(e.getMessage(), e.getCode(), e);
    }

    @ExceptionHandler(SQLSyntaxErrorException.class)
    public PageResp sqlSyntaxErrorException(SQLSyntaxErrorException e) {
        return errorResult("SQL参数异常！", e);
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public PageResp badSqlGrammarException(BadSqlGrammarException e) {
        return errorResult("SQL语法异常！", e);
    }

    @ExceptionHandler(ParameterValidationException.class)
    public PageResp parameterValidationException(ParameterValidationException e) {
        return errorResult("必填参数未传递或传入的参数格式不正确！", 1001, e);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public PageResp httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return errorResult("必填参数未传递或传入的参数格式不正确！", 1001, e);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public PageResp unauthorizedAccessException(UnauthorizedAccessException e) {
        return errorResult("越权访问,无此访问权限！", 1003, e);
    }

    @ExceptionHandler(VkServiceUnavailableException.class)
    public PageResp vkServiceUnavailableException(VkServiceUnavailableException e) {
        return errorResult("VK 服务不可用！", 1004, e);
    }

    @ExceptionHandler(UploadOverMaximumException.class)
    public PageResp uploadOverMaximumException(UploadOverMaximumException e) {
        return errorResult("上传数据量超过最大值！", 2107, e);
    }

    /**
     *  统一处理故障汇总 & 格式定义
     *
     *  格式如下：
     *  日志记录时间（2023-01-31 19:02:18）|日志类型key（01）|日志类型value（关键信息）|异常来源key（04）|异常来源value（小程序）|业务Idkey（01）|业务Idvalue（车端）|功能异常idkey（B00B）|功能异常idvalue（签名数据UnPadding失败）|解决方案 （联系数字钥匙云端供应商云端对应padding数据）
     *
     * 示例数据如下：
     *
     * 2023-01-31 19:02:18 | 01 | 关键信息 | 04 | 小程序 | 01 | 车端 | B00B | 签名数据UnPadding失败 | 联系数字钥匙云端供应商云端对应padding数据
     * @param e
     * @return PageResp
     */
    @ExceptionHandler(DiagnosticLogsException.class)
    public PageResp diagnosticLogsException(DiagnosticLogsException e) {
        // 获取当前日志记录时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(formatter);

        String faultId = e.getFaultId();
        String businessId = e.getBusinessId();
        // 遍历 unfunctionalMap 集合获取异常信息
        DkmFunctionalAbnormal thisDkmFunctionalAbnormal = unfunctionalMap.values().stream()
                .filter(d -> d.getBusinessId().equals(businessId) && d.getFaultId().equals(faultId))
                .findFirst().orElse(null);
        if (thisDkmFunctionalAbnormal == null) {
            return errorResult("未找到相应则的异常信息", 500, e);
        }
        // 拼接日志信息
        String separator = "|";
        String[] values = {
                dateTime,
                thisDkmFunctionalAbnormal.getBusinessId(),
                thisDkmFunctionalAbnormal.getBusiness(),
                thisDkmFunctionalAbnormal.getSourceId(),
                thisDkmFunctionalAbnormal.getSource(),
                thisDkmFunctionalAbnormal.getSourceId(),
                thisDkmFunctionalAbnormal.getSource(),
                thisDkmFunctionalAbnormal.getFaultId(),
                thisDkmFunctionalAbnormal.getFault(),
                //TODO 按list存入
                thisDkmFunctionalAbnormal.getSolution().get(0),
                "",
                ""
                //String.join(" ", thisDkmFunctionalAbnormal.getSolution()) // 使用 String.join() 方法将解决方案按空格分隔转换为字符串
        };
        String join = String.join(separator, values);
        esLog.info(join);
        // 返回响应结果
        return errorResult(thisDkmFunctionalAbnormal.getFault(),
                e.getCode() != null ? e.getCode() : 500, e);
    }


    @ExceptionHandler(ClassNotFoundException.class)
    public PageResp classNotFoundException(ClassNotFoundException e) {
        return errorResult("没有发现此类！", e);
    }


    @ExceptionHandler(Throwable.class)
    public PageResp throwable(Throwable e) {
        log.error("服务繁忙,请稍后...", e);
        return errorResult("服务繁忙,请稍后...", e);
    }

    private PageResp errorResult(String msg, Throwable e) {
        return errorResult(msg, 500, e);
    }

    private PageResp errorResult(String msg, int code, Throwable e) {
        e.printStackTrace();
        return PageResp.fail(code, msg);
    }

    /**
     * 该方法在类实例化后立即执行
     * 将数据库表中的异常信息取出，并转为对象存储到 unfunctionalMap
     */
    @PostConstruct
    public void init() {
        // 获取所有异常信息
        List<Map> dkmFunctionalAbnormalList = dkmFunctionalAbnormalMapper.selectAll();

        for (Map<String, Object> map : dkmFunctionalAbnormalList) {
            // 从 map 中获取异常信息
            Integer id = (Integer) map.get("id");
            String businessId = (String) map.get("business_id");
            String business = (String) map.get("business");
            String faultId = (String) map.get("fault_id");
            String fault = (String) map.get("fault");
            String rawSolution = (String) map.get("solution");
            String source = (String) map.get("terminal");
            String sourceId = (String) map.get("terminal_id");
            // 解析解决方案，将分隔符为 "\r\n\r\n" 的字符串转换为字符串列表
            List<String> solution = null;
            if (ObjectUtils.isNotEmpty(rawSolution)) {
                String[] solutions = rawSolution.split("\r\n\r\n");
                solution = new ArrayList<>(Arrays.asList(solutions));
            }
            // 构建 FunctionalAbnormality 对象
            DkmFunctionalAbnormal functionalAbnormality = DkmFunctionalAbnormal.builder()
                    .id(id)
                    .businessId(businessId)
                    .business(business)
                    .faultId(faultId)
                    .fault(fault)
                    .solution(solution)
                    .source(source)
                    .sourceId(sourceId)
                    .build();
            // 将对象存储到 unfunctionalMap 中
            unfunctionalMap.put(id, functionalAbnormality);
        }
    }
}

