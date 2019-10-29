package com.liqian.mall.controller;

import com.liqian.mall.common.Result;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * 统一异常处理
 *
 * @author liqian
 */
@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object handleException(Exception ex) {
        log.info(ex.getMessage());
        Result result = new Result();
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            result.setStatus(businessException.getCode());
            result.setMsg(businessException.getMsg());
        } else {
            result.setStatus(EmError.UNKNOWN_ERROR.getErrorCode());
            result.setMsg(EmError.UNKNOWN_ERROR.getErrorMsg());
        }
        return result;
    }


}
