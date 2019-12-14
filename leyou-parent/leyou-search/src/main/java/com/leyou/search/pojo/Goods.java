package com.leyou.search.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(indexName = "goods", type = "_doc", shards = 1, replicas = 0)
public class Goods {

    /**
     * spuId
     */
    @Id
    private Long id;

    /**
     * 所有需要被搜索的信息，包含标题，分类，甚至品牌
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all;
    @Field(type = FieldType.Keyword, index = false)

    /**
     * 卖点
     */
    private String subTitle;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 1级分类id
     */
    private Long cid1;

    /**
     * 2级分类id
     */
    private Long cid2;

    /**
     * 3级分类id
     */
    private Long cid3;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 价格
     */
    private List<Long> price;

    /**
     * 信息的json结构
     */
    @Field(type = FieldType.Keyword, index = false)

    /**
     * List<sku>信息的json结构
     */
    private String skus;

    /**
     * 可搜索的规格参数，key是参数名，值是参数值
     */
    private Map<String, Object> specs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCid1() {
        return cid1;
    }

    public void setCid1(Long cid1) {
        this.cid1 = cid1;
    }

    public Long getCid2() {
        return cid2;
    }

    public void setCid2(Long cid2) {
        this.cid2 = cid2;
    }

    public Long getCid3() {
        return cid3;
    }

    public void setCid3(Long cid3) {
        this.cid3 = cid3;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Long> getPrice() {
        return price;
    }

    public void setPrice(List<Long> price) {
        this.price = price;
    }

    public String getSkus() {
        return skus;
    }

    public void setSkus(String skus) {
        this.skus = skus;
    }

    public Map<String, Object> getSpecs() {
        return specs;
    }

    public void setSpecs(Map<String, Object> specs) {
        this.specs = specs;
    }

}