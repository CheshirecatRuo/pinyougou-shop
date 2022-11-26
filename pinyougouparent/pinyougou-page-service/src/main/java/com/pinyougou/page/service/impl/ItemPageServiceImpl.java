package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import tk.mybatis.mapper.entity.Example;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemPageServiceImpl implements ItemPageService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean;

    @Value("${HTML_PATH}")
    private String HTML_PATH;

    @Value("${HTML_SUFFIX}")
    private String HTML_SUFFIX;

    @Override
    public Boolean buildHtml(Long goodsId) throws Exception{
        //创建数据模型
        Map<String, Object> dataMap = new HashMap<>();
        TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
        TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);

        dataMap.put("goods", goods);
        dataMap.put("goodsDesc", goodsDesc);
        List<TbItem> items = skuList(goodsId);
        dataMap.put("items", JSON.toJSONString(items));
        //创建Configuration对象
        Configuration configuration = freeMarkerConfigurationFactoryBean.createConfiguration();

        //创建模板对象
        Template template = configuration.getTemplate("item.ftl");

        //指定文件输出对象
        //Writer writer = new FileWriter(new File("G:\\Java\\MyProject\\J2EE\\pinyougouparent\\pinyougou-page-service\\src\\main\\webapp\\" + goodsId + ".html"));
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(HTML_PATH + goodsId + HTML_SUFFIX),"UTF-8"));

        //合成输出文件
        template.process(dataMap, writer);

        //关闭资源
        writer.flush();
        writer.close();

        return true;
    }

    @Override
    public void deleteHtml(Long id) throws IOException {
        File file = new File(HTML_PATH + id + HTML_SUFFIX);
        if (!file.exists())
        {
            throw new IOException("文件不存在");
        }
        file.delete();
    }

    public List<TbItem> skuList(Long goodsId)
    {
        Example example = new Example(TbItem.class);

        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("status", "1");
        criteria.andEqualTo("goodsId", goodsId);

        example.orderBy("isDefault").desc();

        return itemMapper.selectByExample(example);
    }
}
