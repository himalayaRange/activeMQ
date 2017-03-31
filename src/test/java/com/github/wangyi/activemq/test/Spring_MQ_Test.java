package com.github.wangyi.activemq.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class Spring_MQ_Test {

	@Autowired
	private ApplicationContext ac;
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Test
	public void test(){
		System.out.println(ac);
		System.out.println(jmsTemplate);
	}
	
	
}
