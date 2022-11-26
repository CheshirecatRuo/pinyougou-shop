package com.pinyougou.search;

import com.pinyougou.pojo.TbItem;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {

    /**
     *搜索方法
     * @param searchMap 关键词、分类、品牌、规格、价格、排序
     * @return 商品列表、分页数据
     */
    public Map<String, Object> search(Map<String, Object> searchMap);

    /**
     *高亮搜索方法
     * @param searchMap 关键词、分类、品牌、规格、价格、排序
     * @return 商品列表、分页数据
     */
    public Map<String, Object> searchNotHighLighting(Map<String, Object> searchMap);

    /**
     * 批量导入
     * @param items
     */
    public void importList(List<TbItem> items);

    /**
     * 根据goodsId删除TbItem信息
     * @param ids
     */
    public void deleteByGoodsIds(Long[] ids);
}
