package com.liqian.mall.controller.qt;

import com.liqian.mall.common.Const;
import com.liqian.mall.common.Result;
import com.liqian.mall.entity.Shipping;
import com.liqian.mall.entity.User;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.IShippingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 前台收货地址接口
 *
 * @author liqian
 */
@RestController
@RequestMapping("qt_shipping")
public class QtShippingController {

    @Resource
    private IShippingService iShippingService;

    @Resource
    private HttpSession session;

    @GetMapping("add")
    public Result add(Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iShippingService.add(user.getId(),shipping);
    }

    @GetMapping("delete")
    public Result delete(Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iShippingService.delete(user.getId(),shippingId);
    }

    @GetMapping("update")
    public Result update(Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iShippingService.update(user.getId(),shipping);
    }

    @GetMapping("select")
    public Result select(Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iShippingService.select(user.getId(),shippingId);
    }

    @GetMapping("list")
    public Result list(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iShippingService.list(user.getId(),pageNum,pageSize);
    }
}
