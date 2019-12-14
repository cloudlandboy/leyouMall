package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author cloudlandboy
 * @Date 2019/12/9 下午7:34
 * @Since 1.0.0
 */

public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {

}