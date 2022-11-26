package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.http.Result;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private WeixinPayService weixinPayService;

    @Reference
    private OrderService orderService;

    @RequestMapping("/createNative")
    public Map<String, String>  createNative()
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TbPayLog payLog = orderService.getPayLogByUserId(username);
        //return weixinPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee() * 100 + "");
        return weixinPayService.createNative(payLog.getOutTradeNo(), "1");
    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String outTradeNo) throws InterruptedException {
        int count = 0;
        while (true) {
            Map map = weixinPayService.queryPayStatus(outTradeNo);
            if (map == null) {
                return new Result(false, "查询支付失败");
            }

            if ("SUCCESS".equals(map.get("trade_state"))) {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                orderService.updatePayStatus(username, (String) map.get("transaction_id"));
                return new Result(true, "支付成功");
            } else if ("PAYERROR".equals(map.get("trade_state"))) {
                return new Result(false, "支付失败");
            }

            //休眠3s
            Thread.sleep(3000);
            count++;
            if (count > 10)
            {
                return new Result(false, "timeout");
            }
        }


    }
}
