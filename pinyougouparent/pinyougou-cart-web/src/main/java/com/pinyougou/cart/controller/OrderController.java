package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.http.Result;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbOrder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @RequestMapping("/add")
    public Result add(@RequestBody TbOrder order)
    {
        System.out.println(order);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setUserId(username);
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());
        order.setStatus("1"); //未付款

        int count = orderService.add(order);

        if (count > 0)
        {
            return new Result(true, "订单添加成功!");
        }

        return new Result(false, "订单添加失败!");
    }
}
