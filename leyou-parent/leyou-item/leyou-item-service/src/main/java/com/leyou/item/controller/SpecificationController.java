package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @Date 2019/12/6 上午10:55
 * @Since 1.0.0
 */

@Controller
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据分类id查询规格分组
     *
     * @return
     */
    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupsByCid(@PathVariable Long cid) {
        List<SpecGroup> specGroups = specificationService.querySpecGroupsByCid(cid);
        if (CollectionUtils.isEmpty(specGroups)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specGroups);
    }

    /**
     * 根据规格分组id查询规格参数
     *
     * @param gid
     * @return
     */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecParams(
            @RequestParam(name = "gid", required = false) Long gid,
            @RequestParam(name = "cid", required = false) Long cid,
            @RequestParam(name = "generic", required = false) Boolean generic,
            @RequestParam(name = "searching", required = false) Boolean searching) {
        List<SpecParam> specParams = specificationService.queryParams(gid, cid, generic, searching);
        if (CollectionUtils.isEmpty(specParams)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specParams);
    }

    /**
     * 根据规分类id查询所有的规格参数组和规格参数组下的所有规格参数
     *
     * @param cid
     * @return
     */
    @GetMapping("/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecsByCid(@PathVariable Long cid) {
        List<SpecGroup> specGroups = this.specificationService.querySpecsByCid(cid);
        if (CollectionUtils.isEmpty(specGroups)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specGroups);
    }

    /**
     * 根据规格参数Id查询规格参数
     *
     * @param ids
     * @return
     */
    @GetMapping("/params/query")
    public ResponseEntity<List<SpecParam>> querySpecsByIds(@RequestParam("ids") List<Long> ids) {
        List<SpecParam> specParams = this.specificationService.querySpecsByIds(ids);
        if (CollectionUtils.isEmpty(specParams)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specParams);
    }
}