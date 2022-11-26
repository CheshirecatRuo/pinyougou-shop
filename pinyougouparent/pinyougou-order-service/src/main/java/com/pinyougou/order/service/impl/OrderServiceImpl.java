package com.pinyougou.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.mapper.TbPayLogMapper;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbCart;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbOrderItemMapper orderItemMapper;

    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbPayLogMapper payLogMapper;

    @Override
    public int add(TbOrder order) {

        //查询购物车
        List<TbCart> carts = (List<TbCart>) redisTemplate.boundHashOps("CartList").get(order.getUserId());

        int count = 0;
        double money = 0;

        List<String> orderIdList = new ArrayList<>();

        for (TbCart cart : carts) {
            Long id = idWorker.nextId();
            TbOrder newOrder = new TbOrder();
            newOrder.setOrderId(id);
            newOrder.setUpdateTime(order.getCreateTime());
            newOrder.setCreateTime(order.getCreateTime());
            newOrder.setReceiver(order.getReceiver());
            newOrder.setReceiverAreaName(order.getReceiverAreaName());
            newOrder.setReceiverMobile(order.getReceiverMobile());
            newOrder.setStatus(order.getStatus());
            newOrder.setPaymentType(order.getPaymentType());

            double totalFee = 0;
            for (TbOrderItem orderItem : cart.getOrderItemList()) {
                orderItem.setId(idWorker.nextId());
                orderItem.setOrderId(id);
                totalFee += orderItem.getTotalFee().doubleValue();

                count += orderItemMapper.insertSelective(orderItem);
            }
            newOrder.setPayment(new BigDecimal(totalFee));
            newOrder.setSourceType("2");
            newOrder.setSellerId(cart.getSellerId());

            count += orderMapper.insert(newOrder);
            money += totalFee;

            orderIdList.add(id+ "");
        }

        //清空购物车
        redisTemplate.boundHashOps("CartList").delete(order.getUserId());

        if ("1".equals(order.getPaymentType()))
        {
            TbPayLog payLog = new TbPayLog();
            payLog.setOutTradeNo(idWorker.nextId() + ""); //交易编号
            payLog.setCreateTime(order.getCreateTime());
            payLog.setTotalFee((long) money);   //支付总金额
            payLog.setUserId(order.getUserId());  //用户编号
            payLog.setTradeState("0");  //待支付
            payLog.setOrderList(orderIdList.toString().replace("[", "").replace("]", ""));  //支付的订单编号
            payLog.setPayType("1"); //线上支付
            payLogMapper.insertSelective(payLog);

            //将交易日志存入缓存
            redisTemplate.boundHashOps("PayLog").put(order.getUserId(), payLog);
        }

        return count;
    }

    public TbPayLog getPayLogByUserId(String username)
    {
        return (TbPayLog) redisTemplate.boundHashOps("PayLog").get(username);
    }

    @Override
    public void updatePayStatus(String username, String transactionId) {
        TbPayLog payLog = (TbPayLog) redisTemplate.boundHashOps("PayLog").get(username);

        if (payLog != null)
        {
            String orderList = payLog.getOrderList();
            String[] ids = orderList.split(",");

            for (String id : ids) {
                TbOrder order = new TbOrder();
                order.setOrderId(Long.parseLong(id));
                order.setStatus("2");
                order.setPaymentTime(new Date()); // 实际应该为微信支付返回的时间
                orderMapper.updateByPrimaryKeySelective(order);
            }

            payLog.setTradeState("1");
            payLog.setPayTime(new Date());
            payLog.setTransactionId(transactionId);
            payLogMapper.updateByPrimaryKeySelective(payLog);

            redisTemplate.boundHashOps("PayLog").delete(username);

        }
    }
}
