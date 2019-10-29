package com.liqian.mall.service;

import com.liqian.mall.common.Result;

/**
 * @author liqian
 */
public interface IOrderService {

    Result create(Integer userId, Integer shippingId);

    String pay(Integer userId, String orderNo);

    Result cancel(Integer userId, String orderNo);

    Result getOrderCartProduct(Integer userId);

    Result detail(Integer userId, String orderNo);

    Result list(Integer userId, Integer pageNum, Integer pageSize);

    Result htList(Integer pageNum, Integer pageSize);

    Result htDetail(String orderNo);

    Result htSearch(String orderNo,Integer pageNum,Integer pageSize );

    Result htSendGoods(String orderNo);
}
