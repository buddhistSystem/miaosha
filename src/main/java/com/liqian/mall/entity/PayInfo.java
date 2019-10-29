package com.liqian.mall.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 支付信息
 *
 * @author liqian
 */
@Getter
@Setter
public class PayInfo {
    private Integer id;

    private Integer userId;

    private String orderNo;

    private Integer payPlatfrom;

    private String platformNumber;

    private String platformStatus;

    private Date createTime;

    private Date updateTime;

}