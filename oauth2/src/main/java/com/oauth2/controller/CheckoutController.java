package com.oauth2.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.oauth2.util.JwtTokenUtil;

@RestController
public class CheckoutController {
	protected static final Logger logger = LoggerFactory.getLogger("oauth");
	
	@RequestMapping("/checkoutToken")
    public String checkoutToken(HttpServletRequest request) {
		String accessToken = request.getParameter("accessToken");
		JSONObject json = new JSONObject();
		Map<String, Object> map = JwtTokenUtil.analysisToken(accessToken, 1);
		if(!JwtTokenUtil.checkAccessToken(accessToken)) {
			String aud = String.valueOf(map.get("aud"));
			String scp = String.valueOf(map.get("scp"));
			String app_account_id = String.valueOf(map.get("sub"));
			logger.info("<checkoutToken>accessToken有效：^o^---aud：{}---scp：{}---accessToken：{}", aud, scp, accessToken);
			json.put("flag", true);
			json.put("app_account_id", app_account_id);
		}else {
			String exp = String.valueOf(map.get("exp"));
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        long lt = new Long(exp);
	        Date date = new Date(lt);
	        exp = simpleDateFormat.format(date);
			logger.info("<checkoutToken>accessToken无效-_- @过期时间：{}---accessToken：{}", exp, accessToken);
			json.put("flag", false);
		}
		return json.toJSONString();
	}

}
