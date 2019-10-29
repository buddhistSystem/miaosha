package com.liqian.mall.common;

import com.liqian.mall.error.EmError;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * @author Administrator
 */
@Getter
@Setter
public class Result<T> implements Serializable {

    private int status;

    private String msg;

    private T data;

    public static Result createBySuccess() {
        Result result = new Result();
        result.setStatus(0);
        result.setMsg("success");
        return result;
    }

    public static Result createBySuccess(Object object) {
        Result result = new Result();
        result.setStatus(0);
        result.setMsg("success");
        result.setData(object);
        return result;
    }

    public static Result createBySuccess(String msg, Object object) {
        Result result = new Result();
        result.setStatus(0);
        result.setMsg(msg);
        result.setData(object);
        return result;
    }

    public static Result createByError(EmError emError) {
        Result result = new Result();
        result.setStatus(emError.getErrorCode());
        result.setMsg(emError.getErrorMsg());
        return result;
    }

}
