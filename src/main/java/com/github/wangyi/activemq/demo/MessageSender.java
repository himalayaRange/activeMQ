package com.github.wangyi.activemq.demo;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息发送者
 * <p>User: wangyi
 * <p>Date: 2016-10-9
 * <p>Version: 1.0
 */
public class MessageSender {
	
	private static final Logger logger=LoggerFactory.getLogger(MessageSender.class);

	//发送次数
	public static final int SEND_NUM=5;
	
	//tcp地址
	public static final String BROKER_URL="tcp://localhost:61616";
	
	//目标地址，在ActiveMQ管理员控制台创建 http://localhost:8161/admin/queues.jsp
	public static final String DESTINATION="hoo.mq.queue";

	/**
	 * 发送文本消息
	 * @param session
	 * @param messageProducer
	 * @throws JMSException 
	 */
	public static void sendMessage(Session session,MessageProducer messageProducer) throws JMSException{
		for(int i=0;i<SEND_NUM;i++){
			String message = "发送消息第" + (i + 1) + "条";
			TextMessage textMessage = session.createTextMessage(message);
			logger.info("sendMessage,{}",textMessage);
			messageProducer.send(textMessage);
		}
	}
	
	public static void run() throws Exception{
		Connection connection=null;
		Session session=null;
		try {
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, BROKER_URL);
			connection=factory.createConnection();
			connection.start();
			//创建会话
			session=connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			//创建消息队列
			Destination destination = session.createQueue(DESTINATION);
			//创建消息生产者
			MessageProducer messageProducer = session.createProducer(destination);
			//设置持久化模式
			messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT); //非持久化
			sendMessage(session, messageProducer);
			//提交会话
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
	
	public static void main(String[] args) throws Exception {
		MessageSender.run();
	}
}
