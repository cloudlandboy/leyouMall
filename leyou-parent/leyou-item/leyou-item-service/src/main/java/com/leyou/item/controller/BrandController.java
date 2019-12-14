package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/4 上午11:03
 * @Since 1.0.0
 */

@Controller
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

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
    public ResponseEntity<PageResult<Brand>> queryBrandsByPage
    (@RequestParam(name = "key", required = false) String key,
     @RequestParam(name = "page", defaultValue = "1") Integer page,
     @RequestParam(name = "rows", defaultValue = "5") Integer rows,
     @RequestParam(name = "sortBy", required = false) String sortBy,
     @RequestParam(name = "desc", required = false) Boolean desc
    ) {

        PageResult<Brand> pageResult = brandService.queryBrandsByPage(key, page, rows, sortBy, desc);
        return ResponseEntity.ok(pageResult);

    }

    /**
     * 添加品牌
     *
     * @param brand
     * @param cids
     */
    @PostMapping
    public ResponseEntity saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {

        brandService.saveBrand(brand, cids);

        return ResponseEntity.created(null).build();
    }

    /**
     * 根据商品分类id查询品牌
     *
     * @return
     */
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable Long cid) {
        List<Brand> brands = brandService.queryBrandsByCid(cid);
        if (CollectionUtils.isEmpty(brands)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);
    }

    /**
     * 根据id查询品牌
     *
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Brand> queryBrandByid(@PathVariable Long id) {
        Brand brand = brandService.queryBrandByid(id);
        if (brand == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }

}