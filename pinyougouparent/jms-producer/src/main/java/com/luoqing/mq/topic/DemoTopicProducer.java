package com.luoqing.mq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class DemoTopicProducer {

    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        Connection connection = connectionFactory.createConnection();

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        TextMessage textMessage = session.createTextMessage();
        textMessage.setText("小红你好高！");


        Topic topic = session.createTopic("topicText");

        MessageProducer producer = session.createProducer(topic);

        producer.send(textMessage);

        session.close();
        connection.close();
    }
}
