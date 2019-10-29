package com.liqian.mall.controller.qt;

import com.liqian.mall.common.Result;
import com.liqian.mall.service.IProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 前台商品接口
 *
 * @author liqna
 */
@RestController
@RequestMapping("qt_product")
public class QtProductController {

    @Resource
    private IProductService iProductService;

    @GetMapping("getDetailById")
    public Result detail(Integer productId) {
        return iProductService.getQtProductDetail(productId);
    }

    @GetMapping("list")
    public Result list(String keyword, Integer categoryId,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                       @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return iProductService.listQtProduct(keyword, categoryId, pageNum, pageSize, orderBy);
    }

}
