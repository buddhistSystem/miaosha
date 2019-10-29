package com.liqian.mall.dao;

import com.liqian.mall.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> listProduct();

    List<Product> searchProduct(@Param("productName") String productName,
                                @Param("productId") Integer productId);

    List<Product> listProductByNameAndCategoryIds(@Param("productName") String productName,
                                                  @Param("categoryIds") List<Integer> categoryIds);
}