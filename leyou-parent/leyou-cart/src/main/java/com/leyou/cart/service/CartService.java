package com.leyou.cart.service;

import com.leyou.cart.pojo.Cart;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/17 下午4:50
 * @Since 1.0.0
 */

public interface CartService {

    /**
     * 添加购物车
     *
     * @param cart
     */
    void addCart(Cart cart);

    /**
     * 查询购物车
     *
     * @return
     */
    List<Cart> queryCart();

    /**
     * 修改购物车l
     *
     * @param cart
     */
    void updateCart(Cart cart);

    /**
     * 删除购物车
     *
     * @param skuId
     */
    void deleteCart(String... skuId);

    /**
     * 合并localStorage和redis中的购物车
     *
     * @param carts
     * @return
     */
    void mergeCart(List<Cart> carts);
}