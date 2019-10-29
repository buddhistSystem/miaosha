package com.liqian.mall.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


/**
 * @author liqian
 */
@Getter
@Setter
public class CartProductVo {
    /**
     * 购物车id
     */
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 购物车中此商品的数量
     */
    private Integer quantity;
    /**
     * 购物车中此商品是否被选中
     */
    private Integer checked;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 副标题
     */
    private String productSubTitle;
    /**
     * 主图
     */
    private String productMainImage;
    /**
     * 单价
     */
    private BigDecimal productPrice;
    /**
     * 状态
     */
    private Integer productStatus;
    /**
     * 该商品的总价(商品单价*商品数量)
     */
    private BigDecimal productTotalPrice;
    /**
     * 该商品库存
     */
    private Integer productStock;
    /**
     * 限制数量
     */
    private String limitQuantity;

}
