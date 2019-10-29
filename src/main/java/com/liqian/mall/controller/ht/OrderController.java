package com.liqian.mall.controller.ht;

import com.liqian.mall.common.Const;
import com.liqian.mall.common.Result;
import com.liqian.mall.entity.User;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.IOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 后台订单接口
 *
 * @author liqan
 */
@RestController
@RequestMapping("ht_order")
public class OrderController {

    @Resource
    private HttpSession session;

    @Resource
    private IOrderService iOrderService;

    @RequestMapping("list")
    public Result list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new BusinessException(EmError.NOT_PERMISSION);
        }
        return iOrderService.htList(pageNum, pageSize);
    }

    @RequestMapping("detail")
    public Result detail(String orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new BusinessException(EmError.NOT_PERMISSION);
        }
        return iOrderService.htDetail(orderNo);
    }

    @RequestMapping("search")
    public Result search(String orderNo,
                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new BusinessException(EmError.NOT_PERMISSION);
        }
        return iOrderService.htSearch(orderNo, pageNum, pageSize);
    }

    @RequestMapping("sendGoods")
    public Result sendGoods(String orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new BusinessException(EmError.NOT_PERMISSION);
        }
        return iOrderService.htSendGoods(orderNo);
    }

}
