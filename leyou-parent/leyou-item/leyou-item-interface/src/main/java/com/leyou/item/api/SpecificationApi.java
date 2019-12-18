package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/6 上午10:55
 * @Since 1.0.0
 */

@RequestMapping("spec")
public interface SpecificationApi {

    /**
     * 根据分类id查询规格分组
     *
     * @return
     */
    @GetMapping("/groups/{cid}")
    public List<SpecGroup> querySpecGroupsByCid(@PathVariable Long cid);

    /**
     * 根据规格分组id查询规格参数
     *
     * @param gid
     * @return
     */
    @GetMapping("/params")
    public List<SpecParam> querySpecParams(
            @RequestParam(name = "gid", required = false) Long gid,
            @RequestParam(name = "cid", required = false) Long cid,
            @RequestParam(name = "generic", required = false) Boolean generic,
            @RequestParam(name = "searching", required = false) Boolean searching);

    /**
     * 根据规分类id查询所有的规格参数组和规格参数组下的所有规格参数
     *
     * @param cid
     * @return
     */
    @GetMapping("/{cid}")
    public List<SpecGroup> querySpecsByCid(@PathVariable Long cid);

    /**
     * 根据规格参数Id查询规格参数
     *
     * @param ids
     * @return
     */
    @GetMapping("/params/query")
    public List<SpecParam> querySpecsByIds(@RequestParam("ids") List<Long> ids);
}