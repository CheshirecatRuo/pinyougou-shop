package com.pinyougou.seckill.service;

import com.pinyougou.pojo.TbSeckillOrder;

public interface SeckillOrderService {

    /**
     * 增加订单
     */
    void add(String username, Long id);

    /**
     * 根据用户名查询订单信息
     * @param username
     * @return
     */
    TbSeckillOrder getOrderByUsername(String username);

    /**
     * 修改订单支付状态
     * @param username
     * @param transactionId
     */
    void updatePayStatus(String username, String transactionId);

    /**
     * 根据用户名移除订单信息
     * @param username
     */
    void removeOrder(String username);
}
