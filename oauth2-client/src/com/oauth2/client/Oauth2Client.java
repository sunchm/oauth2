package com.oauth2.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.oauth2.client.entity.Jwt;
import com.oauth2.client.entity.OAuthToken;
import com.oauth2.client.util.HttpsUtil;

public class Oauth2Client {
	
    private final static int CONNECTTIMEOUT = 5000;
    private final static int READTIMEOUT = 5000;
    private final static String CHECKOUT_TOKEN_URL = "http://localhost:8010/oauth2/checkoutToken";
    private final static String GENERATE_TOKEN_URL = "http://localhost:8010/oauth2/accessToken";
    private final static String CLIENT_ID = "oauth_clientid";
    private final static String CLIENT_SECRET = "oauth_clientsecret";
    private final static String SCOPE = "scp01001";
    private final static String REDIRECT_URI = "https://www.baidu.com";

    /**
     * 客户端模式使用应用公钥、密钥获取accessToken 使用默认参数
     * @return accessToken
     */
    public static OAuthToken getAccessTokenByClientCredentials(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "client_credentials");
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        params.put("scope", SCOPE);
        return generateAccessToken(params);
    }
    
    /**
     * 授权码模式使用应用公钥、密钥获取accessToken 使用默认参数
     * @return accessToken
     */
    public static OAuthToken getAccessTokenByAuthorizationCode(String code){
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("grant_type", "authorization_code");
    	params.put("client_id", CLIENT_ID);
    	params.put("client_secret", CLIENT_SECRET);
    	params.put("scope", SCOPE);
    	params.put("code", code);
    	params.put("redirect_uri", REDIRECT_URI);
    	return generateAccessToken(params);
    }
    
    /**
     * 获取accessToken  使用url默认参数
     * @param params 请求方式（授权码：authorize_code，客户端：client_credentials，刷新访问令牌：refresh_token）相关参数
     * @return 授权token相关信息
     */
	public static OAuthToken generateAccessToken(Map<String, String> params){
        return generateAccessToken(GENERATE_TOKEN_URL, params);
    }
    
    
    /**
     * 客户端模式使用应用公钥、密钥获取accessToken 手动传入
     * @return accessToken
     */
    public static OAuthToken getAccessTokenByClientCredentials(String url, Map<String, String> params){
    	params.put("grant_type", params.get("grant_type"));
    	params.put("client_id", params.get("client_id"));
    	params.put("client_secret", params.get("client_secret"));
    	params.put("scope", params.get("scope"));
    	return generateAccessToken(url, params);
    }
    
    /**
     * 授权码模式使用应用公钥、密钥获取accessToken 手动传入
     * @return accessToken
     */
    public static OAuthToken getAccessTokenByAuthorizationCode(String url, Map<String, String> params){
    	params.put("grant_type", params.get("grant_type"));
    	params.put("client_id", params.get("client_id"));
    	params.put("client_secret", params.get("client_secret"));
    	params.put("scope", params.get("scope"));
    	params.put("code", params.get("code"));
    	params.put("redirect_uri", params.get("redirect_uri")); 
    	return generateAccessToken(url, params);
    }
    
    /**
     * 获取accessToken
     * @param url 获取token请求地址 
     * @param params 请求方式（授权码：authorize_code，客户端：client_credentials，刷新访问令牌：refresh_token）相关参数
     * @return 授权token相关信息
     */
	public static OAuthToken generateAccessToken(String url, Map<String, String> params){
		OAuthToken accessToken = null;
		String jsonResult = null;
		try {
			jsonResult = HttpsUtil.doPost(url, params, CONNECTTIMEOUT, READTIMEOUT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (jsonResult != null) {
            accessToken = new OAuthToken(jsonResult);
        }
        return accessToken;
    }
	
	/**
     * 检验accessToken是否有效，默认url flag=true为有效
     */
    public static Jwt checkoutAccessToken(String accessToken){
    	return checkoutAccessToken(CHECKOUT_TOKEN_URL, accessToken);
    }
    
    /**
     * 检验accessToken是否有效  手动传入url flag=true为有效
     */
    public static Jwt checkoutAccessToken(String url, String accessToken){
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("accessToken", accessToken);
    	Jwt jwt = null;
    	String jsonResult = null;
    	try {
    		jsonResult = HttpsUtil.doGet(url, params);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	if (jsonResult != null) {
    		jwt = new Jwt(jsonResult);
        }
        return jwt;
    }
    
    public static void main(String[] args) {
    	//授权码模式生成token code通过登陆获取
    	String code = "22357b2dde555e7dc7d621a092fa5969";
    	OAuthToken oauthToken_AuthorizationCode = Oauth2Client.getAccessTokenByAuthorizationCode(code);
    	String token1 = oauthToken_AuthorizationCode.getAccessToken();
    	System.out.println("token1：" + token1);
		//检验token是否有效
		Jwt jwt1 = Oauth2Client.checkoutAccessToken(token1);
		System.out.println("token1是否有效：" + jwt1.isAvailability() + "---app_account_id：" + jwt1.getApp_account_id());
    	
    	
    	//客户端模式生成token
    	OAuthToken oauthToken_ClientCredentials = Oauth2Client.getAccessTokenByClientCredentials();
		String token2 = oauthToken_ClientCredentials.getAccessToken();
		System.out.println("token2：" + token2);
		//检验token是否有效
		Jwt jwt2 = Oauth2Client.checkoutAccessToken(token2);
		System.out.println("token2是否有效：" + jwt2.isAvailability() + "---app_account_id为空是正确的：" + jwt2.getApp_account_id());
	}
    
}
