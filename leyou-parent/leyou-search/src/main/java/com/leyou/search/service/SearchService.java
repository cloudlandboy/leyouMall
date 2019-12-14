package com.leyou.search.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;

/**
 * @Author cloudlandboy
 * @Date 2019/12/9 下午7:35
 * @Since 1.0.0
 */

public interface SearchService {

    /**
     * 构建商品查询数据
     *
     * @param spu
     * @return
     */
    Goods buildGoods(Spu spu) throws Exception;

    /**
     * 搜索商品
     * @param searchRequest
     * @return
     */
    PageResult<Goods> searchGoods(SearchRequest searchRequest);
}