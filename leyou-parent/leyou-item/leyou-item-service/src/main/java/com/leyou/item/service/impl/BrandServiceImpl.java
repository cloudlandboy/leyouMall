package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/4 上午11:51
 * @Since 1.0.0
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {

        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        //根据名称和首字母模糊查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }

        //使用PageHelper插件进行分页
        PageHelper.startPage(page, rows);

        //添加排序条件，默认升序
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }

        //执行查询
        List<Brand> brands = brandMapper.selectByExample(example);

        //获取分页信息
        PageInfo pageInfo = new PageInfo(brands);
        PageResult<Brand> pageResult = new PageResult<Brand>(pageInfo.getTotal(), brands);
        return pageResult;
    }

    @Override
    public void saveBrand(Brand brand, List<Long> cids) {
        brandMapper.insertSelective(brand);
        cids.forEach(cid -> {
            brandMapper.insertCategoryAndBrand(cid, brand.getId());
        });
    }

    @Override
    public List<Brand> queryBrandsByCid(Long cid) {
        List<Brand> brands = brandMapper.SelectByCategoryId(cid);
        return brands;
    }

    @Override
    public Brand queryBrandByid(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }
}