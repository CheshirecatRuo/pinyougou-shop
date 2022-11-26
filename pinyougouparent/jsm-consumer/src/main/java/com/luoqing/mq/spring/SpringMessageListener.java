package com.luoqing.mq.spring;

import com.luoqing.mq.pojo.User;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Map;

@Component
public class SpringMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage)
        {
            TextMessage textMessage = (TextMessage) message;
            try {
                System.out.println("监听到的数据:" + textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        else if (message instanceof MapMessage)
        {
            MapMessage mapMessage = (MapMessage) message;
            Map userData = null;
            try {
                userData = (Map) mapMessage.getObject("userData");
            } catch (JMSException e) {
                e.printStackTrace();
            }
            System.out.println(userData);
        }
        else if (message instanceof ObjectMessage)
        {
            ObjectMessage objectMessage = (ObjectMessage) message;
            User user = null;
            try {
                user = (User) objectMessage.getObject();
            } catch (JMSException e) {
                e.printStackTrace();
            }
            System.out.println(user);
        }
    }
}
