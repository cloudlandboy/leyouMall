package com.leyou.item.mapper;

import com.leyou.item.pojo.SpecParam;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author cloudlandboy
 * @Date 2019/12/6 上午11:22
 * @Since 1.0.0
 */

public interface SpecParamMapper extends Mapper<SpecParam>, SelectByIdListMapper<SpecParam, Long> {

}