package com.github.wangyi.activemq.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.jms.JMSException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.alibaba.fastjson.JSON;
import com.github.wangyi.activemq.model.OrderInfo;
import com.github.wangyi.activemq.service.SendMessageService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TestSendMessage {
	
	@Autowired
	private SendMessageService sendMessageService;
	
	
	
/**********************************消息监听器*******************************************************************************************/	
	@Test
	public void sendText() throws JMSException{
		sendMessageService.sendTextMessage("测试文本消息！");
	}
	
	@Test
	public void testObject() throws JMSException{
		OrderInfo order=new OrderInfo();
		order.setGoodsId(1000);
		order.setGoods("三星 note3");
		order.setPrice(3500);
		order.setCreateTime(new Date());
		//sendMessageService.sendObjectMessage(order);
		sendMessageService.sendTextMessage(JSON.toJSONString(order));
	}
	
	
	
	
/**********************************含session的消息监听器*********************************************************************************/
	/**
	 * 测试sessionAwareListener文本并回复消息
	 */
	@Test
	public void testSessionAwareListener(){
		sendMessageService.sendTextMessage("sessionAware.mq.queue", "测试sendMessageService！");
	}
	
	/**
	 * 测试sessionAwareListener对象并消息回复
	 */
	@Test
	public void testSeesionAwareObjectListener(){
		OrderInfo order=new OrderInfo();
		order.setGoodsId(1000);
		order.setGoods("三星 note3");
		order.setPrice(3500);
		order.setCreateTime(new Date());
		//sendMessageService.sendObjectMessage("sessionAware.mq.queue", order);
		sendMessageService.sendObjectMessage("sessionAware.mq.queue", JSON.toJSONString(order));
	}
	
	@Test
	public  void testSessionAwareMapListener(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("content", "MapMessage测试....");
		map.put("time", new Date().getTime());
		OrderInfo order = new OrderInfo();
		order.setGoodsId(1000);
		order.setGoods("三星 note4");
		order.setPrice(3500);
		order.setCreateTime(new Date());
		map.put("order", order);
		sendMessageService.sendMapMessage("sessionAware.mq.queue",JSON.toJSONString(map));
	}
	
	
/**********************************消息适配器监听器***************************************************************************************/	
	
	@Test
	public void testAdapterTextListener(){
		sendMessageService.sendTextMessage("adapterQueue.mq.queue", "消息适配器监听器...");
	}
	
	@Test
	public void testAdapterObjectLister(){
		OrderInfo order = new OrderInfo();
		order.setGoodsId(1000);
		order.setGoods("三星 note4");
		order.setPrice(3500);
		order.setCreateTime(new Date());
		//sendMessageService.sendObjectMessage("adapterQueue.mq.queue", order);
		sendMessageService.sendObjectMessage("adapterQueue.mq.queue", JSON.toJSONString(order));
	}
	
	
	@Test
	public  void testAdaptetMapLister(){
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
	 * 发布文本订阅模式
	 */
	@Test
	public void testTextTopicListener(){
		String text="发布文本订阅模式。。。。。";
		sendMessageService.sendTopicTextMessage(text);
	}
	
}
