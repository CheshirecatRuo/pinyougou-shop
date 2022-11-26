package com.luoqing.mq.quene;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class DemoMQConsumer {
    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");

        Connection connection = connectionFactory.createConnection();

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue("queueTest2");

        MessageConsumer consumer = session.createConsumer(queue);

        while (true)
        {
            Message message = consumer.receive(10000);
            if (message!=null)
            {
                if (message instanceof TextMessage)
                {
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println(textMessage);
                    break;
                }
            }
        }

        session.close();
        connection.close();
    }
}
