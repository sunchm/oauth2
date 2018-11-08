package com.oauth2.client.entity;

import org.json.simple.JSONObject;

import com.oauth2.client.util.JsonUtil;

public class Jwt {
	/**
	 * true：token有效
	 */
	private boolean isAvailability;
	/**
	 * 用户id
	 */
	private String app_account_id;

	public Jwt(String json) {
		JSONObject obj = JsonUtil.parseJson(json);
        if (obj != null) {
            Object isAvailability = obj.get("flag");
            Object app_account_id = obj.get("app_account_id");
            if (isAvailability != null) {
            	this.setAvailability((boolean) isAvailability);
            }
            if (app_account_id != null) {
                this.setApp_account_id(String.valueOf(app_account_id));
            }
        }
	}
	public String getApp_account_id() {
		return app_account_id;
	}
	
	public void setApp_account_id(String app_account_id) {
		this.app_account_id = app_account_id;
	}
	
	public boolean isAvailability() {
		return isAvailability;
	}
	
	public void setAvailability(boolean isAvailability) {
		this.isAvailability = isAvailability;
	}
}
