package com.liqian.mall.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author liqian
 */
@Getter
@Setter
public class Cart {

    private Integer id;

    private Integer userId;

    private Integer productId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 是否勾选
     */
    private Integer checked;

    private Date createTime;

    private Date updateTime;

}