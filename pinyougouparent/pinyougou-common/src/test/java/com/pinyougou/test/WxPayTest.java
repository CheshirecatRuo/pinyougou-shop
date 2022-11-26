package com.pinyougou.test;


import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.util.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class WxPayTest {

    public static void main(String[] args) throws Exception {
        //createNative();
        queryPayStatus();
    }

    public static void createNative() throws Exception {


        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("appid", "wx8397f8696b538317");  //应用id
        dataMap.put("mch_id", "1473426802");  //商户编号
        dataMap.put("nonce_str", WXPayUtil.generateNonceStr()); //随机数
        dataMap.put("body", "品优购小黄人");   //产品描述
        dataMap.put("out_trade_no", "12552323343");  //商户订单号
        dataMap.put("total_fee", "1"); //总金额
        dataMap.put("spbill_create_ip", "192.168.25.128"); //支付终端ip
        dataMap.put("notify_url", "http://www.baidu.com"); //回调地址
        dataMap.put("trade_type", "NATIVE"); //支付类型


        String paramXml = WXPayUtil.generateSignedXml(dataMap, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb");
        System.out.println(paramXml);


//        String url = "https://api.mch.weixin.qq.com/v3/pay/transactions/native";
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        HttpClient httpClient = new HttpClient(url);
        httpClient.setHttps(true);
        httpClient.setXmlParam(paramXml);
        httpClient.setParameter(dataMap);
        httpClient.post();

        String content = httpClient.getContent();

        Map<String, String > responseMap = WXPayUtil.xmlToMap(content);
        System.out.println(responseMap);
        System.out.println(responseMap.get("code_url"));
    }

    public static void queryPayStatus() throws Exception {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("appid", "wx8397f8696b538317");  //应用id
        dataMap.put("mch_id", "1473426802");  //商户编号
        dataMap.put("out_trade_no", "12552323343");  //商户订单号
        dataMap.put("nonce_str", WXPayUtil.generateNonceStr()); //随机数

        String xmlParam = WXPayUtil.generateSignedXml(dataMap, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb");

        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        HttpClient client = new HttpClient(url);
        client.setHttps(true);
        client.setXmlParam(xmlParam);
        client.post();
        String content = client.getContent();
        Map<String, String> responseMap = WXPayUtil.xmlToMap(content);
        System.out.println(responseMap);
        System.out.println(responseMap.get("trade_status_desc"));
    }

}
