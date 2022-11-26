package com.pinyougou.mapper;

import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TbSpecificationOptionMapper extends Mapper<TbSpecificationOption> {
    /**
     * 查询所有Option，并封装数据for select2
     * @return
     */
    public List<Map<String, Object>> findAllForList();
}