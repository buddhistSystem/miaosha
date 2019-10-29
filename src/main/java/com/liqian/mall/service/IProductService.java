package com.liqian.mall.service;

import com.liqian.mall.common.Result;
import com.liqian.mall.entity.Product;

/**
 * @author liqian
 */
public interface IProductService {

    /**
     * 新增一个商品或者修改一个商品,依据是否传入id
     *
     * @param product 接收参数对象
     * @return Result
     */
    Result addOrUpdateProduct(Product product);

    /**
     * 设置商品的状态（上架、下架、删除）
     *
     * @param productId 商品id
     * @param status    状态
     * @return Result
     */
    Result setProductStatus(Integer productId, Integer status);

    /**
     * 获取后台的商品详情
     *
     * @param productId 商品id
     * @return Result
     */
    Result getHtProductDetail(Integer productId);

    /**
     * 获取后台的商品列表
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return Result
     */
    Result listProduct(Integer pageNum, Integer pageSize);

    /**
     * 商品搜索
     *
     * @param pageNum     页码
     * @param pageSize    每页条数
     * @param productName 商品名（模糊匹配）
     * @param productId   商品id
     * @return Result
     */
    Result searchProduct(Integer pageNum, Integer pageSize, String productName, Integer productId);

    /**
     * 获取前台的商品详情
     *
     * @param productId 商品id
     * @return Result
     */
    Result getQtProductDetail(Integer productId);

    /**
     * 获取前台的商品列表
     *
     * @param keyword    关键字
     * @param categoryId 分类id
     * @param pageNum    页码
     * @param pageSize   每页条数
     * @param orderBy    排序条件
     * @return Result
     */
    Result listQtProduct(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy);
}
