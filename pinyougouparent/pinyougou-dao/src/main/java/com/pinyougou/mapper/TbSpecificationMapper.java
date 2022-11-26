package com.pinyougou.mapper;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TbSpecificationMapper extends Mapper<TbSpecification> {

    /**
     * 查询所有规格，并封装数据for select2
     * @return
     */
    public List<Map<String, Object>> findAllForList();
}