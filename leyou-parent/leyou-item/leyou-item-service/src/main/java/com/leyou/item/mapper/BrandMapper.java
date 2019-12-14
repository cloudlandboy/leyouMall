package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/4 下午12:03
 * @Since 1.0.0
 */

public interface BrandMapper extends Mapper<Brand> {


    @Insert("INSERT INTO tb_category_brand VALUES (#{cid},#{id})")
    int insertCategoryAndBrand(Long cid, Long id);

    @Select("SELECT tb.* FROM tb_brand tb INNER JOIN tb_category_brand tcb ON tb.id=tcb.brand_id WHERE tcb.category_id=#{cid}")
    List<Brand> SelectByCategoryId(Long cid);
}