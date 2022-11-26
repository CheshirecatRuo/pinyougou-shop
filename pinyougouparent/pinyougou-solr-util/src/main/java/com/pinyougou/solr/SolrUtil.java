package com.pinyougou.solr;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;
    /**
     * 商品数据批量导入数据库
     */
    public void batchAdd()
    {
        //查询数据，商品状态为上架状态
        TbItem item = new TbItem();
        item.setStatus("1");
        List<TbItem> items = itemMapper.select(item);

        //将数据导入数据库中
        solrTemplate.saveBeans(items);
        solrTemplate.commit();
    }

    /**
     * 动态域商品数据批量导入数据库
     */
    public void batchAddDynamic()
    {
        //查询数据，商品状态为上架状态
        TbItem tbItem = new TbItem();
        tbItem.setStatus("1");
        List<TbItem> items = itemMapper.select(tbItem);
        for (TbItem item : items)
        {
            //规格字符串
            String specString = item.getSpec();
            Map<String, String> specMap = JSON.parseObject(specString, Map.class);
            item.setSpecMap(specMap);
        }
        //将数据导入数据库中
        solrTemplate.saveBeans(items);
        solrTemplate.commit();
    }

    public void deleteAll()
    {
        SimpleQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    /**
     * 动态域搜索
     */
    public void queryByCondition(String filedName, String keywords)
    {
        //创建Query指定查询条件
        Query query = new SimpleQuery();
        //增加条件
        Criteria criteria = new Criteria("item_spec_" + filedName).is(keywords);

        //将条件传给Query
        query.addCriteria(criteria);

        //指定分页参数
        query.setOffset(0);
        query.setRows(5);

        //执行分页搜索
        //Query:搜索条件的封装
        //TbItem.class:查询数据结果集转换为Item
        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);

        //获取结果集
        List<TbItem> items = scoredPage.getContent();

        //总记录数
        long totalElements = scoredPage.getTotalElements();

        for (TbItem item : items)
        {
            System.out.println(item.getSpec());
        }
    }

}
