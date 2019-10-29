package com.liqian.mall.service;

import com.liqian.mall.common.Result;

import java.util.List;

/**
 * 分类
 *
 * @author liqian
 */
public interface ICategoryService {

    /**
     * 新增分类
     *
     * @param categoryName 分类名称
     * @param parentId     父类id
     * @return Result
     */
    Result addCategory(String categoryName, Integer parentId);

    /**
     * 更改分类名称
     *
     * @param categoryId   分类id
     * @param categoryName 分类名称
     * @return Result
     */
    Result updateCategoryName(Integer categoryId, String categoryName);

    /**
     * 查找categoryId下的分类（不递归子分类）
     *
     * @param categoryId 分类id
     * @return Result
     */
    Result listCategory(Integer categoryId);

    /**
     * 查找categoryId下的分类id（递归子分类）
     *
     * @param categoryId 分类id
     * @return Result
     */
    Result<List<Integer>> listDeepCategory(Integer categoryId);
}
