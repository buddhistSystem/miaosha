package com.liqian.mall.service;

import com.liqian.mall.common.Result;

/**
 * @Author: Administrator
 * @Date: 2019/8/30 0030 10:28
 * @Description:
 */
public interface ICartService {

    Result add(Integer userId, Integer productId, Integer count);

    Result update(Integer userId, Integer productId, Integer count);

    Result delete(Integer userId, String productIds);

    Result list(Integer userId);

    Result selectOrUnSelect(Integer userId, Integer checked, Integer productId);

    Result getCartProductCount(Integer userId);
}
