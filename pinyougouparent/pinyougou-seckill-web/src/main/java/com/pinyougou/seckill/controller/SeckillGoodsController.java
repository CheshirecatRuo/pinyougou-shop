package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillGoodsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seckill/goods")
public class SeckillGoodsController {

    @Reference
    private SeckillGoodsService seckillGoodsService;


    @RequestMapping("/list")
    public List<TbSeckillGoods> list()
    {
        return seckillGoodsService.list();
    }

    @RequestMapping("/findOne")
    public TbSeckillGoods findOne(Long id)
    {
        return  seckillGoodsService.findOne(id);
    }

}
