//package com.liqian.mall.controller.qt;
//
//import com.alipay.api.AlipayClient;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.request.AlipayTradePagePayRequest;
//import com.liqian.mall.config.AlipayConfig;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.PrintWriter;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//
///**
// * 支付宝网页支付demo
// *
// * @author liqian
// */
//@Controller
//public class AlipayController {
//
//    private static final String TRADE_SUCCESS = "TRADE_SUCCESS";
//
//    /**
//     * 用户调用支付的接口
//     */
//    @RequestMapping("alipaySum")
//    @ResponseBody
//    public void alipayIumpSum(String payables, String subject, String body, HttpServletResponse response)
//            throws Exception {
//        // 获得初始化的AlipayClient
//        AlipayClient alipayClient = new DefaultAlipayClient(
//                AlipayConfig.gatewayUrl,
//                AlipayConfig.app_id,
//                AlipayConfig.merchant_private_key,
//                "json",
//                AlipayConfig.charset,
//                AlipayConfig.alipay_public_key,
//                AlipayConfig.sign_type);
//
//        // 设置请求参数
//        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
//        //返回用户的页面
//        alipayRequest.setReturnUrl(AlipayConfig.return_url);
//        //异步通知商家的地址
//        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
//
//        //商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        String outTradeNo = sdf.format(new Date());
//
//        // 付款金额，必需项
//        String totalAmount = payables.replace(",", "");
//
//        /*
//         * 其他参数：
//         * subject：订单标题 （必填项）
//         * body：   订单描述 （选填）
//         */
//        alipayRequest.setBizContent(
//                "{\"out_trade_no\":\"" + outTradeNo + "\","
//                        + "\"total_amount\":\"" + totalAmount + "\","
//                        + "\"subject\":\"" + subject + "\","
//                        + "\"body\":\"" + body + "\","
//                        + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
//        // 发送请求
//        String result = alipayClient.pageExecute(alipayRequest).getBody();
//        System.out.println(result);
//
//        // 记录支付日志
////        AlipayConfig.logResult(result);
//        response.setContentType("text/html; charset=gbk");
//        PrintWriter out = response.getWriter();
//        out.print(result);
//    }
//
//
//    @RequestMapping("notifyUrl")
//    @ResponseBody
//    public void notify(HttpServletRequest request) throws Exception {
//        System.out.println("success---------------------");
//        // 商户订单号
//        String outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "GBK");
//        // 付款金额
//        String totalAmount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "GBK");
//        // 支付宝交易号
//        String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "GBK");
//        // 交易说明
//        String cus = new String(request.getParameter("body").getBytes("ISO-8859-1"), "GBK");
//        // 交易状态
//        String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "GBK");
//
//        System.out.println("商户订单号:" + outTradeNo + " 付款金额:" + totalAmount + " 支付宝交易号:" + tradeNo + " 交易说明:" + cus);
//        //支付成功商家操作
//        if (TRADE_SUCCESS.equals(tradeStatus)) {
//            System.out.println("回调成功");
//        }
//    }
//
//
//    @RequestMapping("returnUrl")
//    @ResponseBody
//    public String returnUrl() {
//        return "支付成功后用户的页面跳转";
//    }
//
//}
