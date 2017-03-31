package com.github.wangyi.activemq.listener;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.github.wangyi.activemq.handler.MessageHandler;

/**
 * SessionAwareMessageListener接收到一个消息后并发送一个回复消息
 * <p>User: wangyi
 * <p>Date: 2016-10-11
 * <p>Version: 1.0
 */
@Service
public class ConsumerSessionAwareMessageListener implements SessionAwareMessageListener<Message> {
	
	private static final Logger logger=LoggerFactory.getLogger(ConsumerSessionAwareMessageListener.class);
	
	@Autowired
	private MessageHandler messageHandler;
	
	private Destination destination;
	
	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	/**
	 * message:接收到的消息
	 * session:用来发送回复消息的对象
	 */
	@Override
	@Transactional(value="mq-jta-tx",readOnly=false)
	//不加jms监听将使用默认的application.propries中的地址@JmsListener(destination="test.mq.queue",concurrency="5-10")
	public void onMessage(Message message, Session session)
			throws JMSException {
		if(message instanceof TextMessage){
			try {
				TextMessage textMessage=(TextMessage)message;
				logger.info(String.format("ConsumerSessionAwareMessageListener received:{%s}",textMessage.getText()));
				messageHandler.handlerTextMessage(textMessage);
				MessageProducer producer = session.createProducer(destination);
				Message backMessage = session.createTextMessage("收到<TextMessage>：ConsumerSessionAwareMessageListener jmsID:"+textMessage.getJMSMessageID());
				producer.send(backMessage);
			} catch (Exception e) {
				logger.info("textMessage ConsumerSessionAwareMessageListener jms received exception:",e.getMessage());
				throw new RuntimeException(e);
			}
		}else if(message instanceof ObjectMessage){
			try {
				ObjectMessage objectMessage=(ObjectMessage)message;
				logger.info(String.format("ConsumerSessionAwareMessageListener received:{%s}",JSON.toJSONString(objectMessage.getObject())));
				messageHandler.handlerObjectMessage(objectMessage);
				MessageProducer producer = session.createProducer(destination);
				Message backMessage = session.createTextMessage("收到<ObjectMessage>：ConsumerSessionAwareMessageListener jmsID:"+objectMessage.getJMSMessageID());
				producer.send(backMessage);
			} catch (Exception e) {
				logger.info("objectMessage ConsumerSessionAwareMessageListener jms received exception:",e.getMessage());
				throw new RuntimeException(e);
			}
		}else if(message instanceof StreamMessage){
			try {
				StreamMessage streamMessage=(StreamMessage)message;
				logger.info(String.format("ConsumerSessionAwareMessageListener received:{%s}",streamMessage.getJMSDestination()));
				messageHandler.handlerStreamMessage(streamMessage);
				MessageProducer producer = session.createProducer(destination);
				Message backMessage = session.createTextMessage("收到<StreamMessage>：ConsumerSessionAwareMessageListener jmsID:"+streamMessage.getJMSMessageID());
				producer.send(backMessage);
			} catch (Exception e) {
				logger.info("streamMessage ConsumerSessionAwareMessageListener jms received exception:",e.getMessage());
				throw new RuntimeException(e);
			}
		}else if(message instanceof MapMessage){
			try {
				MapMessage mapMessage=(MapMessage)message;
				logger.info(String.format("ConsumerSessionAwareMessageListener received:{%s}",mapMessage.getString("mapMessageContent")));
				messageHandler.handlerMapMessage(mapMessage,mapMessage.getString("mapMessageContent"));
				MessageProducer producer = session.createProducer(destination);
				Message backMessage = session.createTextMessage("收到<MapMessage>：ConsumerSessionAwareMessageListener jmsID:"+mapMessage.getJMSMessageID());
				producer.send(backMessage);
			} catch (Exception e) {
				logger.info("mapMessage ConsumerSessionAwareMessageListener jms received exception:",e.getMessage());
				throw new RuntimeException(e);
			}
		}
	}

}
