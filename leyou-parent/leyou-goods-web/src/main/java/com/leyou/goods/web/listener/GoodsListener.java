package com.leyou.goods.web.listener;

import com.leyou.goods.web.service.GoodsHtmlService;
import com.leyou.goods.web.service.GoodsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GoodsListener {

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @Autowired
    private GoodsService goodsService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "LEYOU.CREATE.WEB.QUEUE", durable = "true"),
                    exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
                    key = {"item.insert", "item.update"}
            )
    )
    public void listenCreate(Long id) throws Exception {
        if (id == null) {
            return;
        }

        //获取数据
        Map<String, Object> data = goodsService.loadData(id);
        // 创建页面
        goodsHtmlService.createHtml(data);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "LEYOU.DELETE.WEB.QUEUE", durable = "true"),
                    exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
                    key = {"item.delete"}
            )
    )
    public void listenDelete(Long id) {
        if (id == null) {
            return;
        }
        // 删除页面
        goodsHtmlService.deleteHtml(id);
    }
}