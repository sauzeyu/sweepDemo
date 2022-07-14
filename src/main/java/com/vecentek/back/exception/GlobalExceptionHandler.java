package com.vecentek.back.exception;

import com.vecentek.common.response.PageResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

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

    @ExceptionHandler(ClassNotFoundException.class)
    public PageResp classNotFoundException(ClassNotFoundException e) {
        return errorResult("没有发现此类！", e);
    }

    @ExceptionHandler(Throwable.class)
    public PageResp throwable(Throwable e) {
        return errorResult("服务繁忙,请稍后...", e);
    }

    private PageResp errorResult(String msg, Throwable e) {
        return errorResult(msg, 500, e);
    }

    private PageResp errorResult(String msg, int code, Throwable e) {
        e.printStackTrace();
        return PageResp.fail(code, msg);
    }
}

