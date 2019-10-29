package com.liqian.mall.service.impl;

import com.liqian.mall.common.Const;
import com.liqian.mall.dao.OrderMapper;
import com.liqian.mall.dao.PayInfoMapper;
import com.liqian.mall.entity.Order;
import com.liqian.mall.entity.PayInfo;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.IPayInfoService;
import com.liqian.mall.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 支付信息
 *
 * @author liqian
 */
@Service("iPayInfoService")
@Slf4j
public class PayInfoServiceImpl implements IPayInfoService {

    @Resource
    private PayInfoMapper payInfoMapper;

    @Resource
    private OrderMapper orderMapper;

    @Override
    public void add(HttpServletRequest request) throws UnsupportedEncodingException, ParseException {

        // 商户订单号
        String outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "GBK");
        // 付款金额
        String totalAmount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "GBK");
        // 支付宝交易号
        String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "GBK");
        // 交易说明
        //String cus = new String(request.getParameter("body").getBytes("ISO-8859-1"), "GBK");
        // 交易状态
        String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "GBK");
        // 支付时间
        String paymentTime = new String(request.getParameter("gmt_payment").getBytes("ISO-8859-1"), "GBK");
        log.info("商户订单号:" + outTradeNo + " 付款金额:" + totalAmount + " 支付宝交易号:" + tradeNo + " 支付时间：" + paymentTime);
        Order order = orderMapper.selectByOrderNo(outTradeNo);
        if (order == null) {
            throw new BusinessException(EmError.ORDER_NOT_EXIST);
        }
        if (order.getStatus() >= Const.OrderStatus.PAID.getCode()) {
            //订单状态是已付款不再处理
            return;
        }
        //支付成功商家操作
        if (Const.AlipayCallBack.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
            log.info("交易成功,修改订单状态");
            order.setStatus(Const.OrderStatus.PAID.getCode());
            SimpleDateFormat sdf = DateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss");
            order.setPaymentTime(sdf.parse(paymentTime));
            orderMapper.updateByPrimaryKeySelective(order);
        }
        PayInfo payInfo = new PayInfo();
        payInfo.setOrderNo(outTradeNo);
        payInfo.setUserId(order.getUserId());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPayPlatfrom(Const.PayPlatfrom.ALIPAY.getCode());
        payInfo.setPlatformStatus(Const.AlipayCallBack.TRADE_STATUS_TRADE_SUCCESS);

        payInfoMapper.insertSelective(payInfo);
    }
}
