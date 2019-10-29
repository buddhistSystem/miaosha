package com.liqian.mall.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 收货地址
 *
 * @author liqian
 */
@Getter
@Setter
public class Shipping {
    private Integer id;

    private Integer userId;

    private String receiveName;

    private String receivePhone;

    private String receiveMobile;

    private String receiveProvince;

    private String receiveCity;

    private String receiveDistrict;

    private String receiveAddress;

    private String receiveZip;

    private Date createTime;

    private Date updateTime;

}