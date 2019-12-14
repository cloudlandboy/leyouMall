package com.leyou.item.service;

import com.leyou.item.pojo.Category;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/4 上午9:42
 * @Since 1.0.0
 */

public interface CategoryService {


    /**
     * 根据parentId查询子类目
     *
     * @param pid
     * @return
     */
    List<Category> queryCategoriesByPid(Long pid);

    /**
     * 根据id查询分类
     *
     * @param id
     * @return
     */
    List<Category> queryByBrandId(Long id);

    /**
     * 根据id列表查询分类信息
     *
     * @param ids
     * @return
     */
    List<String> selectNamesByIds(List<Long> ids);

    /**
     * 根据id查询分类的名称(集合)
     *
     * @param ids
     * @return
     */
    List<String> queryNamesByIds(List<Long> ids);

    /**
     * 根据三级分类（cid3）查询1~3级的category
     *
     * @param id
     * @return
     */
    List<Category> queryAllByCid3(Long id);
}