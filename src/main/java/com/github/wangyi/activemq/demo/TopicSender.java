package com.github.wangyi.activemq.demo;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;

/**
 * 发布订阅模式
 * <p>User: wangyi
 * <p>Date: 2016-10-9
 * <p>Version: 1.0
 */
public class TopicSender {
	
	private static final Logger logger = LoggerFactory.getLogger(TopicSender.class);
	
	 // 发送次数
    public static final int SEND_NUM = 5;
    
    // tcp 地址
    public static final String BROKER_URL = "tcp://localhost:61616";
    
   //目标，在ActiveMQ管理员控制台创建 http://localhost:8161/admin/queues.jsp
    public static final String DESTINATION="hoo.mq.topic";
    
    /**
     * 发布订阅消息
     * MapMessage可以自定义发送消息的内容
     * @param session
     * @param publisher
     * @throws JMSException 
     */
    public static void sendMessage(Session session,TopicPublisher publisher) throws JMSException{
    	for(int i=0;i<SEND_NUM;i++){
    		String message="发布订阅消息："+(i+1)+"条";
    		MapMessage mapMessage = session.createMapMessage();
    		mapMessage.setString("text", message);
    		mapMessage.setLong("time", System.currentTimeMillis());
    		logger.info("topicSender,{}",message);
    		
    		publisher.publish(mapMessage);
    	}
    }

    
    public static void run() throws JMSException{
    	TopicConnection connection=null;
    	TopicSession session=null;
    	try {
    		   TopicConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, BROKER_URL);
    		   connection=factory.createTopicConnection();
    		   connection.start();
    		   session=connection.createTopicSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
    		   //创建订阅者消息队列
    		   Topic topic = session.createTopic(DESTINATION);
    		   //创建订阅消息生产者
    		   TopicPublisher publisher = session.createPublisher(topic);
    		   //设置持久化模式
    		   publisher.setDeliveryMode(DeliveryMode.PERSISTENT);
    		   sendMessage(session, publisher);
    		   session.commit();
    	} catch (Exception e) {
			throw e;
		}finally{
			if(session!=null){
				session.close();
			}
			if(connection!=null){
				connection.close();
			}
		}
    }
    
    public static void main(String[] args) throws JMSException {
		TopicSender.run();
	}
}
