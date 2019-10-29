package com.liqian.mall.service;

import com.liqian.mall.common.Result;
import com.liqian.mall.entity.Shipping;

/**
 * @author liqian
 */
public interface IShippingService {

    Result add(Integer userId, Shipping shipping);

    Result delete(Integer userId, Integer shippingId);

    Result update(Integer userId, Shipping shipping);

    Result select(Integer userId, Integer shippingId);

    Result list(Integer userId, Integer pageNum, Integer pageSize);
}
