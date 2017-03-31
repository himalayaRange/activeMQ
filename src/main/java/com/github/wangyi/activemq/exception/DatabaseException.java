package com.github.wangyi.activemq.exception;

public class DatabaseException  extends Exception{

	private static final long serialVersionUID = 1L;
	
	public DatabaseException(){
		
	}
	
	
	public DatabaseException(String message){
			super(message);
	}
	
	public DatabaseException(Exception e){
		super(new StringBuffer().append("access database exception，detail:").append(e.getMessage()).toString());
}

}
