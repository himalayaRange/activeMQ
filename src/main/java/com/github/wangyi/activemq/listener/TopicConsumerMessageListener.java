package com.github.wangyi.activemq.listener;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;

@Service
public class TopicConsumerMessageListener{

	private static final Logger logger=LoggerFactory.getLogger(ConsumerAdapterListener.class);
	
	public void handleMessage(String message){
		 logger.info("TopicConsumerMessageListener通过handleMessage接收到一个纯文本消息，消息内容是：" + message);   
	}
	
	public void handlesMessage(byte[] message){
		 logger.info("TopicConsumerMessageListener通过handleMessage接收到一个二进制消息，消息内容是：" + message.toString());   
	}
	
	public void handleMessage(Map message){
		 logger.info("TopicConsumerMessageListener通过handleMessage接收到一个Map消息，消息内容是：" + message.get("mapMessageContent"));   
	}
	
	public void handleMessage(Object message){
		 logger.info("TopicConsumerMessageListener通过handleMessage接收到一个对象消息消息，消息内容是：" + JSON.toJSONString(message));   
	}
	
	
	public void receiveMessage(String message){
		logger.info("TopicConsumerMessageListener通过receiveMessage接收到一个纯文本消息，消息内容是：" + message);   
	}
	
	public void receiveMessage(byte[] message){
		 logger.info("TopicConsumerMessageListener通过receiveMessage接收到一个二进制消息，消息内容是：" + message.toString());   
	}
	
	public void receiveMessage(Map message){
		 logger.info("TopicConsumerMessageListener通过receiveMessage接收到一个Map消息，消息内容是：" + message.get("mapMessageContent"));   
	}
	
	public void receiveMessage(Object message){
		 logger.info("TopicConsumerMessageListener通过receiveMessage接收到一个对象消息消息，消息内容是：" + JSON.toJSONString(message));   
	}	
}
