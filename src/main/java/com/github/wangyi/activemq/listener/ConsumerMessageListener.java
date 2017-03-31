package com.github.wangyi.activemq.listener;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.github.wangyi.activemq.handler.MessageHandler;

/***
 * MessageListener最原始的消息监听器，是JMS规范中一个接口，只接收消息进行处理
 * spring对activemq的消息监听器
 * <p>User: wangyi
 * <p>Date: 2016-10-10
 * <p>Version: 1.0
 */
@Service
public class ConsumerMessageListener implements MessageListener{
	
	private static final Logger logger=LoggerFactory.getLogger(ConsumerMessageListener.class);
	
	@Autowired
	private MessageHandler messageHandler;
	
	@Override
	@Transactional(value="mq-jta-tx",readOnly=false)
	//不加jms监听将使用默认的application.propries@JmsListener(destination="test.mq.queue",concurrency="5-10")
	public void onMessage(Message message) {
		if(message instanceof TextMessage){
			TextMessage textMessage=(TextMessage)message;
			try {
				logger.info(String.format("messageLister received,{%s}",textMessage.getText()));
				messageHandler.handlerTextMessage(textMessage);
			} catch (Exception e) {
				logger.info("textMessage messageLister jms received exception:",e.getMessage());
				throw new RuntimeException(e);
			} 
		}else if(message instanceof ObjectMessage){
			ObjectMessage objectMessage=(ObjectMessage)message;
			try {
				logger.info(String.format("messageLister received,{%s}",JSON.toJSON(objectMessage.getObject())));
				messageHandler.handlerObjectMessage(objectMessage);
			} catch (Exception e) {
				logger.info("objectMessage messageLister jms received exception:",e.getMessage());
				throw new RuntimeException(e);
			} 
		}else if(message instanceof StreamMessage){
			StreamMessage streamMessage=(StreamMessage)message;
			try {
				logger.info(String.format("messageLister received,{%s}",streamMessage.getJMSDestination()));
				messageHandler.handlerStreamMessage(streamMessage);
			} catch (Exception e) {
				logger.info("streamMessage messageLister jms received exception:",e.getMessage());
				throw new RuntimeException(e);
			} 
		}else if(message instanceof MapMessage){
			MapMessage mapMessage=(MapMessage)message;
			try {
				logger.info(String.format("messageLister received,{%s}",mapMessage.getString("mapMessageContent")));
				messageHandler.handlerMapMessage(mapMessage,mapMessage.getString("mapMessageContent"));
			} catch (Exception e) {
				logger.info("mapMessage messageLister jms received exception:",e.getMessage());
				throw new RuntimeException(e);
			} 
		}
		
	}

}
