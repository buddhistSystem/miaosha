package com.liqian.mall.dao;

import com.liqian.mall.entity.OrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDetailMapper {

    int insertSelective(OrderDetail record);

    OrderDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDetail record);

    int insertOrderDetailList(List<OrderDetail> list);

    List<OrderDetail> listByUserIdAndOrderNo(@Param("userId") Integer userId,
                                            @Param("orderNo") String orderNo);

    List<OrderDetail> listByOrderNo(String orderNo);
}