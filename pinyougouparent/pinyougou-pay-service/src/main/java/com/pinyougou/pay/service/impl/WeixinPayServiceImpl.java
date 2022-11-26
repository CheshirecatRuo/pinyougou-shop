package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.service.WeixinPayService;
import com.pinyougou.util.HttpClient;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Value("${appId}")
    private String appId;

    @Value("${mchId}")
    private String mchId;

    @Value("${key}")
    private String key;

    @Value("${notifyUrl}")
    private String notifyUrl;

    @Override
    public Map createNative(String outTradeNo, String totalFee) {
        try {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("appid", appId);  //应用id
            dataMap.put("mch_id", mchId);  //商户编号
            dataMap.put("nonce_str", WXPayUtil.generateNonceStr()); //随机数
            dataMap.put("body", "品优购小黄人");   //产品描述
            dataMap.put("out_trade_no", outTradeNo);  //商户订单号
            dataMap.put("total_fee", totalFee); //总金额
            dataMap.put("spbill_create_ip", "192.168.25.128"); //支付终端ip
            dataMap.put("notify_url", notifyUrl); //回调地址
            dataMap.put("trade_type", "NATIVE"); //支付类型


            String paramXml = WXPayUtil.generateSignedXml(dataMap, key);
            System.out.println(paramXml);

            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            HttpClient httpClient = new HttpClient(url);
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.setParameter(dataMap);
            httpClient.post();

            String content = httpClient.getContent();

            Map<String, String > responseMap = WXPayUtil.xmlToMap(content);


            Map<String, String > response = new HashMap<>();
            response.put("out_trade_no", outTradeNo);
            response.put("total_fee", totalFee);
            response.put("code_url", responseMap.get("code_url"));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map queryPayStatus(String outTradeNo) {
        try {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("appid", appId);  //应用id
            dataMap.put("mch_id", mchId);  //商户编号
            dataMap.put("out_trade_no", outTradeNo);  //商户订单号
            dataMap.put("nonce_str", WXPayUtil.generateNonceStr()); //随机数

            String xmlParam = WXPayUtil.generateSignedXml(dataMap, key);

            String url = "https://api.mch.weixin.qq.com/pay/orderquery";
            HttpClient client = new HttpClient(url);
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();
            String content = client.getContent();
            Map<String, String> responseMap = WXPayUtil.xmlToMap(content);

            return responseMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map closePay(String tradeOutNo) throws Exception {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("appid", appId);  //应用id
        dataMap.put("mch_id", mchId);  //商户编号
        dataMap.put("nonce_str", WXPayUtil.generateNonceStr()); //随机数
        dataMap.put("out_trade_no", tradeOutNo);  //商户订单号
        String xmlParam = WXPayUtil.generateSignedXml(dataMap, key);

        String url = "https://api.mch.weixin.qq.com/pay/closeorder";
        HttpClient client = new HttpClient(url);
        client.setHttps(true);
        client.setXmlParam(xmlParam);
        client.post();
        String content = client.getContent();
        Map<String, String> responseMap = WXPayUtil.xmlToMap(content);

        return responseMap;
    }
}
