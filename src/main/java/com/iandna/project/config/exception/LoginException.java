package com.iandna.project.config.exception;

import com.iandna.project.config.model.MapData;

public class LoginException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String errorCode;
    private String message;
    
    private String filler;
    private MapData result;
    
    /**
     * 로그인 실패시
     * @param errorCode
     * @param message
     */
    public LoginException(String errorCode, String message) {
    	super(errorCode);
    	this.errorCode = errorCode;
    	this.message = message;
    }
    
    public String getErrorCode() {
		return errorCode;
	}
    
    public String getMessage(){
    	return message;
    }
    
    public String getFiller(){
    	return filler;
    }

	public MapData getResult() {
		return result;
	}

	public void setResult(MapData result) {
		this.result = result;
	}
    
}
