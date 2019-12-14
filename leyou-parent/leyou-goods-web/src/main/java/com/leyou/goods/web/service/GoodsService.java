package com.leyou.goods.web.service;

import java.util.Map;

/**
 * @Author cloudlandboy
 * @Date 2019/12/12 下午5:01
 * @Since 1.0.0
 */

public interface GoodsService {

    /**
     * 加载商品数据
     *
     * @param spuId
     * @return
     */
    Map<String, Object> loadData(Long spuId);
}