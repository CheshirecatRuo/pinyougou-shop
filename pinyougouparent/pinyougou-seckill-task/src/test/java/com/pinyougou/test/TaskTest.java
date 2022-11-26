package com.pinyougou.test;

import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;

public class TaskTest {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/spring/spring.xml");
        TbSeckillGoodsMapper seckillGoodsMapper = (TbSeckillGoodsMapper) applicationContext.getBean("tbSeckillGoodsMapper");
        RedisTemplate redisTemplate = (RedisTemplate) applicationContext.getBean("redisTemplate");


        TbSeckillGoodsExample example = new TbSeckillGoodsExample();
        TbSeckillGoodsExample.Criteria criteria = example.createCriteria();

        criteria.andStatusEqualTo( "1");
        criteria.andStockCountGreaterThan(0);
        Date now = new Date();
        criteria.andStartTimeLessThan(now);
        criteria.andEndTimeGreaterThan(now);

        System.out.println(seckillGoodsMapper);

        List<TbSeckillGoods> goods = seckillGoodsMapper.selectByExample(example);

        redisTemplate.boundHashOps("SeckillGoodsList").put("list", goods);

        System.out.println(goods);
    }
}
