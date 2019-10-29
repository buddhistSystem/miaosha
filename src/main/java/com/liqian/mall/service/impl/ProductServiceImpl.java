package com.liqian.mall.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liqian.mall.common.Const;
import com.liqian.mall.common.Result;
import com.liqian.mall.config.FtpConfig;
import com.liqian.mall.dao.CategoryMapper;
import com.liqian.mall.dao.ProductMapper;
import com.liqian.mall.entity.Category;
import com.liqian.mall.entity.Product;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.ICategoryService;
import com.liqian.mall.service.IProductService;
import com.liqian.mall.vo.ProductDetailVo;
import com.liqian.mall.vo.ProductListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liqian
 */
@Service("iProductService")
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private FtpConfig ftpConfig;

    @Resource
    private ICategoryService iCategoryService;

    @Override
    public Result addOrUpdateProduct(Product product) {
        if (product == null) {
            log.info("[新增或更新的product为空]");
            throw new BusinessException(EmError.PARAMETER_VALIDATION_ERROR);
        } else {
            if (!StringUtils.isEmpty(product.getSubImages())) {
                String[] subImages = product.getSubImages().split(",");
                product.setMainImage(subImages[0]);
            }
            if (product.getId() == null) {
                //新增
                productMapper.insertSelective(product);
            } else {
                //更新
                productMapper.updateByPrimaryKeySelective(product);
            }
        }
        return Result.createBySuccess();
    }

    @Override
    public Result setProductStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            log.info("[修改获取上下架参数错误,productId={},status={}]", productId, status);
            throw new BusinessException(EmError.PARAMETER_VALIDATION_ERROR);
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        productMapper.updateByPrimaryKeySelective(product);
        return Result.createBySuccess();
    }

    @Override
    public Result getHtProductDetail(Integer productId) {
        if (productId == null) {
            log.info("[获取商品详情,productId为空]");
            throw new BusinessException(EmError.PARAMETER_VALIDATION_ERROR);
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            throw new BusinessException(EmError.PRODUCT_NOT_EXIST);
        }
        ProductDetailVo productVo = changeProduct2ProductDetailVo(product);
        return Result.createBySuccess(productVo);
    }

    @Override
    public Result listProduct(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.listProduct();
        List<ProductListVo> productListVoList = new ArrayList<>();
        for (Product product : productList) {
            ProductListVo productListVo = changeProduct2ProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return Result.createBySuccess(pageInfo);
    }

    @Override
    public Result searchProduct(Integer pageNum, Integer pageSize, String productName, Integer productId) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.searchProduct(productName, productId);
        List<ProductListVo> productListVoList = new ArrayList<>();
        for (Product product : productList) {
            ProductListVo productListVo = changeProduct2ProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return Result.createBySuccess(pageInfo);
    }

    @Override
    public Result getQtProductDetail(Integer productId) {
        if (productId == null) {
            log.info("[获取商品详情,productId为空]");
            throw new BusinessException(EmError.PARAMETER_VALIDATION_ERROR);
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            throw new BusinessException(EmError.PRODUCT_NOT_EXIST);
        }
        if (product.getStatus() != Const.ProductStatus.PRODUCT_ON_SALE.getCode()) {
            throw new BusinessException(EmError.PRODUCT_NOT_ON_SALE);
        }
        ProductDetailVo productVo = changeProduct2ProductDetailVo(product);
        return Result.createBySuccess(productVo);
    }

    @Override
    public Result listQtProduct(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy) {
        if (StringUtils.isEmpty(keyword) && categoryId == null) {
            throw new BusinessException(EmError.PARAMETER_VALIDATION_ERROR);
        }
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            //没有该分类并且关键字为空，则返回一个空结果集
            if (category == null && StringUtils.isEmpty(keyword)) {
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = new ArrayList<>();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return Result.createBySuccess(pageInfo);
            }
        }
        List<Integer> categoryIdList = iCategoryService.listDeepCategory(categoryId).getData();
        //排序
        PageHelper.startPage(pageNum, pageSize);
        if (!StringUtils.isEmpty(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + "" + orderByArray[1]);
            }
        }
        //分别判断一下参数是否为空
        keyword = StringUtils.isEmpty(keyword) ? null : keyword;
        categoryIdList = categoryIdList.size() == 0 ? null : categoryIdList;
        List<Product> productList = productMapper.listProductByNameAndCategoryIds(keyword, categoryIdList);

        List<ProductListVo> productListVoList = new ArrayList<>();
        productList.forEach(product -> {
            productListVoList.add(changeProduct2ProductListVo(product));
        });

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return Result.createBySuccess(pageInfo);
    }


    private ProductDetailVo changeProduct2ProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product, productDetailVo);
        productDetailVo.setImageHost(ftpConfig.getImageHost());
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        return productDetailVo;
    }

    private ProductListVo changeProduct2ProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        BeanUtils.copyProperties(product, productListVo);
        productListVo.setImageHost(ftpConfig.getImageHost());
        return productListVo;
    }
}
