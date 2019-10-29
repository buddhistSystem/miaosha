package com.liqian.mall.dao;

import com.liqian.mall.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    Order selectByUserIdAndOrderNo(@Param("userId") Integer userId,
                                   @Param("orderNo")String orderNo);

    Order selectByOrderNo(String orderNo);

    List<Order> listByUserId(Integer userId);

    List<Order> listAll();
}