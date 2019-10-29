package com.liqian.mall.controller.qt;

import com.liqian.mall.common.Const;
import com.liqian.mall.common.Result;
import com.liqian.mall.entity.User;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.ICartService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 前台购物车接口
 *
 * @author liqian
 */
@RestController
@RequestMapping("qt_cart")
public class QtCartController {

    @Resource
    private ICartService iCartService;

    @GetMapping("add")
    public Result add(Integer productId, Integer count, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iCartService.add(user.getId(), productId, count);
    }

    @GetMapping("update")
    public Result update(Integer productId, Integer count, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iCartService.update(user.getId(), productId, count);
    }

    @GetMapping("delete")
    public Result delete(String productIds, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iCartService.delete(user.getId(), productIds);
    }


    @GetMapping("list")
    public Result list(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iCartService.list(user.getId());
    }

    /**
     * 全选
     *
     * @param session session
     * @return Result
     */
    @GetMapping("selectAll")
    public Result selectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.CHECKED, null);
    }

    /**
     * 全反选
     *
     * @param session session
     * @return Result
     */
    @GetMapping("unSelectAll")
    public Result unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.UN_CHECKED, null);
    }

    @GetMapping("select")
    public Result select(Integer productId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.CHECKED, productId);
    }

    @GetMapping("unSelect")
    public Result unSelect(Integer productId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.UN_CHECKED, productId);
    }

    @GetMapping("getCartProductCount")
    public Result getCartProductCount(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Result.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }

}
