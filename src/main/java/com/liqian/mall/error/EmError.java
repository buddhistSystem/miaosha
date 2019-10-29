package com.liqian.mall.error;

import lombok.Getter;

/**
 * 错误枚举类
 *
 * @author liqian
 */
@Getter
public enum EmError {

    /**
     * 通用类型错误1000
     */
    UNKNOWN_ERROR(10001, "未知错误"),
    PARAMETER_VALIDATION_ERROR(10002, "参数不合法"),
    NOT_PERMISSION(10003, "无权访问"),
    /**
     * 用户类错误以2000开头
     */
    USER_NOT_EXIST(20001, "用户不存在"),
    LOGIN_PASSWORD_ERROR(20002, "密码错误"),
    USERNAME_EXIST(20003, "该用户名已存在"),
    EMAIL_EXIST(20004, "邮箱已存在"),
    USER_NOT_LOGIN(20005, "用户未登录"),
    PASSWORD_NOT_NULL(20006, "密码不能为空"),
    USERNAME_NOT_NULL(20007, "用户名不能为空"),
    QUESTION_ANSWER_ERROR(20008, "问题的答案错误"),
    TOKEN_INVALID_OR_EXPIRED(20009, "token无效或者过期"),
    TOKEN_ERROR(20010, "token错误"),
    OLD_PASSWORD_ERROR(20011, "旧密码错误"),
    /**
     * 分类错误以3000开头
     */
    CATEGORY_NAME_NOT_NULL(30001, "分类名称不能为空"),
    CATEGORY_ID_NOT_NULL(30002, "分类id不能为空"),
    CATEGORY_PARENT_NOT_EXIST(30003, "该父分类不存在"),
    /**
     * 产品类错误以4000开头
     */
    PRODUCT_NOT_EXIST(40001, "产品不存在"),
    PRODUCT_NOT_ON_SALE(40002, "产品已下架或删除"),
    PRODUCT_STOCK_NOT_WORTH(40003, "产品库存不足"),
    /**
     * ftp上传类错误以5000开头
     */
    UPLOAD_FILE_NOT_NULL(50001, "上传文件不能为空"),

    /**
     * 收货地址类错误以6000开头
     */
    SHIPPING_NOT_EXIST(60001, "该收货地址不存在"),
    SHIPPING_ID_NOT_NULL(60002, "收货地址id不能为空"),
    /**
     * 订单类错误以7000开头
     */
    ORDER_NOT_EXIST(70001, "订单不存在"),
    ORDER_IS_PAY(70002,"已付款无法取消订单"),
    /**
     * 购物车错误
     */
    CART_NULL(80001, "购物车为空"),;

    private int errorCode;
    private String errorMsg;

    EmError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


}
