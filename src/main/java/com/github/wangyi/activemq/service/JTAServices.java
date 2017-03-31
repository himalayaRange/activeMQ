package com.github.wangyi.activemq.service;

import java.sql.SQLException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.wangyi.activemq.exception.DatabaseException;
import com.github.wangyi.commons.dao.OAuth2UserDao;
import com.github.wangyi.commons.model.Oauth2User;

@Service
public class JTAServices {

	@Autowired
	private  OAuth2UserDao dao;
	
	/**
	 * 数据库抛出所有异常，catch抛出自定义异常
	 * @return
	 * @throws SQLException
	 * @throws DatabaseException 
	 */
	@Transactional(value="mq-jta-tx",readOnly=false,rollbackFor=SQLException.class)
	public  Integer insertUser() throws DatabaseException{
		try {
			Oauth2User user=new Oauth2User();
			user.setId(1000L);
			user.setUsername("ActiveMQ");
			user.setPassword("123456");
			user.setSalt("wangyi");
			user.setCreate_date(new Date());
			user.setUpdate_date(new Date());
			Integer result = dao.insertSelective(user);
			return result;
		} catch (Exception e) {
			throw new DatabaseException("access database exception....");
		}
		
	}
	
}
