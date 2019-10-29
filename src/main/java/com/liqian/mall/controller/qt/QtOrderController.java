package com.liqian.mall.controller.qt;

import com.liqian.mall.common.Const;
import com.liqian.mall.common.Result;
import com.liqian.mall.entity.User;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.IOrderService;
import com.liqian.mall.service.IPayInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 前台订单接口
 *
 * @author liqian
 */
@Controller
@RequestMapping("qt_order")
public class QtOrderController {

    @Resource
    private HttpSession session;

    @Resource
    private HttpServletResponse response;

    @Resource
    private IOrderService iOrderService;

    @Resource
    private IPayInfoService iPayInfoService;

    @RequestMapping("create")
    @ResponseBody
    public Result create(Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iOrderService.create(user.getId(), shippingId);
    }

    @RequestMapping("cancel")
    @ResponseBody
    public Result cancel(String orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iOrderService.cancel(user.getId(), orderNo);
    }


    @RequestMapping("detail")
    @ResponseBody
    public Result detail(String orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iOrderService.detail(user.getId(), orderNo);
    }

    @RequestMapping("list")
    @ResponseBody
    public Result list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iOrderService.list(user.getId(), pageNum, pageSize);
    }

    /**
     * 此接口功能和CartController的list接口相似
     *
     * @return Result
     */
    @RequestMapping("getOrderCartProduct")
    @ResponseBody
    public Result getOrderCartProduct() {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

    @RequestMapping("pay")
    @ResponseBody
    public void pay(String orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        String result = iOrderService.pay(user.getId(), orderNo);
        response.setContentType("text/html; charset=gbk");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }

    @RequestMapping("notifyUrl")
    @ResponseBody
    public Object notify(HttpServletRequest request) {
        try {
            iPayInfoService.add(request);
        } catch (Exception e) {
            e.printStackTrace();
            return Const.AlipayCallBack.RESPONSE_FAILED;
        }
        return Const.AlipayCallBack.RESPONSE_SUCCESS;
    }

}
