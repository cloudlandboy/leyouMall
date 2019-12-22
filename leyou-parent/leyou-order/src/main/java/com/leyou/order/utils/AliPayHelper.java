package com.leyou.order.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.leyou.common.util.JsonUtils;
import com.leyou.order.config.AlipayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author cloudlandboy
 * @Date 2019/12/21 下午5:57
 * @Since 1.0.0
 */

@Component
public class AliPayHelper {

    @Autowired
    private AlipayProperties properties;

    /**
     * @param orderId
     * @param actualPay
     * @return
     * @throws
     */
    public String pay(String orderId, String actualPay) throws Exception {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(properties.getGatewayUrl(), properties.getAppId(), properties.getPrivateKey(),
                "json", properties.getCharset(), properties.getAlipayPublicKey(), properties.getSignType());

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(properties.getReturnUrl());
        alipayRequest.setNotifyUrl(properties.getNotifyUrl());

        //请求参数
        Map<String, String> data = new HashMap<>(5);
        //商户订单号，商户网站订单系统中唯一订单号，必填
        data.put("out_trade_no", orderId);
        //付款金额，必填
        data.put("total_amount", actualPay);
        //订单名称，必填
        data.put("subject", "乐优商城测试订单");
        //商品描述，可空
        data.put("body", "乐优商城的订单");
        //该数据不要修改
        data.put("product_code", "FAST_INSTANT_TRADE_PAY");
        alipayRequest.setBizContent(JsonUtils.serialize(data));
        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //返回的是一个html源码，里面是个表单然后用js提交的
        return result;
    }


    /**
     * 调用SDK验证签名
     *
     * @param params
     * @return
     * @throws AlipayApiException
     */
    public boolean rsaCheckV1(Map<String, String> params) throws AlipayApiException {
        boolean signVerified = AlipaySignature.rsaCheckV1(params, properties.getAlipayPublicKey(), properties.getCharset(), properties.getSignType());
        return signVerified;
    }

    /**
     * 交易状态
     */
    public static class TradeStatus {
        /**
         * 交易关闭 0
         */
        public static final String TRADE_CLOSED = "TRADE_CLOSED";
        /**
         * 交易完结 0
         */
        public static final String TRADE_FINISHED = "TRADE_FINISHED";
        /**
         * 支付成功	1
         */
        public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
        /**
         * 交易创建	0
         */
        public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
    }
}