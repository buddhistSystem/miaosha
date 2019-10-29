package com.liqian.mall.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.liqian.mall.common.Result;
import com.liqian.mall.dao.CategoryMapper;
import com.liqian.mall.entity.Category;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.ICategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author liqian
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    @Resource
    private CategoryMapper categoryMapper;


    @Override
    public Result addCategory(String categoryName, Integer parentId) {
        if (StringUtils.isEmpty(categoryName)) {
            throw new BusinessException(EmError.CATEGORY_NAME_NOT_NULL);
        }
        if (parentId != 0) {
            Category existCategory = categoryMapper.selectByPrimaryKey(parentId);
            if (existCategory == null) {
                throw new BusinessException(EmError.CATEGORY_PARENT_NOT_EXIST);
            }
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setCreateTime(new Date());
        categoryMapper.insertSelective(category);
        return Result.createBySuccess();
    }

    @Override
    public Result updateCategoryName(Integer categoryId, String categoryName) {
        if (StringUtils.isEmpty(categoryName)) {
            throw new BusinessException(EmError.CATEGORY_NAME_NOT_NULL);
        }
        if (categoryId == null) {
            throw new BusinessException(EmError.CATEGORY_ID_NOT_NULL);
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        categoryMapper.updateByPrimaryKeySelective(category);
        return Result.createBySuccess();
    }

    @Override
    public Result listCategory(Integer categoryId) {
        List<Category> list = categoryMapper.listCategoryByParentId(categoryId);
        return Result.createBySuccess(list);
    }

    @Override
    public Result<List<Integer>> listDeepCategory(Integer categoryId) {
        Set<Category> set = new HashSet<>();
        findChildCategory(set, categoryId);
        List<Integer> categoryIdList = new ArrayList<>();
        for (Category category : set) {
            categoryIdList.add(category.getId());
        }
        return Result.createBySuccess(categoryIdList);
    }


    /**
     * 递归获取分类的所有子分类id
     *
     * @param categorySet 存放所有分类id
     * @param categoryId  分类id
     * @return categorySet
     */
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        List<Category> list = categoryMapper.listCategoryByParentId(categoryId);
        for (Category c : list) {
            findChildCategory(categorySet, c.getId());
        }
        return categorySet;
    }

}
