package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.mapper.TbSeckillOrderMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;
import com.pinyougou.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbSeckillGoodsMapper goodsMapper;

    @Autowired
    private TbSeckillOrderMapper orderMapper;

    @Override
    public void add(String username, Long id) {
//        TbSeckillOrder seckillOrder = (TbSeckillOrder) redisTemplate.boundHashOps("SeckillOrder").get(username);
//
//        if (seckillOrder != null)
//        {
//            throw new RuntimeException("存在未支付订单");
//        }


        /**
         * 高并发下实现抢购检测库存
         */
        Object goodsId = redisTemplate.boundListOps("SeckillGoods_id_" + id).rightPop();
        if (goodsId == null)
        {
            throw new RuntimeException("已售罄");
        }

        //根据id获取商品信息
        TbSeckillGoods goods = (TbSeckillGoods) redisTemplate.boundHashOps("SeckillGoods").get(id);

        if (goods == null || goods.getStockCount() <= 0)
        {
            throw new RuntimeException("已售罄");
        }

        //根据商品信息创建一个订单信息
        if (goods != null)
        {
            TbSeckillOrder order = new TbSeckillOrder();
            order.setId(idWorker.nextId());
            order.setSeckillId(id);
            order.setMoney(goods.getCostPrice());
            order.setUserId(username);
            order.setSellerId(goods.getSellerId());
            order.setCreateTime(new Date());
            order.setStatus("0");

            //将订单存到redis
            redisTemplate.boundHashOps("SeckillOrder").put(username, order);

            goods.setStockCount(goods.getStockCount() - 1);
            //库存削减， 如果商品售罄，数据同步到数据中，并且移除Redis中的记录
            if (goods.getStockCount() <= 0)
            {
                goodsMapper.updateByPrimaryKey(goods);

                //移除redis数据
                redisTemplate.boundHashOps("SeckillOrder").delete(id);
            }
            else
            {
                redisTemplate.boundHashOps("SeckillGoods").put(id, goods);
            }
            //未售罄，修改redis数据即可
        }



    }

    @Override
    public TbSeckillOrder getOrderByUsername(String username) {
        return (TbSeckillOrder) redisTemplate.boundHashOps("SeckillOrder").get(username);
    }

    @Override
    public void updatePayStatus(String username, String transactionId) {
        TbSeckillOrder order = (TbSeckillOrder) redisTemplate.boundHashOps("SeckillOrder").get(username);
        if (order != null)
        {
            order.setStatus("1");
            order.setTransactionId(transactionId);
            order.setPayTime(new Date());

            orderMapper.insertSelective(order);
            redisTemplate.boundHashOps("SeckillOrder").delete(username);
        }
    }

    @Override
    public void removeOrder(String username) {
        TbSeckillOrder order = (TbSeckillOrder) redisTemplate.boundHashOps("SeckillOrder").get(username);
        if (order != null)
        {
            redisTemplate.boundHashOps("SeckillOrder").delete(username);
        }

        //数据回滚
        TbSeckillGoods goods = (TbSeckillGoods) redisTemplate.boundHashOps("SeckillGoods").get(order.getSeckillId());
        if (goods == null)
        {
            goods = goodsMapper.selectByPrimaryKey(order.getSeckillId());
        }

        goods.setStockCount(goods.getStockCount() + 1);

        redisTemplate.boundHashOps("SeckillGoods").put(goods.getId(), goods);

    }
}
