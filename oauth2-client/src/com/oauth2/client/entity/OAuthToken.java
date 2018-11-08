package com.oauth2.client.entity;

import org.json.simple.JSONObject;

import com.oauth2.client.util.JsonUtil;

public class OAuthToken {
    /**
     * 过期时间
     */
    private int expiresIn;

    /**
     * 刷新token的令牌
     */
    private String refreshToken;

    /**
     * accessToken
     */
    private String accessToken;
    
    /**
     * 构造函数
     */
    public OAuthToken(String json){
    	JSONObject obj = JsonUtil.parseJson(json);
        if (obj != null) {
            Object objExpire = obj.get("expires_in");
            Object objRefresh = obj.get("refresh_token");
            Object objAccess = obj.get("access_token");
            if (objAccess != null) {
                this.setAccessToken(objAccess.toString());
            }
            if (objExpire != null) {
                this.setExpiresIn(Integer.valueOf(objExpire.toString()));
            }
            if (objRefresh != null) {
                this.setRefreshToken(objRefresh.toString());
            }
        }
    }

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String toString() {
		return "OAuthToken [expiresIn=" + expiresIn + ", refreshToken=" + refreshToken + ", accessToken=" + accessToken
				+ "]";
	}
}
