package com.leyou.item.bo;

import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/6 下午12:34
 * @Since 1.0.0
 */

public class SpuBo extends Spu {
    /**
     * 分类名称
     */
    private String cname;

    /**
     * 品牌名称
     */
    private String bname;

    /**
     * 商品详情
     */
    SpuDetail spuDetail;

    /**
     * sku列表
     */
    List<Sku> skus;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }
}