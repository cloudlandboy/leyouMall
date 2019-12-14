package com.leyou.search.pojo;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;
import java.util.Map;

/**
 * @Author cloudlandboy
 * @Date 2019/12/11 下午12:46
 * @Since 1.0.0
 */

public class SearchResult extends PageResult<Goods> {

    /**
     * 搜索结果聚合出的分类信息
     */
    private List<Map<String, Object>> categories;

    /**
     * 搜索结果聚合出的品牌信息
     */
    private List<Brand> brands;

    /**
     * 搜索结果聚合出的规格参数信息
     */
    private List<Map<String, Object>> specs;


    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }

    public SearchResult() {
    }

    public SearchResult(List<Map<String, Object>> categories, List<Brand> brands) {
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Map<String, Object>> categories, List<Brand> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }

    public List<Map<String, Object>> getCategories() {
        return categories;
    }

    public void setCategories(List<Map<String, Object>> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }
}