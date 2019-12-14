package com.leyou.goods.web.service.impl;

import com.leyou.common.util.ThreadUtils;
import com.leyou.goods.web.service.GoodsHtmlService;
import com.leyou.goods.web.service.GoodsService;
import com.leyou.item.pojo.Spu;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

@Service
public class GoodsHtmlServiceImpl implements GoodsHtmlService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TemplateEngine templateEngine;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public void createHtml(Map<String, Object> model) {
        //创建thymeleaf上下文对象
        Context context = new Context();
        //把数据放入上下文
        context.setVariables(model);
        //获取spu
        Spu spu = (Spu) model.get("spu");

        Writer writer = null;
        try {
            writer = new PrintWriter(new File("/home/cloudlandboy/Project/leyou/html/item/" + spu.getId() + ".html"));
            // 执行页面静态化方法
            templateEngine.process("item", context, writer);
        } catch (FileNotFoundException e) {
            LOGGER.error("页面静态化出错：{}，" + e, model);
        } finally {
            IOUtils.closeQuietly(writer);
        }

    }

    @Override
    public void asyncExcuteCreateHtml(Map<String, Object> model) {
        ThreadUtils.execute(() -> createHtml(model));
    }
}