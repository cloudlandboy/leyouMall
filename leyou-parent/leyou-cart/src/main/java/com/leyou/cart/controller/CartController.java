package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加购物车
     *
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        this.cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }

    /**
     * 查询购物车
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCart() {
        List<Cart> cart = this.cartService.queryCart();
        if (CollectionUtils.isEmpty(cart)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cart);
    }

    /**
     * 修改购物车
     *
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateCart(@RequestBody Cart cart) {
        this.cartService.updateCart(cart);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除购物车
     *
     * @return
     */
    @DeleteMapping("/{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") String... skuId) {
        this.cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }

    /**
     * 合并localStorage和redis中的购物车
     *
     * @param carts
     * @return
     */
    @PostMapping("/merge")
    public ResponseEntity<Void> mergeCart(@RequestBody List<Cart> carts) {
        this.cartService.mergeCart(carts);
        return ResponseEntity.ok().build();
    }
}