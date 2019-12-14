package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.http.ResponseEntity;
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

@RequestMapping("category")
public interface CategoryApi {

    /**
     * 根据parentId查询子类目
     *
     * @param pid
     * @return
     */
    @GetMapping("/list")
    public List<Category> queryCategoriesByPid(@RequestParam(name = "pid", defaultValue = "0") Long pid);

    /**
     * 根据品牌id查询分类(修改品牌时回显所属分类)
     *
     * @return
     */
    @GetMapping("bid/{id}")
    public List<Category> queryByBrandId(@PathVariable Long id);

    /**
     * 根据id查询分类的名称(集合)
     *
     * @param ids
     * @return
     */
    @GetMapping("/names")
    public List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 根据3级分类id，查询1~3级的分类
     *
     * @param id
     * @return
     */
    @GetMapping("all/level")
    public List<Category> queryAllByCid3(@RequestParam("id") Long id);
}