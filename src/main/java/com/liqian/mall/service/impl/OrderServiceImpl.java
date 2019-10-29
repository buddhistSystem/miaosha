package com.liqian.mall.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.OrderItem;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liqian.mall.common.Const;
import com.liqian.mall.common.Result;
import com.liqian.mall.config.AlipayConfig;
import com.liqian.mall.config.FtpConfig;
import com.liqian.mall.dao.*;
import com.liqian.mall.entity.*;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.IOrderService;
import com.liqian.mall.util.DateUtil;
import com.liqian.mall.util.UUIdUtil;
import com.liqian.mall.vo.OrderDetailVo;
import com.liqian.mall.vo.OrderProductVo;
import com.liqian.mall.vo.OrderVo;
import com.liqian.mall.vo.ShippingVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liqian
 */
@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private CartMapper cartMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Resource
    private ShippingMapper shippingMapper;

    @Resource
    private FtpConfig ftpConfig;

    @Override
    public Result create(Integer userId, Integer shippingId) {
        if (shippingId == null) {
            throw new BusinessException(EmError.SHIPPING_ID_NOT_NULL);
        }
        //1.获取购物车被勾选的数据
        List<Cart> cartList = cartMapper.listCheckedCartByUserId(userId);
        //2.生成订单详情并计算这个大订单的总价
        List<OrderDetail> orderDetailList = getCartOrderDetail(userId, cartList);
        BigDecimal totalPrice = getOrderTotalPrice(orderDetailList);
        //3.生成订单
        Order order = assembleOrder(userId, shippingId, totalPrice);
        //4.更新订单详情中的订单号
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetail.setOrderNo(order.getOrderNo());
        }
        //5.批量插入订单详情
        orderDetailMapper.insertOrderDetailList(orderDetailList);
        //6.生成成功，减少库存
        reduceProductStock(orderDetailList);
        //7.清空购物车
        clearCart(cartList);
        //8.组装返回给前端的orderVo对象
        OrderVo orderVo = assembleOrderVo(order, orderDetailList);
        return Result.createBySuccess(orderVo);
    }

    /**
     * 组装返回给前端的orderVo对象
     *
     * @param order           订单对象
     * @param orderDetailList 订单详情列表
     */
    private OrderVo assembleOrderVo(Order order, List<OrderDetail> orderDetailList) {
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setPaymentTypeDesc(Const.PaymentType.codeOf(order.getPaymentType()).getValue());
        orderVo.setStatusDesc(Const.OrderStatus.codeOf(order.getStatus()).getStatus());

        orderVo.setShippingId(order.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if (shipping != null) {
            orderVo.setReceiveName(shipping.getReceiveName());
            ShippingVo shippingVo = new ShippingVo();
            BeanUtils.copyProperties(shipping, shippingVo);
            orderVo.setShippingVo(shippingVo);
        }
        orderVo.setPaymentTime(order.getPaymentTime());
        orderVo.setSendTime(order.getSendTime());
        orderVo.setEndTime(order.getEndTime());
        orderVo.setCloseTime(order.getCloseTime());
        orderVo.setCreateTime(order.getCreateTime());

        List<OrderDetailVo> orderDetailVoList = new ArrayList<>();
        //Date now = new Date();
        for (OrderDetail orderDetail : orderDetailList) {
            OrderDetailVo orderDetailVo = new OrderDetailVo();
            BeanUtils.copyProperties(orderDetail, orderDetailVo);
            //orderDetailVo.setCreateTime(now);
            orderDetailVoList.add(orderDetailVo);
        }
        orderVo.setOrderDetailVoList(orderDetailVoList);

        orderVo.setImageHost(ftpConfig.getImageHost());
        return orderVo;
    }


    /**
     * 清空购物车已选中的商品
     *
     * @param list 购物车列表
     */
    private void clearCart(List<Cart> list) {
        for (Cart cart : list) {
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }

    /**
     * 减少对应产品库存
     *
     * @param orderDetailList 订单详情list
     */
    private void reduceProductStock(List<OrderDetail> orderDetailList) {
        for (OrderDetail orderDetail : orderDetailList) {
            Product product = productMapper.selectByPrimaryKey(orderDetail.getProductId());
            product.setStock(product.getStock() - orderDetail.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }

    /**
     * 拼装订单对象
     *
     * @param userId     用户id
     * @param shippingId 地址id
     * @param totalPrice 订单总价
     * @return order 订单对象
     */
    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal totalPrice) {
        Order order = new Order();
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setOrderNo(UUIdUtil.getUUID());
        order.setStatus(Const.OrderStatus.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(Const.PaymentType.ONLINE_PAY.getCode());
        order.setPayment(totalPrice);
        orderMapper.insertSelective(order);
        return order;
    }

    /**
     * 求订单总价
     *
     * @param list 订单详情list
     * @return totalPrice 大订单总价
     */
    private BigDecimal getOrderTotalPrice(List<OrderDetail> list) {
        BigDecimal totalPrice = new BigDecimal("0");
        for (OrderDetail orderDetail : list) {
            totalPrice = totalPrice.add(orderDetail.getTotalPrice());
        }
        return totalPrice;
    }

    /**
     * 根据购物车创建订单详情对象
     *
     * @param userId   用户id
     * @param cartList 该用户购物车list
     * @return orderDetailList 订单详情list
     */
    private List<OrderDetail> getCartOrderDetail(Integer userId, List<Cart> cartList) {
        List<OrderDetail> orderDetailList = new ArrayList<>();
        if (CollectionUtils.isEmpty(cartList)) {
            throw new BusinessException(EmError.CART_NULL);
        }
        //校验购物车数据，包括产品状态数量
        for (Cart cart : cartList) {
            OrderDetail orderDetail = new OrderDetail();
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if (Const.ProductStatus.PRODUCT_ON_SALE.getCode() != product.getStatus()) {
                throw new BusinessException(EmError.PRODUCT_NOT_ON_SALE);
            }
            //校验库存
            if (cart.getQuantity() > product.getStock()) {
                throw new BusinessException(EmError.PRODUCT_STOCK_NOT_WORTH);
            }
            orderDetail.setUserId(userId);
            orderDetail.setProductId(product.getId());
            orderDetail.setProductName(product.getName());
            orderDetail.setProductImage(product.getMainImage());
            orderDetail.setCurrentUnitPrice(product.getPrice());
            orderDetail.setQuantity(cart.getQuantity());
            orderDetail.setTotalPrice(product.getPrice().multiply(new BigDecimal(cart.getQuantity().toString())));
            orderDetailList.add(orderDetail);
        }
        return orderDetailList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public String pay(Integer userId, String orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            throw new BusinessException(EmError.ORDER_NOT_EXIST);
        }
        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.gatewayUrl, AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json",
                AlipayConfig.charset, AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);
        // 设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //返回用户的页面
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        //异步通知商家的地址
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
        //商户订单号,64个字符以内、可包含字母、数字、下划线,需保证在商户端不重复
        String outTradeNo = order.getOrderNo();
        // 付款金额，必需项
        String totalAmount = order.getPayment().toString();
        //订单标题 （必填项）
        String subject = orderNo;
        //超时时间
        String timeoutExpress = "120m";

        alipayRequest.setBizContent(
                "{\"out_trade_no\":\"" + outTradeNo + "\","
                        + "\"total_amount\":\"" + totalAmount + "\","
                        + "\"subject\":\"" + subject + "\","
                        //+ "\"body\":\"" + body + "\","
                        + "\"timeout_express\":\"" + timeoutExpress + "\","
                        + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        // 发送请求
        String result = null;
        try {
            result = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }

    @Override
    public Result cancel(Integer userId, String orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            throw new BusinessException(EmError.ORDER_NOT_EXIST);
        }
        if (order.getStatus() != Const.OrderStatus.NO_PAY.getCode()) {
            throw new BusinessException(EmError.ORDER_IS_PAY);
        }
        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(Const.OrderStatus.CANCEL.getCode());
        orderMapper.updateByPrimaryKeySelective(updateOrder);
        return Result.createBySuccess();
    }


    @Override
    public Result getOrderCartProduct(Integer userId) {
        OrderProductVo orderProductVo = new OrderProductVo();
        List<Cart> cartList = cartMapper.listCheckedCartByUserId(userId);
        //根据购物车创建订单详情对象
        List<OrderDetail> orderDetailList = getCartOrderDetail(userId, cartList);
        List<OrderDetailVo> orderDetailVoList = new ArrayList<>();
        //初始化总价
        BigDecimal totalPrice = new BigDecimal("0");

        for (OrderDetail orderDetail : orderDetailList) {
            totalPrice = totalPrice.add(orderDetail.getTotalPrice());
            OrderDetailVo orderDetailVo = new OrderDetailVo();
            BeanUtils.copyProperties(orderDetail, orderDetailVo);
            orderDetailVoList.add(orderDetailVo);
        }
        orderProductVo.setProductTotalPrice(totalPrice);
        orderProductVo.setOrderDetailVoList(orderDetailVoList);
        orderProductVo.setImageHost(ftpConfig.getImageHost());
        return Result.createBySuccess(orderProductVo);
    }

    @Override
    public Result detail(Integer userId, String orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            throw new BusinessException(EmError.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailMapper.listByUserIdAndOrderNo(userId, orderNo);
        OrderVo orderVo = assembleOrderVo(order, orderDetailList);
        return Result.createBySuccess(orderVo);
    }

    @Override
    public Result list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.listByUserId(userId);
        List<OrderVo> orderVoList = assembleOrderVoList(orderList, userId);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVoList);
        return Result.createBySuccess(pageInfo);
    }


    //后台----------------------------------------------------------


    @Override
    public Result htList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.listAll();
        List<OrderVo> orderVoList = assembleOrderVoList(orderList, null);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVoList);
        return Result.createBySuccess(pageInfo);
    }

    @Override
    public Result htDetail(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(EmError.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailMapper.listByOrderNo(orderNo);
        OrderVo orderVo = assembleOrderVo(order, orderDetailList);
        return Result.createBySuccess(orderVo);
    }

    @Override
    public Result htSearch(String orderNo, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        //现在做精确匹配，以后模糊查询会查出多条，所以需要分页信息
        Order order = orderMapper.selectByOrderNo(orderNo);
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        if (order == null) {
            throw new BusinessException(EmError.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailMapper.listByOrderNo(orderNo);
        OrderVo orderVo = assembleOrderVo(order, orderDetailList);
        List<OrderVo> orderVoList = new ArrayList<>();
        orderVoList.add(orderVo);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVoList);
        return Result.createBySuccess(pageInfo);
    }

    @Override
    public Result htSendGoods(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(EmError.ORDER_NOT_EXIST);
        }
        if (order.getStatus() == Const.OrderStatus.PAID.getCode()) {
            order.setStatus(Const.OrderStatus.SHIPPED.getCode());
            order.setSendTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }
        return Result.createBySuccess();
    }

    private List<OrderVo> assembleOrderVoList(List<Order> orderList, Integer userId) {
        List<OrderVo> orderVoList = new ArrayList<>();
        for (Order order : orderList) {
            List<OrderDetail> orderDetailList;
            if (userId == null) {
                //管理员查询
                orderDetailList = orderDetailMapper.listByOrderNo(order.getOrderNo());
            } else {
                orderDetailList = orderDetailMapper.listByUserIdAndOrderNo(userId, order.getOrderNo());
            }
            OrderVo orderVo = assembleOrderVo(order, orderDetailList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

}
