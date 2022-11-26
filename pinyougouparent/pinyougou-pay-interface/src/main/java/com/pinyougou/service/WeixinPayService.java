package com.pinyougou.service;

import java.util.Map;

public interface WeixinPayService {

    /**
     * 获取支付二维码
     */
    Map createNative(String outTradeNo, String totalFee);

    /**
     * 获取支付状态
     */
    Map queryPayStatus(String outTradeNo);

    /**
     * 关闭订单
     * @param tradeOutNo
     * @return
     */
    Map closePay(String tradeOutNo) throws Exception;
}
