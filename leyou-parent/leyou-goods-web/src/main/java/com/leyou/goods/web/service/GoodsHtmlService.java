package com.leyou.goods.web.service;

import java.util.Map;

/**
 * @Author cloudlandboy
 * @Date 2019/12/14 上午10:36
 * @Since 1.0.0
 */

public interface GoodsHtmlService {

    /**
     * 创建商品详情html页面
     *
     * @param model
     */
    void createHtml(Map<String, Object> model);

    /**
     * 新建线程处理页面静态化
     *
     * @param model
     */
    void asyncExcuteCreateHtml(Map<String, Object> model);
}