package com.leyou.item.service;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/6 上午10:59
 * @Since 1.0.0
 */

public interface SpecificationService {

    /**
     * 根据分类id查询规格分组
     *
     * @param cid
     * @return
     */
    List<SpecGroup> querySpecGroupsByCid(Long cid);

    /**
     * 根据规格分组id查询规格参数
     *
     * @param gid
     * @return
     */
    @Deprecated
    List<SpecParam> querySpecParamsByGid(Long gid);

    /**
     * 根据给定条件查询规格参数
     *
     * @param gid
     * @return
     */
    List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching);


    /**
     * 根据规分类id查询所有的规格参数组和规格参数组下的所有规格参数
     *
     * @param cid
     * @return
     */
    List<SpecGroup> querySpecsByCid(Long cid);
}