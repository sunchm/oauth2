package com.oauth2.entity;

public class Constants {
	//错误码
	public static final String INVALID_RESPONSE_TYPE = "100901";
	public static final String INVALID_SCOPE = "100902";
	public static final String INVALID_ACCESS_TOKEN = "100903";
    public static final String INVALID_CLIENT_ID = "100904";
    public static final String INVALID_CLIENT_ID_SECRET = "100905";
    public static final String INVALID_AUTH_CODE = "100906";
    public static final String INVALID_REFRESH_TOKEN = "100907";
    public static final String INVALID_PARAMETER = "100400";
    public static final String SERVER_EXCEPTION= "100500";
    public static final String REQUEST_OVERTIME= "100201";
    //错误码描述
    public static final String INVALID_RESPONSE_TYPE_DESCRIBE = "授权服务器不支持客户端所指定的response_type";
    public static final String INVALID_SCOPE_DESCRIBE = "scope无效";
    public static final String INVALID_ACCESS_TOKEN_DESCRIBE = "accessToken无效或已过期";
    public static final String INVALID_CLIENT_ID_DESCRIBE = "client_id不存在";
    public static final String INVALID_CLIENT_ID_SECRET_DESCRIBE = "client_id和client_secret不匹配";
    public static final String INVALID_AUTH_CODE_DESCRIBE = "授权码无效或已过期";
    public static final String INVALID_REFRESH_TOKEN_DESCRIBE = "refresh_token无效或已过期";
    public static final String INVALID_PARAMETER_DESCRIBE = "请求参数不符合协议规范";
    public static final String SERVER_EXCEPTION_DESCRIBE = "服务器异常";
    public static final String REQUEST_OVERTIME_DESCRIBE = "请求超时";

    //秘钥
    public static String SECRET;
    //授权码过期时间
    public static int CODE_EXPIREIN;
    //refresh_token过期时间
    public static Long REFRESH_TOKEN_EXPIREIN;
    //access_token过期时间
    public static Long ACCESS_TOKEN_EXPIREIN;
    
	public static void setSECRET(String sECRET) {
		SECRET = sECRET;
	}
	public static void setCODE_EXPIREIN(int cODE_EXPIREIN) {
		CODE_EXPIREIN = cODE_EXPIREIN;
	}
	public static void setREFRESH_TOKEN_EXPIREIN(Long rEFRESH_TOKEN_EXPIREIN) {
		REFRESH_TOKEN_EXPIREIN = rEFRESH_TOKEN_EXPIREIN;
	}
	public static void setACCESS_TOKEN_EXPIREIN(Long aCCESS_TOKEN_EXPIREIN) {
		ACCESS_TOKEN_EXPIREIN = aCCESS_TOKEN_EXPIREIN;
	}
}
