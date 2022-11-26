package com.pinyougou.mapper;

import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbItemMapper extends Mapper<TbItem> {

    /**
     * 批量增加集合数据
     * @param items
     */
    void batchInsert(List<TbItem> items);
}