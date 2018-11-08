package com.oauth2.util;

import java.util.HashMap;
import java.util.Map;
/**
 * jwt生成token测试
 */
public class JwtTestUtil {

	public static void main(String[] args) {
		
		//生成accessToken  System.currentTimeMillis()/1000：秒
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("EXPIREIN", System.currentTimeMillis()/1000 + 30);
        params.put("SCOPE", "scp01001");
        params.put("APP_ACCOUNT_ID", "1908");
        String accessToken = null;
		try {
			accessToken = JwtTokenUtil.generateToken(params);
			System.out.println("生成accessToken：" + accessToken);
			System.out.println("生成accessToken长度：" + accessToken.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//解析token
		Map<String,Object> map = JwtTokenUtil.analysisToken(accessToken, 1);
		System.out.println("app_account_id：" + map.get("sub"));
		
		//验证token是否过期
		if(JwtTokenUtil.checkAccessToken(accessToken)){
			System.out.println("access_token过期");
		}else{
			System.out.println("access_token未过期");
		}
	}
}
