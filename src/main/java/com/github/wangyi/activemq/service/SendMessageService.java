package com.github.wangyi.activemq.service;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 异步发送消息
 * SendMessage Service
 * <p>User: wangyi
 * <p>Date: 2016-10-11
 * <p>Version: 1.0
 */

/***
 * 如何保证消息不丢失？
 * 1.方案.业务端增加一张表存放,消息是否执行.每次业务事务commit后,告知server端已经处理该消息. 即使server 因为接受 timeout 重发,导致多个业务机器收到消息.
 * 	 也不会导致重复处理.业务逻辑检查checkMessageExist(), 再丢弃这条消息,不再重试.
 * 2.一种思路.让server记录. 当返回acknowledge时,进行判断是否已经执行,如果否,那么执行次数+1, 告知用户commit事务. 如果是就告知用户端,进行事务回滚.
         此方案缺陷,例如acknowledge接受成功,但是业务端事务commit失败(停电,硬盘出错),导致该消息丢失.这个方案只做到了②消息不重复执行, 没做到①消息不丢失 .某些情况下有可能消息丢失.
 * 
 */
@Service
public  class SendMessageService {
	
	private static final Logger logger=LoggerFactory.getLogger(SendMessageService.class);
	
	
	@Autowired
	@Qualifier("jmsQueueTemplate") //可以不加，此处使用的是默认的消息模板
	private JmsTemplate jmsTemplate;
	
	
	@Autowired
	@Qualifier("jmsTopicTemplate")
	private JmsTemplate topicJmsTemplate;
	
	/**
	 * 初始化Queu配置，可以在spring-mq.xml中配置
	 */
	private   void  inintQueueConfig(){
		//带事务session的时候设置成true，此时签收模式设置无效
		jmsTemplate.setSessionTransacted(true);
		//不带事务session的的签收模式
		//jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
	}
	
	
	/**
	 * 初始化topic配置，可以在spring-mq.xml中配置
	 */
	private   void  initTopicConfig(){
		//带事务session的时候设置成true，此时签收模式设置无效
		topicJmsTemplate.setSessionTransacted(true);
		//不带事务session的的签收模式
		//jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
	}
	
	
	/**
	 * 文本消息,使用配置文件中统一的destination
	 * @param message
	 */
	@Transactional(value="mq-jta-tx",readOnly=false)
	public void sendTextMessage(final String message){
		
		inintQueueConfig();
		
		jmsTemplate.send(new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) {
				try {
					logger.info(String.format("sendTextMessage:{%s}",message));
					
					return session.createTextMessage(message);
				} catch (JMSException e) {
					logger.error(String.format("sendTextMessage exception=>:{%s}",e.getMessage()));
					return null;
				}
			}
		});
	} 
	
	
	/**
	 * 文本消息，携带目的地
	 * @param destination
	 * @param message
	 */
	@Transactional(value="mq-jta-tx",readOnly=false)
	public void sendTextMessage(final String destination,final String message){
		
		inintQueueConfig();
		
		jmsTemplate.send(destination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) {
				try {
					logger.info(String.format("sendTextMessage :{%s},destination:{%s}",message,destination));
					return session.createTextMessage(message);
					
				} catch (JMSException e) {
					logger.error(String.format(String.format("sendTextMessage exception=>:{%s},destination:{%s}",e.getMessage(),destination)));
					return null;
				}
			}
		});
	} 
	
	/**
	 * 发送序列化对象消息，默认的destination
	 * @param message
	 */
	@Transactional(value="mq-jta-tx",readOnly=false)
	public  void sendObjectMessage(final Serializable message){
		
		inintQueueConfig();
		
		jmsTemplate.send(new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) {
				try {
					logger.info(String.format("sendObjectMessage:{%s}",message));
					return session.createObjectMessage(message);
					
				} catch (JMSException e) {
					logger.error(String.format("sendObjectMessage exception=>:{%s}",e.getMessage()));
					return null;
				}
			}
		});
	} 
	
	
	/**
	 * 发送序列化对象消息，携带目的地
	 * @param destination
	 * @param message
	 */
	@Transactional(value="mq-jta-tx",readOnly=false)
	public  void sendObjectMessage(final String destination ,final Serializable message){
		
		inintQueueConfig();
		
		 jmsTemplate.send(destination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) {
				try {
					logger.info(String.format("sendObjectMessage :{%s},destination:{%s}",message,destination));
					return session.createObjectMessage(message);
					
				} catch (JMSException e) {
					logger.error(String.format("sendObjectMessage exception=>:{%s},destination:{%s}",e.getMessage(),destination));
					return null;
				}
			}
		});
	} 

	
	/**
	 * 发送默认的MapMessage，默认的destination
	 * @param message
	 */
	@Transactional(value="mq-jta-tx",readOnly=false)
	public  void sendMapMessage(final String mapMessageJson){
		
		inintQueueConfig();
		
		jmsTemplate.send(new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) {
				try {
					logger.info(String.format("sendMapMessage :{%s}",mapMessageJson));
					 MapMessage mapMessage = session.createMapMessage();
					mapMessage.setString("mapMessageContent", mapMessageJson);
					return mapMessage;
					
				} catch (JMSException e) {
					logger.error(String.format("sendMapMessage exception=>:{%s}",e.getMessage()));
					return null;
				}
			}
		});
	} 
	

	/**
	 * 发送MapMessage，携带目的地
	 * @param destination
	 * @param message
	 */
	@Transactional(value="mq-jta-tx",readOnly=false)
	public  void sendMapMessage(final String destination,final String mapMessageJson){
		
		inintQueueConfig();
		
		jmsTemplate.send(destination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) {
				try {
					logger.info(String.format("sendMapMessage :{%s},destination:{%s}",mapMessageJson,destination));
					MapMessage mapMessage = session.createMapMessage();
					mapMessage.setString("mapMessageContent", mapMessageJson);
					return mapMessage;
					
				} catch (JMSException e) {
					logger.error(String.format("sendMapMessage exception=>:{%s}",e.getMessage()));
					return null;
				}
			}
		});
	}
	
	/**
	 * 订阅模式Text
	 * @param message
	 */
	@Transactional(value="mq-jta-tx",readOnly=false)
	public void sendTopicTextMessage(final String message){
		
		initTopicConfig();
		
		topicJmsTemplate.send(new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) {
				try {
					logger.info(String.format("sendTopicTextMessage:{%s}",message));
					
					return session.createTextMessage(message);
				} catch (JMSException e) {
					logger.error(String.format("sendTopicTextMessage exception=>:{%s}",e.getMessage()));
					return null;
				}
			}
		});
	} 
	
	
	/**
	 * 订阅模式文本，携带目的地
	 * @param destination
	 * @param message
	 */
	@Transactional(value="mq-jta-tx",readOnly=false)
	public void sendTopicTextMessage(final String destination,final String message){
		
		initTopicConfig();
		
		topicJmsTemplate.send(destination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) {
				try {
					logger.info(String.format("sendTopicTextMessage :{%s},destination:{%s}",message,destination));
					return session.createTextMessage(message);
					
				} catch (JMSException e) {
					logger.error(String.format(String.format("sendTopicTextMessage exception=>:{%s},destination:{%s}",e.getMessage(),destination)));
					return null;
				}
			}
		});
	} 
}
