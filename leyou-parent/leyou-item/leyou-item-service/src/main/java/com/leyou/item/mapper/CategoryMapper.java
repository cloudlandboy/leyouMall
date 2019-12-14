package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/4 上午9:33
 * @Since 1.0.0
 */

public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category, Long> {

    /**
     * 根据品牌id查询该品牌的所有分类
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM tb_category WHERE id IN (SELECT category_id FROM tb_category_brand WHERE brand_id=#{brand_id})")
    List<Category> selectByBrandId(Long id);
}