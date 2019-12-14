package com.leyou.search.repository;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.search.LeyouSearchServiceApplication;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeyouSearchServiceApplication.class)
public class GoodsRepositoryTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Test
    public void testImportGoodsIndex() throws Exception {
        //创建索引
        elasticsearchTemplate.createIndex(Goods.class);
        //添加映射
        elasticsearchTemplate.putMapping(Goods.class);

        //导入数据
        int page = 1;
        int rows = 100;
        do {
            //查询spu
            PageResult<SpuBo> pageResult = goodsClient.querySpuBoByPage(null, null, page, rows);
            List<SpuBo> spus = pageResult.getItems();
            List<Goods> goodsList = spus.stream().map(spu -> {
                try {
                    return searchService.buildGoods(spu);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            //保存到es
            goodsRepository.saveAll(goodsList);


            // 获取当前页的数据条数，如果是最后一页，没有100条
            rows = spus.size();
            // 每次循环页码加1
            page++;
        } while (rows == 100);
    }
}