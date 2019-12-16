package com.leyou.search.listener;

import com.leyou.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author cloudlandboy
 * @Date 2019/12/14 下午8:08
 * @Since 1.0.0
 */

@Component
public class GoodsListener {

    @Autowired
    private SearchService searchService;

    /**
     * 处理insert和update消息
     *
     * @param id
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "LEYOU.CREATE.INDEX.QUEUE", durable = "true"),
                    exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
                    key = {"item.insert", "item.update"}
            )
    )
    public void listenCreate(Long id) throws IOException {
        if (id == null) {
            return;
        }
        // 创建或更新索引
        this.searchService.createIndex(id);
    }

    /**
     * 处理delete消息
     *
     * @param id
     * @throws IOException
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "LEYOU.DELETE.INDEX.QUEUE", durable = "true"),
                    exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
                    key = {"item.delete"}
            )
    )
    public void listenDelete(Long id) throws IOException {
        if (id == null) {
            return;
        }
        // 创建或更新索引
        this.searchService.deleteIndex(id);
    }
}