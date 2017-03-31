package com.github.wangyi.activemq.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * prodcer annoation
 * <p>User: wangyi
 * <p>Date: 2016-10-12
 * <p>Version: 1.0
 */
@Component
public class Producer {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	
	
	
}
