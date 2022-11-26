package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.ItemSearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemSearchServiceImpl  implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> searchNotHighLighting(Map<String, Object> searchMap) {
        Map<String, Object> dataMap = new HashMap<>();

        //条件搜索 Query
        Query query = new SimpleQuery("*:*");
        if (searchMap != null)
        {
            String keyword = (String) searchMap.get("keyword");

            if(StringUtils.isNoneBlank(keyword))
            {
                Criteria criteria = new Criteria("item_keywords").is(keyword.replace(" ", ""));
                query.addCriteria(criteria);
            }
        }

        //分页
        query.setOffset(0);
        query.setRows(10);

        //执行查询
        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);

        //总记录数
        long totalElements = scoredPage.getTotalElements();
        dataMap.put("totalElements", totalElements);

        //结果集对象
        List<TbItem> items = scoredPage.getContent();

        //获取返回结果，封装到Map
        dataMap.put("items", items);

        return dataMap;
    }

    @Override
    public void importList(List<TbItem> items) {
        solrTemplate.saveBeans(items);
        solrTemplate.commit();
    }

    @Override
    public void deleteByGoodsIds(Long[] ids) {
        Criteria criteria = new Criteria("item_goodsid").in(ids);
        Query query = new SimpleQuery(criteria);

        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {

        //条件搜索 Query
        HighlightQuery query = new SimpleHighlightQuery(new SimpleStringCriteria("*:*"));

        setHighlight(query);

        if (searchMap != null)
        {
            String keyword = (String) searchMap.get("keyword");

            if(StringUtils.isNoneBlank(keyword))
            {
                Criteria criteria = new Criteria("item_keywords").is(keyword);
                query.addCriteria(criteria);
            }

            //分类过滤
            String category = (String) searchMap.get("category");
            if (StringUtils.isNotBlank(category))
            {
                //创建Criteria对象，用于填充对应的搜索条件
                Criteria criteria = new Criteria("item_category").is(category);

                //搜索过滤对象
                FilterQuery filterQuery = new SimpleFilterQuery();
                filterQuery.addCriteria(criteria);

                //将过滤对象加入到Query
                query.addFilterQuery(filterQuery);
            }

            //品牌过滤
            String brand = (String) searchMap.get("brand");
            if (StringUtils.isNotBlank(brand))
            {
                //创建Criteria对象，用于填充对应的搜索条件
                Criteria criteria = new Criteria("item_brand").is(brand);

                //搜索过滤对象
                FilterQuery filterQuery = new SimpleFilterQuery();
                filterQuery.addCriteria(criteria);

                //将过滤对象加入到Query
                query.addFilterQuery(filterQuery);
            }

            //接受规格数据
            Object spec = searchMap.get("spec");
            if (spec != null)
            {
                //$scope.searchMap = {"keyword": "", "category": "", "brand": ""}
                //过滤搜索规格数据
                Map<String, String> specMap = JSON.parseObject(spec.toString(), Map.class);

                //循环迭代搜索
                for (Map.Entry<String, String> entry : specMap.entrySet())
                {
                    //获取 key
                    String key = entry.getKey();
                    String value = entry.getValue();

                    Criteria criteria = new Criteria("item_spec_" + key).is(value);

                    FilterQuery filterQuery = new SimpleFilterQuery(criteria);

                    query.addFilterQuery(filterQuery);
                }
            }

            //价格区间搜索
            String price = (String) searchMap.get("price");

            if (StringUtils.isNotBlank(price))
            {
                String[] ranges = price.split("-");
                if (ranges != null && ranges.length == 2)
                {
                    //创建Criteria,封装匹配条件
                    Criteria criteria = new Criteria("item_price").between(Long.parseLong(ranges[0]), Long.parseLong(ranges[1]), true, false);

                    //创建过滤搜索对象FilterQuery
                    FilterQuery filterQuery = new SimpleFilterQuery(criteria);

                    //将FilterQuery加入到Query中
                    query.addFilterQuery(filterQuery);
                }

                ranges = price.split(" ");
                if (ranges != null && ranges.length == 2)
                {
                    //创建Criteria,封装匹配条件
                    //Criteria criteria = new Criteria("item_price").between(Long.parseLong(ranges[0]), null, true, true);
                    Criteria criteria = new Criteria("item_price").greaterThanEqual(Long.parseLong(ranges[0]));

                    //创建过滤搜索对象FilterQuery
                    FilterQuery filterQuery = new SimpleFilterQuery(criteria);

                    //将FilterQuery加入到Query中
                    query.addFilterQuery(filterQuery);
                }
            }
        }


        //分页
        Integer pageNum = (Integer) searchMap.get("pageNum");
        Integer size = (Integer) searchMap.get("size");
        if (pageNum == null)
        {
            pageNum = 1;
        }
        if (size == null)
        {
            size = 10;
        }

        query.setOffset((pageNum - 1) * size);
        query.setRows(size);


        //排序功能
        String sort = (String) searchMap.get("sort");
        String sortField = (String) searchMap.get("sortField");
        if (StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(sortField))
        {
            Sort orders = null;
            if ("ASC".equalsIgnoreCase(sort))
            {
                orders = new Sort(Sort.Direction.ASC, sortField);
            }
            else
            {
                orders = new Sort(Sort.Direction.DESC, sortField);
            }
            query.addSort(orders);
        }

        //执行查询
        //返回结果包含非高亮数据和高亮数据
        HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);

        highlightReplace(highlightPage);

        List<String> categoryList = getCategory(query);

        Map<String, Object> dataMap = new HashMap<>();

        //总记录数
        long totalElements = highlightPage.getTotalElements();
        dataMap.put("totalElements", totalElements);

        //结果集对象
        List<TbItem> items = highlightPage.getContent();

        //获取返回结果，封装到Map
        dataMap.put("items", items);
        dataMap.put("categoryList", categoryList);

        //当用户选择了分类，根据分类检索规格和品牌
        String category = (String) searchMap.get("category");
        if (StringUtils.isNotBlank(category))
        {
            dataMap.putAll(getBrandAndSpec(category));
        }
        else {

            //默认选中第一个分类
            if (categoryList != null && categoryList.size() > 0) {
                Map<String, Object> specBrandMap = getBrandAndSpec(categoryList.get(0));
                dataMap.putAll(specBrandMap);
            }
        }

        return dataMap;
    }

    /**
     * 获取分组数据
     * @param query
     * @return
     */
    private List<String> getCategory(HighlightQuery query) {

        query.setRows(100);
        query.setOffset(0);

        //分组查询，条件封装都是用上面的Query对象
        GroupOptions groupOptions = new GroupOptions();

        //指定分组域
        groupOptions.addGroupByField("item_category");

        //指定分组查询条件
        query.setGroupOptions(groupOptions);

        //执行查询
        GroupPage<TbItem> groupPage = solrTemplate.queryForGroupPage(query, TbItem.class);

        //获取对应域的分组结果
        GroupResult<TbItem> groupResult = groupPage.getGroupResult("item_category");

        //存放数据
        List<String> categoryList = new ArrayList<>();
        for(GroupEntry<TbItem> entry : groupResult.getGroupEntries())
        {
            String groupValue = entry.getGroupValue();
            categoryList.add(groupValue);
        }
        return categoryList;
    }

    /**
     * 高亮数据替换
     * @param highlightPage
     */
    private void highlightReplace(HighlightPage<TbItem> highlightPage) {
        //高亮信息和非高亮信息集合
        List<HighlightEntry<TbItem>> highlighted = highlightPage.getHighlighted();

        //循环所有数据
        for (HighlightEntry<TbItem> itemHighlightEntry : highlighted)
        {
            //获取被循环的非高亮数据
            TbItem item = itemHighlightEntry.getEntity();

            //获取被循环的高亮数据
            List<HighlightEntry.Highlight> highlights = itemHighlightEntry.getHighlights();

            //有高亮数据，则替换成非高亮数据
            if (highlights != null && highlights.size() > 0)
            {
                //获取高亮记录
                HighlightEntry.Highlight highlight = highlights.get(0);

                //从高亮记录获取高亮数据
                List<String> snipplets = highlight.getSnipplets();

                //将非高亮数据替换为高亮数据
                if (snipplets != null && snipplets.size() > 0)
                {
                    //获取高亮字符
                    String hlStr = snipplets.get(0);
                    item.setTitle(hlStr);
                }
            }
        }
    }

    /**
     * 高亮设置
     * @param query
     */
    private void setHighlight(HighlightQuery query) {
        //高亮信息设置
        HighlightOptions highlightOptions = new HighlightOptions();
        //设置item_title为高亮域
        highlightOptions.addField("item_title");
        //设置前缀
        highlightOptions.setSimplePrefix("<span style='color: red'>");
        //设置后缀
        highlightOptions.setSimplePostfix("</span>");

        //添加高亮选项到Query
        query.setHighlightOptions(highlightOptions);
    }

    /**
     * 获取模板id，同时获取规格和品牌信息
     */
    public Map<String, Object> getBrandAndSpec(String categoryName)
    {
        Map<String, Object> dataMap = new HashMap<>();

        //获取模板id
        Long typeTemplateId = (Long) redisTemplate.boundHashOps("itemCat").get(categoryName);

        if (typeTemplateId != null)
        {
            //获取品牌信息
            List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(typeTemplateId);

            //获取规格信息
            List<Map> specList = (List<Map>) redisTemplate.boundHashOps("specList").get(typeTemplateId);

            //
            dataMap.put("brandList", brandList);
            dataMap.put("specList", specList);
        }

        return dataMap;
    }
}
