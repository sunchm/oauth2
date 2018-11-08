package com.oauth2.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oauth2.entity.Constants;
import com.oauth2.entity.User;
import com.oauth2.mapper.OAuthDao;
import com.oauth2.redis.RedisConnection;
import com.oauth2.redis.RedisUtils;
import com.oauth2.util.ResponseUtil;
import com.oauth2.util.SaltUtils;

@Controller
public class AuthorizeController {

	protected static final Logger logger = LoggerFactory.getLogger("oauth");
	
    @Autowired
    private OAuthDao oAuthDao;

    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/authorize")
    public Object authorize(HttpServletRequest request)throws URISyntaxException, OAuthSystemException {
        try {
        	if(request.getParameter("st") != null){
        		//记录时间戳 用于未登录非第一次使用
        		request.setAttribute("st", request.getParameter("st"));
        	}
        	if(request.getParameter("flag") == null) {
        		String st = String.valueOf(System.currentTimeMillis() / 1000);
        		request.setAttribute("st", st);
        		logger.info("时间戳：{}---<{}>进行授权请求：client_id:{}，response_type:{}，scope:{}，state:{}，redirect_uri:{}", st, request.getParameter("client_id"), request.getParameter("client_id"), 
        				request.getParameter("response_type"), request.getParameter("scope"), request.getParameter("state"), request.getParameter("redirect_uri"));
        	}
            //构建OAuth授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
         
            //校验client_id
            try {
            	Map<String, Object> checkOAuthThridparty = oAuthDao.checkOAuthThridparty(oauthRequest.getClientId(), null);
            	//检查传入的客户端id是否正确
            	if(checkOAuthThridparty == null){
            		return ResponseUtil.getAbnormalResponse(oauthRequest.getClientId(), request.getParameter("response_type"), Constants.INVALID_CLIENT_ID);
            	}
            } catch (Exception e) {
            	logger.error("{}<{}>{}{}",
    					new Object[] { oauthRequest.getClientId(), request.getParameter("response_type"), "checkOAuthThridparty查询传入的客户端id异常:", e });
            	return ResponseUtil.getAbnormalResponse(oauthRequest.getClientId(), request.getParameter("response_type"), Constants.SERVER_EXCEPTION);
            }
            //校验redirect_uri
            if("".equals(oauthRequest.getRedirectURI()) || oauthRequest.getRedirectURI() == null){
            	return ResponseUtil.getAbnormalResponse(oauthRequest.getClientId(), request.getParameter("response_type"), Constants.INVALID_PARAMETER);
            }
            //校验state
            if("".equals(oauthRequest.getState()) || oauthRequest.getState() == null){
            	return ResponseUtil.getAbnormalResponse(oauthRequest.getClientId(), request.getParameter("response_type"), Constants.INVALID_PARAMETER);
            }
            
            //如果用户没有登录，跳转到登陆页面
            if(!login(request)) {//登录失败时跳转到登陆页面
            	if(request.getParameter("flag") == null) {
            		logger.info("时间戳：{}---<{}>已授权，需登录获取授权码", String.valueOf(request.getAttribute("st")), request.getParameter("client_id"));
            	}
                return "login";
            }
            String app_account_id = String.valueOf(request.getAttribute("app_account_id"));
            //生成授权码
            String authorizationCode = null;
            //responseType目前支持CODE和TOKEN
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            if (responseType.equals(ResponseType.CODE.toString())) {
                OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                authorizationCode = oauthIssuerImpl.authorizationCode();
                RedisUtils.setex(RedisConnection.getShardedJedis(), authorizationCode, app_account_id);
            }
            //进行OAuth响应构建
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
            //设置授权码
            builder.setCode(authorizationCode);
            builder.setParam("state", request.getParameter("state"));
            //得到到客户端重定向地址
            String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            //构建响应
            final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
            //根据OAuthResponse返回ResponseEntity响应
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri()));
			@SuppressWarnings("rawtypes")
			HttpEntity httpEntity = new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
            logger.info("时间戳：{}---<{}>获取授权码：{}，授权应答参数httpEntity：{}", request.getParameter("st"), request.getParameter("client_id"), authorizationCode, httpEntity);
            return httpEntity;
        } catch (OAuthProblemException e) {
        	logger.error("{}<{}>{}[error={}，description={}]",
					new Object[] { request.getParameter("client_id"), request.getParameter("response_type"), "OAuthProblemException异常:", e.getError(), e.getDescription() });
        	//出错处理
        	return ResponseUtil.getAbnormalResponse(e.getDescription());
        }
    }
    
    private boolean login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String flag = request.getParameter("flag");

        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
        	if(flag != null && "1".equals(flag)){
        		request.setAttribute("error","用户名密码为空");
        	}
            return false;
        }
        try {
            //查询用户信息
            User user = oAuthDao.findByUsername(username);
            //密码进行sha256 base64加密
            password = SaltUtils.encryptPassword(password);
            if(user!=null){
            	if (!SaltUtils.verification(password, user.getSalt(), user.getPassword())) {
            		request.setAttribute("error","密码不正确");
            		return false;
    			}else{
    				request.setAttribute("app_account_id", user.getKey_id());
    				return true;
                }
            }else{
                request.setAttribute("error","用户名不正确");
                return false;
            }
        } catch (Exception e) {
        	logger.error("{}<{}>{}{}",
					new Object[] { request.getParameter("client_id"), request.getParameter("response_type"), "findByUsername查询用户信息异常:", e });
            return false;
        }
    }
}