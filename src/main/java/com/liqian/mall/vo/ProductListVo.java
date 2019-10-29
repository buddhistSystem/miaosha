package com.liqian.mall.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Author: Administrator
 * @Date: 2019/8/22 0022 11:02
 * @Description:
 */
@Getter
@Setter
public class ProductListVo {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private BigDecimal price;

    private Integer status;

    private String imageHost;
}
