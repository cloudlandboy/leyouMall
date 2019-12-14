package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author cloudlandboy
 * @Date 2019/12/6 下午12:45
 * @Since 1.0.0
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper StockMapper;


    @Override
    public PageResult<SpuBo> querySpuBoByPage(String key, Boolean saleable, Integer page, Integer rows) {

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }

        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        PageHelper.startPage(page, rows);

        List<Spu> spus = spuMapper.selectByExample(example);

        List<SpuBo> spuBos = spus.stream().map(spu -> {
            //查询分类名称
            List<String> categoryNames = categoryService.selectNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            String cname = StringUtils.join(categoryNames, "/");
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            spuBo.setCname(cname);

            //查询品牌信息
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());
            return spuBo;
        }).collect(Collectors.toList());

        PageInfo<Spu> pageInfo = new PageInfo(spus);
        PageResult pageResult = new PageResult(pageInfo.getTotal(), spuBos);
        return pageResult;
    }

    @Override
    public void saveGoods(SpuBo spuBo) {
        Date now = new Date();

        //保存商品spu
        //保证id为自增长，防止页面恶意提交id参数
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(false);
        spuBo.setCreateTime(now);
        spuBo.setLastUpdateTime(now);
        spuMapper.insertSelective(spuBo);

        //保存商品详情spuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        spuDetailMapper.insertSelective(spuDetail);

        //调用保存sku和库存的方法
        this.saveSkuAndStock(spuBo);
    }

    @Override
    public SpuDetail querySpuDetailById(Long id) {
        return spuDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Sku> querySkuBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skus = skuMapper.select(sku);

        //查询库存，sku表没有库存信息，需要从Stock表查询
        skus.forEach(val -> {
            Stock stock = StockMapper.selectByPrimaryKey(val.getId());
            val.setStock(stock.getStock());
        });

        return skus;
    }

    @Override
    public void updateGoods(SpuBo spuBo) {

        //更新商品spu
        spuBo.setLastUpdateTime(new Date());
        spuBo.setCreateTime(null);
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        spuMapper.updateByPrimaryKeySelective(spuBo);

        //更新商品详情spuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetailMapper.updateByPrimaryKeySelective(spuDetail);

        //先删除原来sku和库存
        List<Sku> oldSkus = this.querySkuBySpuId(spuBo.getId());

        if (!CollectionUtils.isEmpty(oldSkus)) {
            //获取所有以前的sku的id
            List<Long> oldSkuIds = oldSkus.stream().map(oldSku -> oldSku.getId()).collect(Collectors.toList());

            //删除以前的库存
            Example example = new Example(Stock.class);
            example.createCriteria().andIn("skuId", oldSkuIds);
            StockMapper.deleteByExample(example);

            //删除以前的sku
            Sku sku = new Sku();
            sku.setSpuId(spuBo.getId());
            skuMapper.delete(sku);
        }

        //调用保存sku和库存的方法
        this.saveSkuAndStock(spuBo);
    }

    @Override
    public Spu querySpuById(Long id) {
        return this.spuMapper.selectByPrimaryKey(id);
    }


    /**
     * 保存sku和库存
     *
     * @param spuBo
     */
    private void saveSkuAndStock(SpuBo spuBo) {
        //保存sku
        spuBo.getSkus().forEach(sku -> {
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(spuBo.getLastUpdateTime());
            sku.setLastUpdateTime(spuBo.getLastUpdateTime());
            skuMapper.insertSelective(sku);

            //保存库存Stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            StockMapper.insertSelective(stock);
        });
    }

}