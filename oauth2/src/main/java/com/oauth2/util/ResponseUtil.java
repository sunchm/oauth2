package com.oauth2.util;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.oauth2.entity.Constants;


public class ResponseUtil {
	
	protected static final Logger logger = LoggerFactory.getLogger("oauth");
	
	public static JSONObject getJson(String client_id, String operate_type, String error_code){
		JSONObject json = new JSONObject();
		String error_description = "";
		json.put("error_code", error_code);
		switch (error_code) {
			case Constants.REQUEST_OVERTIME:
				error_description = Constants.REQUEST_OVERTIME_DESCRIBE;
				break;
			case Constants.INVALID_PARAMETER:
				error_description = Constants.INVALID_PARAMETER_DESCRIBE;
				break;
			case Constants.SERVER_EXCEPTION:
				error_description = Constants.SERVER_EXCEPTION_DESCRIBE;
				break;
			case Constants.INVALID_RESPONSE_TYPE:
				error_description = Constants.INVALID_RESPONSE_TYPE_DESCRIBE;
				break;
			case Constants.INVALID_SCOPE:
				error_description = Constants.INVALID_SCOPE_DESCRIBE;
				break;
			case Constants.INVALID_CLIENT_ID:
				error_description = Constants.INVALID_CLIENT_ID_DESCRIBE;
				break;
			case Constants.INVALID_CLIENT_ID_SECRET:
				error_description = Constants.INVALID_CLIENT_ID_SECRET_DESCRIBE;
				break;
			case Constants.INVALID_AUTH_CODE:
				error_description = Constants.INVALID_AUTH_CODE_DESCRIBE;
				break;
			case Constants.INVALID_REFRESH_TOKEN:
				error_description = Constants.INVALID_REFRESH_TOKEN_DESCRIBE;
				break;
			case Constants.INVALID_ACCESS_TOKEN:
				error_description = Constants.INVALID_ACCESS_TOKEN_DESCRIBE;
				break;
		}
		json.put("error_description", error_description);
		logger.info("{}<{}>{}{}", client_id, operate_type, "错误请求：", json);
		return json;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HttpEntity getAbnormalResponse(String client_id, String operate_type, String error_code){
		return new ResponseEntity(getJson(client_id, operate_type, error_code) ,HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HttpEntity getAbnormalResponse(String description){
		JSONObject json = new JSONObject();
		json.put("error_code", Constants.INVALID_PARAMETER);
		json.put("error_description", description);
		return new ResponseEntity(json ,HttpStatus.OK);
	}
}
