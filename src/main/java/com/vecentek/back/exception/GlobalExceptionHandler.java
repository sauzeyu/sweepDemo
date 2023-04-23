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

    @ExceptionHandler(DiagnosticLogsException.class)
    public PageResp DiagnosticLogsException(DiagnosticLogsException e) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(formatter);

        String faultId = e.getFaultId();
        String businessId = e.getBusinessId();
        DkmFunctionalAbnormal thisDkmFunctionalAbnormal = null;
        for (DkmFunctionalAbnormal dkmFunctionalAbnormal : unfunctionalMap.values()) {
            if (dkmFunctionalAbnormal.getBusinessId().equals(businessId) && dkmFunctionalAbnormal.getFaultId().equals(faultId)) {
                thisDkmFunctionalAbnormal = dkmFunctionalAbnormal;
                break;
            }
        }
        String separator = "|";
        //esLog.info(thisDkmFunctionalAbnormal.getBusinessId() + Separator + thisDkmFunctionalAbnormal.getBusiness() + Separator
        //        + thisDkmFunctionalAbnormal.getSourceId() + Separator + thisDkmFunctionalAbnormal.getSource() + Separator + thisDkmFunctionalAbnormal.getSourceId() + Separator + thisDkmFunctionalAbnormal.getSource()
        //        + thisDkmFunctionalAbnormal.getFaultId() + Separator + thisDkmFunctionalAbnormal.getFault() + Separator
        //        + thisDkmFunctionalAbnormal.getSolution()
        //);
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
                thisDkmFunctionalAbnormal.getSolution().get(0)
        };
        String join = String.join(separator, values);
        esLog.info(join);
        return errorResult(thisDkmFunctionalAbnormal.getFault(), Integer.parseInt(thisDkmFunctionalAbnormal.getFaultId()), e);
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

    @PostConstruct
    public void init() {
        List<Map> dkmFunctionalAbnormalList = dkmFunctionalAbnormalMapper.selectAll();

        for (int i = 0; i < dkmFunctionalAbnormalList.size(); i++) {
            Map map = dkmFunctionalAbnormalList.get(i);
            Integer id = (Integer) map.get("id");
            String businessId = (String) map.get("business_id");
            String business = (String) map.get("business");
            String faultId = (String) map.get("fault_id");
            String fault = (String) map.get("fault");
            String rawSolution = (String) map.get("solution");
            String source = (String) map.get("terminal");
            String sourceId = (String) map.get("terminal_id");
            List<String> solution = null;
            if (ObjectUtils.isNotEmpty(rawSolution)) {
                String[] solutions = rawSolution.split("\r\n\r\n");
                solution = new ArrayList<>(Arrays.asList(solutions));
            }
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

            unfunctionalMap.put(id, functionalAbnormality);
        }
    }
}

