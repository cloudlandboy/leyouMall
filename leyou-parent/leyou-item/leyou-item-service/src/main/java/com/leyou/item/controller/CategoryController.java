package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/4 上午9:24
 * @Since 1.0.0
 */

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据parentId查询子类目
     *
     * @param pid
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(@RequestParam(name = "pid", defaultValue = "0") Long pid) {
        List<Category> categoryList = categoryService.queryCategoriesByPid(pid);

        //没有数据返回404
        if (CollectionUtils.isEmpty(categoryList)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categoryList);
    }

    /**
     * 根据品牌id查询分类(修改品牌时回显所属分类)
     *
     * @return
     */
    @GetMapping("bid/{id}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable Long id) {
        List<Category> categories = categoryService.queryByBrandId(id);
        if (CollectionUtils.isEmpty(categories)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categories);
    }

    /**
     * 根据id查询分类的名称(集合)
     *
     * @param ids
     * @return
     */
    @GetMapping("/names")
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids") List<Long> ids) {
        List<String> categoryNames = categoryService.queryNamesByIds(ids);
        if (CollectionUtils.isEmpty(categoryNames)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoryNames);
    }

    /**
     * 根据3级分类id，查询1~3级的分类
     *
     * @param id
     * @return
     */
    @GetMapping("all/level")
    public ResponseEntity<List<Category>> queryAllByCid3(@RequestParam("id") Long id) {
        List<Category> list = this.categoryService.queryAllByCid3(id);
        if (CollectionUtils.isEmpty(list)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }
}