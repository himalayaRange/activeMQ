package com.github.wangyi.activemq.handler;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.github.wangyi.activemq.exception.DatabaseException;
import com.github.wangyi.activemq.service.JTAServices;

/**
 * 消息处理器:所有的异常全部抛到消息监听器
 * <p>User: wangyi
 * <p>Date: 2016-10-11
 * <p>Version: 1.0
 */
@Service
public class MessageHandler {
	
	@Autowired
	private  JTAServices services;
	
	private static final Logger logger=LoggerFactory.getLogger(MessageHandler.class);
	/**
	 * @param textMessage
	 * @throws JMSException 
	 */
	public  void handlerTextMessage(TextMessage textMessage) throws JMSException{
		logger.info("TxtMessage消息ID:"+textMessage.getJMSMessageID()+"处理完毕....");
	}

	/**
	 * 
	 * @param objectMessage
	 * @throws JMSException 
	 */
	public  void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException{
		logger.info("ObjectMessage消息ID:"+objectMessage.getJMSMessageID()+"处理完毕....");
	}
	
	/**
	 * 
	 * @param streamMessage
	 * @throws JMSException 
	 */
	public  void handlerStreamMessage(StreamMessage streamMessage) throws JMSException{
		logger.info("StreamMessage消息ID:"+streamMessage.getJMSMessageID()+"处理完毕....");
	}
	
	/**
	 * 
	 * @param mapMessage
	 * @throws JMSException 
	 * @throws DatabaseException 
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public  void handlerMapMessage(MapMessage mapMessage,String mapMessageContent) throws JMSException, DatabaseException {
		Map<String,Object> map=(Map<String,Object>)JSON.parseObject(mapMessageContent);
		Set<String> keySet = map.keySet();
		for(String key:keySet){
			System.out.println(key+" : "+JSON.toJSONString(map.get(key)));
		}
		/**
		 * 如果消息异常，会进行重发机制（默认为6次）
		 */
		//int count=(int)map.get("count");
		services.insertUser();
		logger.info("MapMessage消息ID:"+mapMessage.getJMSMessageID()+"处理完毕....");
	}
	
	/**
	 * 支持泛型遍历
	 * @param mapMessage
	 * @throws JMSException
	 */
	public  void handlerMapMessage(MapMessage mapMessage) throws JMSException{
		Enumeration enumer  = mapMessage.getMapNames();
		while(enumer.hasMoreElements()){
			Object object=enumer.nextElement();
			System.out.println(mapMessage.getObject(object.toString()));
		}
	}
	
	
}
