package com.github.wangyi.activemq.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.jms.JMSException;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.fastjson.JSON;
import com.github.wangyi.activemq.model.OrderInfo;
import com.github.wangyi.activemq.service.SendMessageService;

@Controller
public class IndexController {

	@Autowired
	private SendMessageService sendMessageService;
	
	/**
	 * 消息事务测试text
	 * @throws JMSException
	 */
	@RequestMapping("text")
	public void testObject() throws JMSException{
		OrderInfo order=new OrderInfo();
		order.setGoodsId(1000);
		order.setGoods("三星 note3");
		order.setPrice(3500);
		order.setCreateTime(new Date());
		//sendMessageService.sendObjectMessage(order);
		sendMessageService.sendTextMessage(JSON.toJSONString(order));
	}
	
	/**
	 * 消息事务测试session
	 */
	@RequestMapping("session")
	public  void sessionAwareMapListener(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("content", "MapMessage测试....");
		map.put("time", new Date().getTime());
		OrderInfo order = new OrderInfo();
		order.setGoodsId(RandomUtils.nextInt(1000));
		order.setGoods("三星 note4");
		order.setPrice(3500);
		order.setCreateTime(new Date());
		map.put("order", order);
		for(int i=1;i<=5;i++){
			map.put("count", i);
			sendMessageService.sendMapMessage("sessionAware.mq.queue",JSON.toJSONString(map));
		}
		
	}
	
	
	/**
	 * 消息事务测试adapter
	 */
	@RequestMapping("adapter")
	public  void adaptetMapLister(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("content", "MapMessage测试....");
		map.put("time", new Date().getTime());
		OrderInfo order = new OrderInfo();
		order.setGoodsId(1000);
		order.setGoods("三星 note4");
		order.setPrice(3500);
		order.setCreateTime(new Date());
		map.put("order", order);
		sendMessageService.sendMapMessage("adapterQueue.mq.queue",JSON.toJSONString(map));
	}
	
	/**
	 * 订阅模式
	 */
	@RequestMapping("topic")
	public void topic(){
		String text="发布文本订阅模式。。。。。";
		sendMessageService.sendTopicTextMessage(text);
	}
	
	/********************************************************
	 * 目标：activeMQ支持分布式事务处理
	 * 
	 * 应用场景：接收到消息后进行事务的处理（访问数据库层）
	 * 
	 * 方案：通过spring的jtaTransactioned来实现分布式事务
	 * 
	 * 
	 * 
	 * *********************************************************/
	
}
