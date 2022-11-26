package com.luoqing.test;

import com.luoqing.mq.pojo.User;
import com.luoqing.mq.spring.SpringMessageProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-mq.xml")
public class SpringProducerTest {
    @Autowired
    private SpringMessageProducer messageProducer;

    @Test
    public void testSendTextMessage()
    {
        messageProducer.sendTextMessage("小红，你吃饭了吗");
    }

    @Test
    public void testSendMapMessage()
    {
        Map dataMap = new HashMap<String, String>();
        dataMap.put("username", "夏红");
        dataMap.put("age", "21");
        dataMap.put("address", "dizhi");
        messageProducer.sendMapMessage(dataMap);
    }

    @Test
    public void testSendObjectMessage()
    {
        User user = new User(1, "xiaohong", new Date());
        messageProducer.sendObjectMessage(user);
    }
}
