package com.liqian.mall.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Administrator
 * @Date: 2019/8/30 0030 15:20
 * @Description:
 */
@Getter
@Setter
public class CartVo {

    private List<CartProductVo> cartProductVoList;

    private BigDecimal cartTotalPrice;

    private Boolean allChecked;

    private String imageHost;
}
