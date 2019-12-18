package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/6 下午12:41
 * @Since 1.0.0
 */

@Controller
public interface GoodsApi {

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
    public PageResult<SpuBo> querySpuBoByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    );

    /**
     * 新增商品
     *
     * @param spuBo
     */
    @PostMapping("/goods")
    public void saveGoods(@RequestBody SpuBo spuBo);

    /**
     * 根据商品id查询商品详情
     *
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable Long id);

    /**
     * 根据商品id查询sku
     *
     * @return
     */
    @GetMapping("/sku/list")
    public List<Sku> querySkuBySpuId(@RequestParam Long id);

    /**
     * 更新商品信息
     *
     * @param spuBo
     * @return
     */
    @PutMapping("/goods")
    public void updateGoods(@RequestBody SpuBo spuBo);

    /**
     * 根据商品id查询商品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/spu/{id}")
    public Spu querySpuById(@PathVariable("id") Long id);

    /**
     * 根据sku的id查询sku
     *
     * @param id
     * @return
     */
    @GetMapping("sku/{id}")
    public Sku querySkuById(@PathVariable("id") Long id);
}