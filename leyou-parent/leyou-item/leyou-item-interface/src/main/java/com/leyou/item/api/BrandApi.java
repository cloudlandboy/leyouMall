package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/4 上午11:03
 * @Since 1.0.0
 */
@RequestMapping("brand")
public interface BrandApi {
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
    @GetMapping("/page")
    public PageResult<Brand> queryBrandsByPage
    (@RequestParam(name = "key", required = false) String key,
     @RequestParam(name = "page", defaultValue = "1") Integer page,
     @RequestParam(name = "rows", defaultValue = "5") Integer rows,
     @RequestParam(name = "sortBy", required = false) String sortBy,
     @RequestParam(name = "desc", required = false) Boolean desc
    );

    /**
     * 添加品牌
     *
     * @param brand
     * @param cids
     */
    @PostMapping
    public ResponseEntity saveBrand(Brand brand, @RequestParam("cids") List<Long> cids);

    /**
     * 根据商品分类id查询品牌
     *
     * @return
     */
    @GetMapping("/cid/{cid}")
    public List<Brand> queryBrandsByCid(@PathVariable Long cid);

    /**
     * 根据id查询品牌
     *
     * @return
     */
    @GetMapping("/{id}")
    public Brand queryBrandByid(@PathVariable Long id);

}