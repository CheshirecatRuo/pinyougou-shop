package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbCart;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<TbCart> add(List<TbCart> carts, Long itemId, Integer num) {

        if (carts == null)
        {
            carts = new ArrayList<>();
        }

        //查询该商品的信息
        TbItem item = itemMapper.selectByPrimaryKey(itemId);

        //判断该商品的的商家购物车是否存在
        TbCart cart = searchCart(carts, item.getSellerId());

        //存在，获取该商家的购物车信息Cart
        if (cart != null)
        {
            //获取该商家的购物车里面的所有商品明细，检查当前需要购买的商品是否已经加入了购物车
            TbOrderItem orderItem = searchOrderItem(cart.getOrderItemList(), itemId);
            //如果存在该商品购买明细，则让对应数量增加 + 价格重新计算(总价 = 数量 * 单价)
            if (orderItem != null)
            {
                //数量增加 = 原有数量 + 现购买数量
                orderItem.setNum(orderItem.getNum() + num);
                orderItem.setTotalFee(BigDecimal.valueOf(orderItem.getPrice().doubleValue() * orderItem.getNum()));

                //商品数量减少到0,删除
                if (orderItem.getNum() <= 0)
                {
                    cart.getOrderItemList().remove(orderItem);
                }

                //该商家的购物车对象购买商品明细为空，移除商家购物车
                if (cart.getOrderItemList().size() <= 0)
                {
                    carts.remove(cart);
                }
            }
            //不存在，创建一个新的TbOrderItem,加入该商家对应购物车的商品明细集合中
            else
            {
                orderItem = createOrderItem(item, num);
                cart.getOrderItemList().add(orderItem);
            }
        }
        else
        {
            //给商家创建一个Cart购物车对象
            cart = new TbCart();
            cart.setSellerId(item.getSellerId());
            cart.setSellerName(item.getSeller());

            //创建一个TbOrderItem对象
            TbOrderItem orderItem = createOrderItem(item, num);
            cart.getOrderItemList().add(orderItem);

            carts.add(cart);
        }

        return carts;
    }

    @Override
    public void addGoodsToRedis(String username, List<TbCart> carts) {
        redisTemplate.boundHashOps("CartList").put(username, carts);
    }

    @Override
    public List<TbCart> findCartListFromRedis(String username) {
        return (List<TbCart>) redisTemplate.boundHashOps("CartList").get(username);
    }


    private TbCart searchCart(List<TbCart> carts, String sellerId)
    {
        for (TbCart cart : carts)
        {
            if (sellerId.equals(cart.getSellerId()))
            {
                return  cart;
            }
        }
        return null;
    }

    private TbOrderItem searchOrderItem(List<TbOrderItem> orderItems, Long itemId)
    {
        for (TbOrderItem orderItem : orderItems)
        {
            if (orderItem.getItemId().longValue() == itemId)
            {
                return orderItem;
            }
        }
        return null;
    }

    private TbOrderItem createOrderItem(TbItem item, Integer num)
    {
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setItemId(item.getId());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setPicPath(item.getImage());
        orderItem.setTitle(item.getTitle());
        orderItem.setPrice(item.getPrice());
        orderItem.setNum(num);
        orderItem.setTotalFee(BigDecimal.valueOf(orderItem.getNum() * orderItem.getPrice().doubleValue()));

        return orderItem;
    }

    @Override
    public List<TbCart>  mergeCarts(List<TbCart> redisCarts, List<TbCart> cookieCarts)
    {
        for (TbCart cart : cookieCarts)
        {
            List<TbOrderItem> orderItems = cart.getOrderItemList();
            for (TbOrderItem orderItem : orderItems)
            {
                Long itemId = orderItem.getItemId();
                Integer num = orderItem.getNum();
                redisCarts = add(redisCarts, itemId, num);
            }
        }

        return redisCarts;
    }
}
