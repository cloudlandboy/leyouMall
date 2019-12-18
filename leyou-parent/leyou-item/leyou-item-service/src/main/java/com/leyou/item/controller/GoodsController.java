package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/6 下午12:41
 * @Since 1.0.0
 */

@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 根据条件查询商品分页信息
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuBoByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        PageResult<SpuBo> pageResult = goodsService.querySpuBoByPage(key, saleable, page, rows);
        if (pageResult == null || CollectionUtils.isEmpty(pageResult.getItems())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pageResult);
    }

    /**
     * 新增商品
     *
     * @param spuBo
     */
    @PostMapping("/goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo) {
        goodsService.saveGoods(spuBo);
        return ResponseEntity.created(null).build();
    }

    /**
     * 根据商品id查询商品详情
     *
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable Long id) {

        SpuDetail spuDetail = goodsService.querySpuDetailById(id);

        if (spuDetail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 根据商品id查询sku
     *
     * @return
     */
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam Long id) {
        List<Sku> skus = goodsService.querySkuBySpuId(id);
        if (CollectionUtils.isEmpty(skus)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(skus);
    }

    /**
     * 更新商品信息
     *
     * @param spuBo
     * @return
     */
    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo) {
        this.goodsService.updateGoods(spuBo);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据商品id查询商品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id) {
        Spu spu = this.goodsService.querySpuById(id);
        if (spu == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spu);
    }

    /**
     * 根据sku的id查询sku
     *
     * @param id
     * @return
     */
    @GetMapping("sku/{id}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("id") Long id) {
        Sku sku = this.goodsService.querySkuById(id);
        if (sku == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sku);
    }
}