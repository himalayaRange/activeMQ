package com.github.wangyi.activemq.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

public class SomeHandler implements ErrorHandler{

	private static final Logger logger=LoggerFactory.getLogger(SomeHandler.class);
	
	@Override
	public void handleError(Throwable t) {
		
		logger.error("Error in listener,detail:"+t);

	}

}
