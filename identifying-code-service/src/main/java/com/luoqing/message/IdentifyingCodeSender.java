package com.luoqing.message;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.google.gson.Gson;
import com.luoqing.alidayu.Sample;
import com.luoqing.pojo.Alidayu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdentifyingCodeSender {

    @Autowired
    private Alidayu alidayu;

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    /**
     *
     * @param signName 阿里云短信测试
     * @param templateCode SMS_154950909
     * @param phoneNumber
     * @param param "{'code': '123666'}"
     * @throws Exception
     */
    public void sendSms(String signName, String templateCode, String phoneNumber, String param) throws Exception {
        Client client = Sample.createClient(alidayu.getAccessKeyId(), alidayu.getAccessKeySecret());
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phoneNumber);
        sendSmsRequest.setSignName(signName);
        sendSmsRequest.setTemplateCode(templateCode);
        sendSmsRequest.setTemplateParam(param);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
            System.out.println(new Gson().toJson(sendSmsResponse));
        } catch (TeaException error) {
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }

    /**
     *
     * @param phoneNumber
     * @param param "{'code': '123666'}"
     * @throws Exception
     */
    public void sendSms(String phoneNumber, String param) throws Exception {
        Client client = Sample.createClient(alidayu.getAccessKeyId(), alidayu.getAccessKeySecret());
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phoneNumber);
        sendSmsRequest.setSignName(alidayu.getSignName());
        sendSmsRequest.setTemplateCode(alidayu.getTemplateCode());
        sendSmsRequest.setTemplateParam(param);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
            System.out.println(new Gson().toJson(sendSmsResponse));
        } catch (TeaException error) {
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }
}
