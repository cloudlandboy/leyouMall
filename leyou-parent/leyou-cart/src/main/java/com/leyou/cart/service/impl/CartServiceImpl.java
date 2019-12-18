package com.leyou.cart.service.impl;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.client.SpecificationClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import com.leyou.common.util.JsonUtils;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpecParam;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author cloudlandboy
 * @Date 2019/12/17 下午4:50
 * @Since 1.0.0
 */

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public static final String KEY_PREFIX = "leyou:cart:uid:";

    @Override
    public void addCart(Cart cart) {

        //获取商品数量
        Integer num = cart.getNum();
        //获取用户信息
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        //获取redis，hash类型的操作对象
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(KEY_PREFIX + loginUser.getId());
        //查看用户购物车中是否已经存在该商品
        if (hashOps.hasKey(cart.getSkuId().toString())) {
            //存在更新数量
            String cartJson = hashOps.get(cart.getSkuId().toString()).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            //修改数量
            cart.setNum(cart.getNum() + num);
        } else {
            //不存在添加
            //先根据skuId查询sku
            Sku sku = goodsClient.querySkuById(cart.getSkuId());
            String[] images = StringUtils.split(sku.getImages(), ",");
            cart.setImage(ArrayUtils.isEmpty(images) ? "" : images[0]);
            //查询特殊的规格参数，目的是在页面能够显示名称
            Map<Long, String> temp = JsonUtils.parseMap(sku.getOwnSpec(), Long.class, String.class);
            List<SpecParam> params = specificationClient.querySpecsByIds(new ArrayList<>(temp.keySet()));
            Map<String, String> paramsMapping = new HashMap<>();
            params.forEach(param -> {
                paramsMapping.put(param.getName(), temp.get(param.getId()));
            });
            cart.setOwnSpec(paramsMapping);
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setUserId(loginUser.getId());
        }

        // 将购物车数据写入redis
        hashOps.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }

    @Override
    public List<Cart> queryCart() {
        // 判断是否存在购物车
        String key = KEY_PREFIX + LoginInterceptor.getLoginUser().getId();
        if (!this.redisTemplate.hasKey(key)) {
            // 不存在，直接返回
            return null;
        }
        //获取购物车数据
        BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(key);
        List<Object> carts = boundHashOps.values();
        // 判断是否有数据
        if (CollectionUtils.isEmpty(carts)) {
            return null;
        }
        //将json转为对象
        return carts.stream().map(cartJson -> {
            return JsonUtils.parse(cartJson.toString(), Cart.class);
        }).collect(Collectors.toList());
    }

    @Override
    public void updateCart(Cart cart) {
        //获取商品数量
        Integer num = cart.getNum();
        //获取用户信息
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        //获取redis，hash类型的操作对象
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(KEY_PREFIX + loginUser.getId());
        //查看用户购物车中是否已经存在该商品
        if (hashOps.hasKey(cart.getSkuId().toString())) {
            //存在更新数量
            String cartJson = hashOps.get(cart.getSkuId().toString()).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            //修改数量
            cart.setNum(num);
            // 将购物车数据写入redis
            hashOps.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
        }
    }

    @Override
    public void deleteCart(String... skuId) {
        // 判断是否存在购物车
        String key = KEY_PREFIX + LoginInterceptor.getLoginUser().getId();
        if (redisTemplate.hasKey(key)) {
            BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(key);
            boundHashOps.delete(skuId);
        }
    }

    @Override
    public void mergeCart(List<Cart> carts) {
        for (Cart cart : carts) {
            this.addCart(cart);
        }
    }
}