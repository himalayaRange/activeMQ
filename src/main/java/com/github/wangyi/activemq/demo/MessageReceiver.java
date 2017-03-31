package com.github.wangyi.activemq.demo;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;

/**
 * 消息接收者
 * 
 * <p>User: wangyi
 * <p>Date: 2016-10-9
 * <p>Version: 1.0
 */
public class MessageReceiver {

	private static final Logger logger=LoggerFactory.getLogger(MessageSender.class);

	//tcp地址
	public static final String BROKER_URL="tcp://localhost:61616";
	
	//目标地址，在ActiveMQ管理员控制台创建 http://localhost:8161/admin/queues.jsp
	public static final String DESTINATION="hoo.mq.queue";
	
	/**
	 * 接收消息
	 * @throws JMSException 
	 */
	public static void run() throws JMSException{
		Connection connection=null;
		Session session=null;
		try {
			 ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, BROKER_URL);
			 connection=factory.createConnection();
			 connection.start();
			 session=connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			 Destination destination = session.createQueue(DESTINATION); //消息队列
			 MessageConsumer messageConsumer = session.createConsumer(destination);

			 while(true){
				 Message receive = messageConsumer.receive(1000*100);//接收消息的时间等待 100ms
				 TextMessage textMessgae=(TextMessage)receive;
				 String message = textMessgae.getText();
				 if(message!=null){
					 logger.info("MessageReceive,{}",JSON.toJSON(message));
				 }else{
					 break;
				 }
			 }
			 
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
		MessageReceiver.run();
		
	}
}
