package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.http.Result;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;
import com.pinyougou.service.WeixinPayService;
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
    private SeckillOrderService seckillOrderService;

    @RequestMapping("/createNative")
    public Map<String, String>  createNative()
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        TbSeckillOrder seckillOrder = seckillOrderService.getOrderByUsername(username);

        return weixinPayService.createNative(seckillOrder.getId()+"", "1");
    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String outTradeNo) throws Exception {
        int count = 0;
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        while (true) {
            Map map = weixinPayService.queryPayStatus(outTradeNo);
            if (map == null) {
                return new Result(false, "查询支付失败");
            }

            if ("SUCCESS".equals(map.get("trade_state"))) {

                seckillOrderService.updatePayStatus(username, (String) map.get("transaction_id"));
                return new Result(true, "支付成功");
            } else if ("PAYERROR".equals(map.get("trade_state"))) {
                return new Result(false, "支付失败");
            }

            //休眠3s
            Thread.sleep(3000);
            count++;
            if (count > 10)
            {
                Map<String, String> closeResult = weixinPayService.closePay(outTradeNo);
                if ("SUCCESS".equals(closeResult.get("result_code")))
                {
                    seckillOrderService.removeOrder(username);
                }
                else
                {
                    if ("ORDERPAID".equals(closeResult.get("error_code")))
                    {
                        map = weixinPayService.queryPayStatus(outTradeNo);
                        seckillOrderService.updatePayStatus(username, (String) map.get("transaction_id"));
                    }
                }

                return new Result(false, "timeout");
            }
        }


    }
}
