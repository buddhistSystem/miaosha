package com.liqian.mall.dao;

import com.liqian.mall.entity.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int deleteByUserIdAndShippingId(@Param("userId") Integer userId,
                                    @Param("shippingId") Integer shippingId);

    int updateByShipping(Shipping shipping);

    Shipping selectByUserIdAndShippingId(@Param("userId") Integer userId,
                                         @Param("shippingId") Integer shippingId);

    List<Shipping> listByUserId(Integer userId);

}