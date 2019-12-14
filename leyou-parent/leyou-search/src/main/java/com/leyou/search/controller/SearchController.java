package com.leyou.search.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author cloudlandboy
 * @Date 2019/12/10 下午3:10
 * @Since 1.0.0
 */

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 搜索商品
     *
     * @param searchRequest
     * @return
     */
    @PostMapping("/page")
    public ResponseEntity<PageResult<Goods>> searchGoods(@RequestBody  SearchRequest searchRequest) {
        PageResult<Goods> pageResult = searchService.searchGoods(searchRequest);
        if (pageResult == null || CollectionUtils.isEmpty(pageResult.getItems())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pageResult);
    }
}