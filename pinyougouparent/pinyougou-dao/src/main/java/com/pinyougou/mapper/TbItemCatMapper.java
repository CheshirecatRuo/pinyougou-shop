package com.pinyougou.mapper;

import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemCatExample;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbItemCatMapper extends Mapper<TbItemCat> {

    /**
     * 根据父ID查询子数据
     * @param parentId 父ID
     * @return 返回子数据ID的Long数组
     */
    public Long[] findIdsByParentId(Long parentId);
}