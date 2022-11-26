package com.pinyougou.task;

import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class SeckillGoodsTask {

    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0/15 * * * * ?")
    public void pushSeckillGoods2Redis()
    {
        //审核通过数据 库存>0 开始时间<=当前时间<=结束时间
//        Example example = new Example(TbSeckillGoods.class);
//        Example.Criteria criteria = example.createCriteria();
//
//        criteria.andEqualTo("status", "1");
//        criteria.andGreaterThan("stockCount", 0);
//        criteria.andCondition("NOW() BETWEEN start_time and end_time");

        //审核通过数据 库存>0 开始时间<=当前时间<=结束时间
        Example example = new Example(TbSeckillGoods.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo( "status", "1");
        criteria.andGreaterThan("stockCount", 0);
        criteria.andCondition("now() between start_time and end_time");

        Set<Long> ids = redisTemplate.boundHashOps("SeckillGoods").keys();
        if (ids != null && ids.size() > 0)
        {
            criteria.andNotIn("id", new ArrayList<>(ids));
        }

        List<TbSeckillGoods> goods = seckillGoodsMapper.selectByExample(example);

        redisTemplate.boundHashOps("SeckillGoodsList").put("list", goods);

        for (TbSeckillGoods good : goods)
        {
            redisTemplate.boundHashOps("SeckillGoods").put(good.getId(), good);

            pushCount2Queue(good);
        }

        List<TbSeckillGoods> goodsList = (List<TbSeckillGoods>) redisTemplate.boundHashOps("SeckillGoodsList").get("list");

        List<TbSeckillGoods> seckillGoods = redisTemplate.boundHashOps("SeckillGoods").values();

        System.out.println("------------");
    }

    public void pushCount2Queue(TbSeckillGoods goods)
    {
        for (int i = 0; i < goods.getStockCount(); i++)
        {
            redisTemplate.boundListOps("SeckillGoods_id_" + goods.getId()).leftPush(goods.getId());
        }
    }

}
