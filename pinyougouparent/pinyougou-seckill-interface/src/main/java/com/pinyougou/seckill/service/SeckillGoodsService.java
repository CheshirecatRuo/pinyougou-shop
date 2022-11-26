package com.pinyougou.seckill.service;

import com.pinyougou.pojo.TbSeckillGoods;

import java.util.List;

public interface SeckillGoodsService {

    /**
     * 秒杀商品列表查询
     * @return
     */
    List<TbSeckillGoods> list();

    /**
     * 根据id查询秒杀商品
     * @param id
     * @return
     */
    TbSeckillGoods findOne(Long id);
}
