package com.pinyougou.mapper;

import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.pojo.TbTypeTemplateExample;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TbTypeTemplateMapper extends Mapper<TbTypeTemplate> {

    /**
     * 返回所有类型，封装for select2,格式：{"id":1,"text":"xxx"}
     * @return
     */
    public List<Map<String, Object>> findAllForList();
}