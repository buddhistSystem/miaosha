package com.liqian.mall.error;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Administrator
 * @Date: 2019/8/19 0019 14:31
 * @Description:包装器业务异常类实现
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {

    private Integer code;

    private String msg;

    public BusinessException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(EmError emError) {
        this.code = emError.getErrorCode();
        this.msg = emError.getErrorMsg();
    }


}
