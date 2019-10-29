package com.liqian.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liqian.mall.common.Result;
import com.liqian.mall.dao.ShippingMapper;
import com.liqian.mall.entity.Shipping;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.IShippingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liqian
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Resource
    private ShippingMapper shippingMapper;

    @Override
    public Result add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        shippingMapper.insertSelective(shipping);
        Map<String, Integer> map = new HashMap<>(4);
        map.put("shippingId", shipping.getId());
        return Result.createBySuccess(map);
    }

    @Override
    public Result delete(Integer userId, Integer shippingId) {
        shippingMapper.deleteByUserIdAndShippingId(userId, shippingId);
        return Result.createBySuccess();
    }

    @Override
    public Result update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        shippingMapper.updateByShipping(shipping);
        return Result.createBySuccess();
    }

    @Override
    public Result select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId, shippingId);
        if (shipping == null) {
            throw new BusinessException(EmError.SHIPPING_NOT_EXIST);
        }
        return Result.createBySuccess(shipping);
    }

    @Override
    public Result list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> list = shippingMapper.listByUserId(userId);
        PageInfo pageInfo = new PageInfo(list);
        return Result.createBySuccess(pageInfo);
    }
}

