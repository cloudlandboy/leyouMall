package com.leyou.goods.web.service.impl;

import com.leyou.goods.web.client.BrandClient;
import com.leyou.goods.web.client.CategoryClient;
import com.leyou.goods.web.client.GoodsClient;
import com.leyou.goods.web.client.SpecificationClient;
import com.leyou.goods.web.service.GoodsService;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Override
    public Map<String, Object> loadData(Long spuId) {
        //根据商品id查询商品(spu)
        Spu spu = this.goodsClient.querySpuById(spuId);
        //查询商品详情(spuDetail)
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);

        //根据spuId查询其下的所有sku
        List<Sku> skus = this.goodsClient.querySkuBySpuId(spuId);

        // 查询分类
        List<Category> categories = this.categoryClient.queryAllByCid3(spu.getCid3());
        //只需要分类id和分类名称
        List<Map<String, Object>> categoryData = categories.stream().map(category -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", category.getId());
            map.put("name", category.getName());
            return map;
        }).collect(Collectors.toList());

        // 查询品牌
        Brand brand = this.brandClient.queryBrandByid(spu.getBrandId());

        // 查询规格参数组和组下规格参数
       List<SpecGroup> groups = this.specificationClient.querySpecsByCid(spu.getCid3());

        //查询特殊的规格参数获取  id，name
        List<SpecParam> specialParams = this.specificationClient.querySpecParams(null, spu.getCid3(), false, null);

        Map<Long, String> params = new HashMap<>();
        specialParams.forEach(specParam -> {
            params.put(specParam.getId(), specParam.getName());
        });
        //封装商品信息
        Map<String, Object> goodsInfo = new HashMap<>();
        // 封装spu
        goodsInfo.put("spu", spu);
        // 封装spuDetail
        goodsInfo.put("spuDetail", spuDetail);
        // 封装sku集合
        goodsInfo.put("skus", skus);
        // 分类
        goodsInfo.put("categories", categories);
        // 品牌
        goodsInfo.put("brand", brand);
        // 规格参数组
        goodsInfo.put("groups", groups);
        // 查询特殊规格参数
        goodsInfo.put("params", params);

        return goodsInfo;
    }
}