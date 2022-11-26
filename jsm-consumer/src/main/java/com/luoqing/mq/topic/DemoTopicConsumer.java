package com.luoqing.mq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class DemoTopicConsumer {
    public static void main(String[] args) throws JMSException, IOException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");

        Connection connection = connectionFactory.createConnection();

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic("topicTest");

        MessageConsumer consumer = session.createConsumer(topic);

        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message!=null)
                {
                    if (message instanceof TextMessage)
                    {
                        TextMessage textMessage = (TextMessage) message;
                        System.out.println("监听到的数据为:" + textMessage);
                    }
                }
            }
        });

        System.in.read();

//        while (true)
//        {
//            Message message = consumer.receive(10000);
//            if (message!=null)
//            {
//                if (message instanceof TextMessage)
//                {
//                    TextMessage textMessage = (TextMessage) message;
//                    System.out.println(textMessage);
//                    break;
//                }
//            }
//        }

        session.close();
        connection.close();
    }
}
