package com.liqian.mall.dao;

import com.liqian.mall.entity.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    Cart getCartByUserIdAndProductId(@Param("userId") Integer userId,
                                     @Param("productId") Integer productId);

    List<Cart> listCartByUserId(Integer userId);

    int getCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdAndProductIds(@Param("userId") Integer userId,
                                    @Param("productIdList") List<String> productIdList);

    int checkedOrUnCheckedProduct(@Param("userId") Integer userId,
                                  @Param("checked") Integer checked,
                                  @Param("productId") Integer productId);

    /**
     * @param userId 用户id
     * @return int 商品数量
     */
    int getCartProductCount(Integer userId);

    /**
     *
     * @param userId 用户id
     * @return list 勾选的购物车数据
     */
    List<Cart> listCheckedCartByUserId(Integer userId);

}