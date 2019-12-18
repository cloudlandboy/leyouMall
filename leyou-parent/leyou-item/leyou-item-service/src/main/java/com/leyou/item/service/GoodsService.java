package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/6 下午12:44
 * @Since 1.0.0
 */

public interface GoodsService {

    /**
     * 根据条件查询商品分页信息
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    PageResult<SpuBo> querySpuBoByPage(String key, Boolean saleable, Integer page, Integer rows);

    /**
     * 新增商品
     *
     * @param spuBo
     */
    void saveGoods(SpuBo spuBo);

    /**
     * 根据商品id查询商品详情
     *
     * @param id
     * @return
     */
    SpuDetail querySpuDetailById(Long id);

    /**
     * 根据商品id查询sku
     *
     * @return
     */
    List<Sku> querySkuBySpuId(Long id);

    /**
     * 更新商品信息
     *
     * @param spuBo
     */
    void updateGoods(SpuBo spuBo);

    /**
     * 根据商品id查询商品信息
     *
     * @param id
     * @return
     */
    Spu querySpuById(Long id);

    /**
     * 根据sku的id查询sku
     *
     * @param id
     * @return
     */
    Sku querySkuById(Long id);
}