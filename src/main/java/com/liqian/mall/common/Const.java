package com.liqian.mall.common;


import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Set;

/**
 * @author liqian
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public interface Cart {
        //购物车选中
        int CHECKED = 1;
        //购物车未选中
        int UN_CHECKED = 0;
        //购物车数量大于库存
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        //购物车数量小于库存
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface Role {
        byte ROLE_CUSTOMER = 1;
        byte ROLE_ADMIN = 0;
    }

    @Getter
    public enum ProductStatus {
        /**
         * 商品状态
         */
        PRODUCT_ON_SALE(1, "在售");
        private int code;
        private String status;

        ProductStatus(int code, String status) {
            this.code = code;
            this.status = status;
        }
    }

    @Getter
    public enum OrderStatus {
        /**
         * 订单状态
         */
        CANCEL(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已支付"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单完成"),
        ORDER_CLOSE(60, "订单关闭");
        private int code;
        private String status;

        OrderStatus(int code, String status) {
            this.code = code;
            this.status = status;
        }

        public static OrderStatus codeOf(int code) {
            for (OrderStatus orderStatus : values()) {
                if (orderStatus.getCode() == code) {
                    return orderStatus;
                }
            }
            return null;
        }
    }

    public interface AlipayCallBack {
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    @Getter
    public enum PayPlatfrom {
        /**
         * 支付平台
         */
        ALIPAY(1, "支付宝"),
        WECHATPAY(2, "微信");
        private int code;
        private String value;

        PayPlatfrom(int code, String value) {
            this.code = code;
            this.value = value;
        }
    }

    @Getter
    public enum PaymentType {
        /**
         * 支付方式
         */
        ONLINE_PAY(1, "在线支付"),;
        private int code;
        private String value;

        PaymentType(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static PaymentType codeOf(int code) {
            for (PaymentType paymentType : values()) {
                if (paymentType.getCode() == code) {
                    return paymentType;
                }
            }
            return null;
        }
    }

}
