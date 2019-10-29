package com.liqian.mall.service.impl;

import com.google.common.base.Splitter;
import com.liqian.mall.common.Const;
import com.liqian.mall.common.Result;
import com.liqian.mall.config.FtpConfig;
import com.liqian.mall.dao.CartMapper;
import com.liqian.mall.dao.ProductMapper;
import com.liqian.mall.entity.Cart;
import com.liqian.mall.entity.Product;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.ICartService;
import com.liqian.mall.vo.CartProductVo;
import com.liqian.mall.vo.CartVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liqian
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private FtpConfig ftpConfig;

    @Override
    public Result add(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            throw new BusinessException(EmError.PARAMETER_VALIDATION_ERROR);
        }
        Cart cart = cartMapper.getCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //记录不存在
            Cart cartItem = new Cart();
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setQuantity(count);
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartMapper.insertSelective(cartItem);
        } else {
            //产品已存在，数量相加
            cart.setQuantity(cart.getQuantity() + count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return list(userId);

    }

    @Override
    public Result update(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            throw new BusinessException(EmError.PARAMETER_VALIDATION_ERROR);
        }
        Cart cart = cartMapper.getCartByUserIdAndProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return list(userId);
    }

    @Override
    public Result delete(Integer userId, String productIds) {
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if (productIdList.isEmpty()) {
            throw new BusinessException(EmError.PARAMETER_VALIDATION_ERROR);
        }
        cartMapper.deleteByUserIdAndProductIds(userId, productIdList);
        return list(userId);
    }

    @Override
    public Result list(Integer userId) {
        CartVo cartVo = getCartVoLimit(userId);
        return Result.createBySuccess(cartVo);
    }

    @Override
    public Result selectOrUnSelect(Integer userId, Integer checked, Integer productId) {
        cartMapper.checkedOrUnCheckedProduct(userId, checked, productId);
        return list(userId);
    }

    @Override
    public Result getCartProductCount(Integer userId) {
        int count = cartMapper.getCartProductCount(userId);
        return Result.createBySuccess(count);
    }

    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.listCartByUserId(userId);
        List<CartProductVo> cartProductVoList = new ArrayList<>();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        if (!CollectionUtils.isEmpty(cartList)) {
            for (Cart cart : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cart.getId());
                cartProductVo.setUserId(cart.getUserId());
                cartProductVo.setProductId(cart.getProductId());

                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if (product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubTitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存
                    int buyLimitCount;
                    if (product.getStock() >= cart.getQuantity()) {
                        //库存充足时
                        buyLimitCount = cart.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cart.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算购物车总价
                    cartProductVo.setProductTotalPrice(product.getPrice().multiply(new BigDecimal(cartProductVo.getQuantity().toString())));
                    cartProductVo.setChecked(cart.getChecked());
                }

                if (cart.getChecked() == Const.Cart.CHECKED) {
                    //勾选增加到整个购物车总价
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductPrice());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(getAllCheckedStatus(userId));
        cartVo.setImageHost(ftpConfig.getImageHost());
        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.getCartProductCheckedStatusByUserId(userId) == 0;
    }

}
