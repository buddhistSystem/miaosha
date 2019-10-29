package com.liqian.mall.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liqian
 */
@Getter
@Setter
public class OrderProductVo {

    private List<OrderDetailVo> orderDetailVoList;

    private BigDecimal productTotalPrice;

    private String imageHost;
}
