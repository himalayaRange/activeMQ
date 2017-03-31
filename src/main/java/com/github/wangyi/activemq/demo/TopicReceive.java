package com.github.wangyi.activemq.demo;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订阅消息
 * <p>User: wangyi
 * <p>Date: 2016-10-9
 * <p>Version: 1.0
 */
public class TopicReceive {
	
	private static final Logger logger=LoggerFactory.getLogger(TopicReceive.class);
		
    public static final String BROKER_URL = "tcp://localhost:61616";
   
    public static final String TARGET = "hoo.mq.topic";
    
    public static void run() throws Exception {
    	TopicConnection connection = null;
        TopicSession session = null;
        try {
    	   TopicConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, BROKER_URL);
           connection = factory.createTopicConnection();
           connection.start();
           session = connection.createTopicSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
           Topic topic = session.createTopic(TARGET); //订阅模式消息队列
           TopicSubscriber subscriber = session.createSubscriber(topic);//消息订阅者
           subscriber.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				if(message!=null){
					MapMessage mapMessage=(MapMessage)message;
					try {
						System.out.println(mapMessage.getLong("time")+"接收："+mapMessage.getString("text"));
						logger.info("topicReceive,{}",mapMessage.getLong("time")+"接收："+mapMessage.getString("text"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
           
           Thread.sleep(1000*100);
           
           session.commit();
		} catch (Exception e) {
			throw e;
		}finally{
			 if (session != null) {
	                session.close();
	          }
	          if (connection != null) {
	                connection.close();
	          }
		}
    }
    
    public static void main(String[] args) throws Exception {
		TopicReceive.run();
	}
}
