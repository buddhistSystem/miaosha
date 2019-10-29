package com.liqian.mall.dao;

import com.liqian.mall.entity.PayInfo;

public interface PayInfoMapper {

    int insertSelective(PayInfo record);

    int updateByPrimaryKeySelective(PayInfo record);


}