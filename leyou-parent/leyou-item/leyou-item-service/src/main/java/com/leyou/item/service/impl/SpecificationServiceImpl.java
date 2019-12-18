package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/6 上午11:00
 * @Since 1.0.0
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;

    @Override
    public List<SpecGroup> querySpecGroupsByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return specGroupMapper.select(specGroup);
    }

    @Deprecated
    @Override
    public List<SpecParam> querySpecParamsByGid(Long gid) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        return specParamMapper.select(specParam);
    }

    @Override
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        //为null的是不会被作为查询条件的
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        return specParamMapper.select(specParam);
    }

    @Override
    public List<SpecGroup> querySpecsByCid(Long cid) {
        //查询所有的规格参数组
        List<SpecGroup> specGroups = this.querySpecGroupsByCid(cid);
        specGroups.forEach(specGroup -> {
            //查询该规格参组下的所有规格参数
            List<SpecParam> specParams = this.queryParams(specGroup.getId(), null, null, null);
            //封装到规格参数组中
            specGroup.setParams(specParams);
        });

        return specGroups;
    }

    @Override
    public List<SpecParam> querySpecsByIds(List<Long> ids) {
        return this.specParamMapper.selectByIdList(ids);
    }
}