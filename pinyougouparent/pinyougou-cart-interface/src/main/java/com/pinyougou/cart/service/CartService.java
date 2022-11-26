package com.pinyougou.cart.service;

import com.pinyougou.pojo.TbCart;

import java.util.List;

public interface CartService {

    /**
     * 添加到购物车
     * @param itemId
     * @param num
     */
    public List<TbCart> add(List<TbCart> carts, Long itemId, Integer num);

    /**
     * 添加购物车到redis
     * @param username
     * @param carts
     */
    void addGoodsToRedis(String username, List<TbCart> carts);

    /**
     * 查询购物车
     * @param username
     * @return
     */
    List<TbCart> findCartListFromRedis(String username);

    /**
     * 合并购物车
     * @param redisCarts
     * @param cookieCarts
     * @return
     */
    List<TbCart>  mergeCarts(List<TbCart> redisCarts, List<TbCart> cookieCarts);
}
