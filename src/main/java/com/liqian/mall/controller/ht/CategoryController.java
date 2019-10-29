package com.liqian.mall.controller.ht;

import com.liqian.mall.common.Const;
import com.liqian.mall.common.Result;
import com.liqian.mall.entity.User;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.ICategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 后台商品分类接口（已测）
 *
 * @author liqian
 */
@RestController
@RequestMapping("ht_category")
public class CategoryController {

    @Resource
    private ICategoryService iCategoryService;

    @Resource
    private HttpSession session;

    @GetMapping("add")
    public Result addCategory(String categoryName,
                              @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new BusinessException(EmError.NOT_PERMISSION);
        }
        return iCategoryService.addCategory(categoryName, parentId);
    }

    @GetMapping("updateCategoryName")
    public Result updateCategoryName(Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new BusinessException(EmError.NOT_PERMISSION);
        }
        return iCategoryService.updateCategoryName(categoryId, categoryName);
    }

    @GetMapping("list")
    public Result listCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new BusinessException(EmError.NOT_PERMISSION);
        }
        return iCategoryService.listCategory(categoryId);
    }

    @GetMapping("listDeep")
    public Result listDeepCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new BusinessException(EmError.NOT_PERMISSION);
        }
        return iCategoryService.listDeepCategory(categoryId);
    }


}
