package com.luoqing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/identifyingCode")
public class IdentifyingCodeController {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @RequestMapping("/send")
    public String sendMessage()
    {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("signName", "阿里云短信测试");
        dataMap.put("templateCode", "SMS_154950909");
        dataMap.put("phoneNumber", "17802926681");
        dataMap.put("param", "{'code': '123456'}");
        jmsMessagingTemplate.convertAndSend("identifying-code", dataMap);
        return "success";
    }
}
