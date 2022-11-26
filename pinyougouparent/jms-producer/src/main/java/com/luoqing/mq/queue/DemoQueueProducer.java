package com.luoqing.mq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class DemoQueueProducer {

    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        Connection connection = connectionFactory.createConnection();

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        TextMessage textMessage = session.createTextMessage();
        textMessage.setText("小红你好高！");

        Queue queue = session.createQueue("queueTest2");

        MessageProducer producer = session.createProducer(queue);

        producer.send(textMessage);

        session.close();
        connection.close();
    }

}
