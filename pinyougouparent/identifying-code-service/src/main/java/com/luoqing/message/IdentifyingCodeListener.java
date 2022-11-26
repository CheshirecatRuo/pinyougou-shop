package com.luoqing.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IdentifyingCodeListener {

    @Autowired
    private IdentifyingCodeSender identifyingCodeSender;

    /**
     * 接受各大平台短信
     * @param dataMap dataMap: 签名、模板、发送手机号、对应参数
     */
    @JmsListener(destination = "identifying-code")
    public void readMessage(Map<String, String> dataMap) throws Exception {
        identifyingCodeSender.sendSms(dataMap.get("signName"), dataMap.get("templateCode"), dataMap.get("phoneNumber"), dataMap.get("param"));
    }

}
