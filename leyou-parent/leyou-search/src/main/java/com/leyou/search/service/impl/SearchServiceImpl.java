package com.leyou.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author cloudlandboy
 * @Date 2019/12/9 下午7:39
 * @Since 1.0.0
 */

@Component
public class SearchServiceImpl implements SearchService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    private static ObjectMapper MAPPER = new ObjectMapper();


    @Override
    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();

        //-------根据分类id查询分类名称-------
        List<String> categoryNames = categoryClient.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //根据品牌id去查询品牌名称
        String brandName = this.brandClient.queryBrandByid(spu.getBrandId()).getName();

        //-------根据spu的id查询所有的sku-------
        List<Sku> skus = this.goodsClient.querySkuBySpuId(spu.getId());
        //所有sku价格集合
        List<Long> skuPriceList = new ArrayList<>();
        //需要的sku数据
        List<Map<String, Object>> skuInfoList = new ArrayList<>();
        skus.forEach(sku -> {
            Map<String, Object> skuInfo = new HashMap<>();
            skuPriceList.add(sku.getPrice());
            skuInfo.put("id", sku.getId());
            skuInfo.put("title", sku.getTitle());
            skuInfo.put("price", sku.getPrice());
            //获取一张图片
            skuInfo.put("image", StringUtils.isNotBlank(sku.getImages()) ? StringUtils.split(sku.getImages(), ",")[0] : "");
            skuInfoList.add(skuInfo);
        });

        //-------根据分类获取可搜索的规格参数集合-------
        List<SpecParam> specParams = specificationClient.querySpecParams(null, spu.getCid3(), null, true);
        //查询SpuDetail
        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(spu.getId());
        //从SpuDetail中获取通用的规格参数
        Map<String, Object> GenericSpecs = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {
        });
        //从SpuDetail中获取特殊的规格参数 eg: {"4":["玫瑰金","金色","黑色"],"12":["3GB","4GB"],"13":["32GB"]}
        Map<String, List> specialSpecs = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List>>() {
        });

        //可搜索的规格参数，key是参数名，值是参数值
        Map<String, Object> specs = new HashMap<>();
        specParams.forEach(specParam -> {
            //判断是通用还是特殊参数
            if (specParam.getGeneric()) {
                Object val = GenericSpecs.get(specParam.getId().toString());
                //判断是否是数字，是数字则转为区间
                if (specParam.getNumeric()) {
                    val = this.chooseSegment(val.toString(), specParam);
                }
                // 把参数名和值放入结果集中
                specs.put(specParam.getName(), val);
            } else {
                specs.put(specParam.getName(), specialSpecs.get(specParam.getId().toString()));
            }

        });

        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(goods.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        //所有需要被搜索的信息，包含标题，分类，甚至品牌，空格分割是防止不同类别连着一起而被分词
        goods.setAll(spu.getTitle() + " " + StringUtils.join(categoryNames, " ") + " " + brandName);
        //sku的价格集合
        goods.setPrice(skuPriceList);
        //需要的sku信息
        goods.setSkus(MAPPER.writeValueAsString(skuInfoList));
        goods.setSpecs(specs);
        return goods;
    }

    @Override
    public SearchResult searchGoods(SearchRequest searchRequest) {
        String key = searchRequest.getKey();
        // 判断是否有搜索条件，如果没有，直接返回null。不允许搜索全部商品
        if (StringUtils.isBlank(key)) {
            return null;
        }
        //构建查询条件
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        QueryBuilder boolQueryBuilder = this.buildBooleanQueryBuilder(searchRequest);
        builder.withQuery(boolQueryBuilder);

        //添加对分类的聚合
        builder.addAggregation(AggregationBuilders.terms("categoryAgg").field("cid3"));
        //添加对品牌的聚合
        builder.addAggregation(AggregationBuilders.terms("brandAgg").field("brandId"));

        //通过sourceFilter设置返回的结果字段,我们只需要spu的id、skus、subTitle
        builder.withSourceFilter(new FetchSourceFilter(
                new String[]{"id", "skus", "subTitle"}, null));

        //添加排序
        if (StringUtils.isNotBlank(searchRequest.getSortBy())) {
            builder.withSort(SortBuilders.fieldSort(searchRequest.getSortBy()).order(searchRequest.getDescending() ? SortOrder.DESC : SortOrder.ASC));
        }

        //分页,页码从0开始
        builder.withPageable(PageRequest.of(searchRequest.getPage() - 1, searchRequest.getSize()));

        //执行查询
        AggregatedPage<Goods> searchResult = (AggregatedPage<Goods>) goodsRepository.search(builder.build());

        //获取聚合结果
        List<Map<String, Object>> categoryDatas = this.getCategoryAggregation(searchResult.getAggregation("categoryAgg"));
        List<Brand> brands = this.getBrandAggregation(searchResult.getAggregation("brandAgg"));

        //判断聚合出的分类是否是1个，如果是1个则进行该分类的规格参数聚合
        List<Map<String, Object>> specs = null;
        if (!CollectionUtils.isEmpty(categoryDatas) && categoryDatas.size() == 1) {
            specs = this.getSpecAggregation((Long) categoryDatas.get(0).get("id"), boolQueryBuilder);
        }

        //封装分页数据
        SearchResult searchPageResult = new SearchResult(searchResult.getTotalElements(), searchResult.getTotalPages(), searchResult.getContent(), categoryDatas, brands);
        //添加聚合出的规格参数到搜索结果中，没有查询就是null
        searchPageResult.setSpecs(specs);
        return searchPageResult;
    }

    @Override
    public void createIndex(Long id) throws IOException {
        //查询spu
        Spu spu = this.goodsClient.querySpuById(id);
        Goods goods = this.buildGoods(spu);
        //保存
        this.goodsRepository.save(goods);
    }

    @Override
    public void deleteIndex(Long id) {
        this.goodsRepository.deleteById(id);
    }

    /**
     * 构建布尔查询对象
     *
     * @param searchRequest
     * @return
     */
    private QueryBuilder buildBooleanQueryBuilder(SearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 添加基本查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));

        // 添加过滤条件
        if (CollectionUtils.isEmpty(searchRequest.getFilters())) {
            return boolQueryBuilder;
        }

        searchRequest.getFilters().forEach((key, val) -> {
            if ("品牌".equals(key)) {
                //品牌过滤brandId
                key = "brandId";
            } else if ("分类".equals(key)) {
                //分类过滤cid3
                key = "cid3";
            } else {
                // 如果是规格参数名，过滤字段名：specs.key.keyword
                key = "specs." + key + ".keyword";
            }

            //添加到过滤中
            boolQueryBuilder.filter(QueryBuilders.termQuery(key, val));
        });

        return boolQueryBuilder;
    }

    /**
     * 获取规格参数聚合
     *
     * @return
     */
    private List<Map<String, Object>> getSpecAggregation(Long cid, QueryBuilder basicQuery) {
        //构建自定义查询条件
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //基于基本的查询条件聚合规格参数
        builder.withQuery(basicQuery);
        //根据分类id查询所有的可用于搜索的规格参数用于聚合
        List<SpecParam> specParams = this.specificationClient.querySpecParams(null, cid, null, true);

        //遍历规格参数添加聚合
        specParams.forEach(specParam -> {
            builder.addAggregation(AggregationBuilders.terms(specParam.getName()).field("specs." + specParam.getName() + ".keyword"));
        });

        //添加结果集过滤，只需要聚合的结果集
        builder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));

        //执行查询
        AggregatedPage<Goods> searchResult = (AggregatedPage<Goods>) this.goodsRepository.search(builder.build());

        //获取所有聚合转为map集合
        Map<String, Aggregation> aggregationMap = searchResult.getAggregations().asMap();

        // 定义一个集合，收集聚合结果集
        List<Map<String, Object>> specs = new ArrayList<>();
        //遍历map集合
        aggregationMap.forEach((aggName, aggregation) -> {
            //创建一个新的map用于存放解析后数据{k:规格参数名},{options:可选的集合}
            Map<String, Object> specData = new HashMap<>(2);
            specData.put("k", aggName);

            List<Object> options = new ArrayList<>();
            //收集规格参数值
            StringTerms terms = (StringTerms) aggregation;
            terms.getBuckets().forEach(bucket -> {
                options.add(bucket.getKeyAsString());
            });

            specData.put("options", options);

            specs.add(specData);
        });

        return specs;
    }

    /**
     * 获取可选区间
     *
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    /**
     * 获取分类聚合结果
     *
     * @return
     */
    private List<Map<String, Object>> getCategoryAggregation(Aggregation categoryAggregation) {
        LongTerms longTerms = (LongTerms) categoryAggregation;
        List<Map<String, Object>> categorys = longTerms.getBuckets().stream().map(bucket -> {
            Long cid = bucket.getKeyAsNumber().longValue();
            String categoryName = categoryClient.queryNamesByIds(Arrays.asList(cid)).get(0);
            Map<String, Object> map = new HashMap();
            map.put("id", cid);
            map.put("name", categoryName);
            //查询数据库
            return map;
        }).collect(Collectors.toList());

        return categorys;
    }

    /**
     * 获取品牌聚合结果
     *
     * @return
     */
    private List<Brand> getBrandAggregation(Aggregation brandAggregation) {
        LongTerms longTerms = (LongTerms) brandAggregation;
        List<Brand> brands = longTerms.getBuckets().stream().map(bucket -> {
            long brandId = bucket.getKeyAsNumber().longValue();
            Brand brand = brandClient.queryBrandByid(brandId);
            return brand;
        }).collect(Collectors.toList());
        return brands;
    }
}