package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/4 上午11:49
 * @Since 1.0.0
 */

public interface BrandService {


    /**
     * 根据条件查询品牌分页信息
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    /**
     * 添加品牌
     *
     * @param brand
     * @param cids
     */
    void saveBrand(Brand brand, List<Long> cids);

    /**
     * 根据商品分类id查询品牌
     *
     * @return
     */
    List<Brand> queryBrandsByCid(Long cid);

    /**
     * 根据id查询品牌
     *
     * @return
     */
    Brand queryBrandByid(Long id);
}