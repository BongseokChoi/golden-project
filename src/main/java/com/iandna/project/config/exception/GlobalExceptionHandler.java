package com.iandna.project.config.exception;

import com.iandna.project.config.model.ResponseModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Log logger = LogFactory.getLog (GlobalExceptionHandler.class);
	/*
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        log.error("handleException",ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    */
	
    @ExceptionHandler(RuntimeException.class)
    public ResponseModel commonJsonExceptionHandler(CommonJsonException ex){
    	logger.error("LoginException", ex);
    	logger.error("CommonJsonException", ex.getCause());
    	ResponseModel responseModel = new ResponseModel();
    	responseModel.setResCd(ex.getCode());
    	responseModel.setResMsg(ex.getMessage());
    	return responseModel;
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseModel loginExceptionHandler(LoginException ex){
    	logger.error("LoginException", ex);
    	logger.error("LoginException", ex.getCause());
    	ResponseModel responseModel = new ResponseModel();
    	responseModel.setResCd(ex.getErrorCode());
    	responseModel.setResMsg(ex.getMessage());
    	return responseModel;
    }
}
