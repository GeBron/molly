package com.demo.molly.exception;

import com.demo.molly.common.ErrorCode;
import com.demo.molly.common.Result;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = ErrorCode.PARAM_ERROR.getMessage();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            message = error.getField() + ":" + error.getDefaultMessage();
            break;
        }
        return Result.error(ErrorCode.PARAM_ERROR.getCode(), message);
    }

    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        String message = ErrorCode.PARAM_ERROR.getMessage();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            message = error.getField() + ":" + error.getDefaultMessage();
            break;
        }
        return Result.error(ErrorCode.PARAM_ERROR.getCode(), message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = ErrorCode.PARAM_ERROR.getMessage();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            message = violation.getMessage();
            break;
        }
        return Result.error(ErrorCode.PARAM_ERROR.getCode(), message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return Result.error(ErrorCode.PARAM_ERROR.getCode(), "缺少必要参数：" + e.getParameterName());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public Result<Void> handleMissingPathVariableException(MissingPathVariableException e) {
        return Result.error(ErrorCode.PARAM_ERROR.getCode(), "缺少路径参数：" + e.getVariableName());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return Result.error(ErrorCode.PARAM_ERROR.getCode(), "参数类型错误：" + e.getName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return Result.error(ErrorCode.PARAM_ERROR.getCode(), "请求体格式错误");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result<Void> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return Result.error(ErrorCode.PARAM_ERROR.getCode(), "不支持的 Content-Type：" + e.getContentType());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return Result.error(ErrorCode.METHOD_NOT_ALLOWED.getCode(), "不支持的请求方法：" + e.getMethod());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return Result.error(ErrorCode.NOT_FOUND.getCode(), "请求路径不存在：" + e.getRequestURL());
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public Result<Void> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException e) {
        return Result.error(ErrorCode.INTERNAL_ERROR.getCode(), "请求处理超时");
    }

    @ExceptionHandler(AuthenticationException.class)
    public Result<Void> handleAuthenticationException(AuthenticationException e) {
        return Result.error(ErrorCode.UNAUTHORIZED.getCode(), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        return Result.error(ErrorCode.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        logger.error("系统异常，请求路径：{}，异常信息：{}", request.getRequestURI(), e.getMessage(), e);
        return Result.error(ErrorCode.INTERNAL_ERROR);
    }
}
