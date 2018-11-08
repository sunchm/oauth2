package com.oauth2.controller;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.oauth2.entity.Constants;
import com.oauth2.mapper.OAuthDao;
import com.oauth2.redis.RedisConnection;
import com.oauth2.redis.RedisUtils;
import com.oauth2.util.JwtTokenUtil;
import com.oauth2.util.ResponseUtil;

@RestController
public class TokenController {
	
	protected static final Logger logger = LoggerFactory.getLogger("oauth");

    @Autowired
    private OAuthDao oAuthDao;

    @RequestMapping("/access")
    public ModelAndView access(Model model) {
        return new ModelAndView("accesstoken");
    }

    @RequestMapping("/accessToken")
    public Object token(HttpServletRequest request) throws URISyntaxException, OAuthSystemException {

        try {
        	//构建OAuth请求
        	OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
            //权限范围
			String scope = null;
			//第三方重定向地址
			String redirect_uri = null;
			//第三方平台id
			String client_id = oauthRequest.getClientId();
			//第三方平台密码
			String client_secret = oauthRequest.getClientSecret();
			try {
				Map<String, Object> checkOAuthThridparty = oAuthDao.checkOAuthThridparty(client_id, client_secret);
				//检查提交的客户端id是否正确和检查客户端安全KEY是否正确
	            if (checkOAuthThridparty == null) {
	                 return ResponseUtil.getAbnormalResponse(client_id, request.getParameter("grant_type"), Constants.INVALID_CLIENT_ID_SECRET);
	            }else{
	            	scope = (String) checkOAuthThridparty.get("scope");
	            	redirect_uri = (String) checkOAuthThridparty.get("redirect_uri");
	            	if("".equals(oauthRequest.getParam(OAuth.OAUTH_SCOPE)) || oauthRequest.getParam(OAuth.OAUTH_SCOPE) == null){
	            		//什么都不做
	                }else{
	                	if(!scope.equals(oauthRequest.getParam(OAuth.OAUTH_SCOPE))){
	                		return ResponseUtil.getAbnormalResponse(client_id, request.getParameter("grant_type"), Constants.INVALID_SCOPE);
	                	}
	                }
	            }
			} catch (Exception e) {
				logger.error("{}<{}>{}{}",
    					new Object[] { oauthRequest.getClientId(), request.getParameter("grant_type"), "checkOAuthThridparty查询传入的客户端id和secret异常:", e });
	            return ResponseUtil.getAbnormalResponse(oauthRequest.getClientId(), request.getParameter("grant_type"), Constants.SERVER_EXCEPTION);
			}
            
            String refreshToken = null;
            String accessToken = null;
            //当前时间
            long currentTime = System.currentTimeMillis()/1000;
            //授权令牌过期时间
            long accessToken_expireIn = currentTime + Constants.ACCESS_TOKEN_EXPIREIN;
            //更新令牌过期时间
            long refreshToken_expireIn = currentTime + Constants.REFRESH_TOKEN_EXPIREIN;
            //授权码模式AUTHORIZATION_CODE
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
                //校验redirect_uri
                if(!redirect_uri.equals(oauthRequest.getRedirectURI())){
                	return ResponseUtil.getAbnormalResponse(oauthRequest.getClientId(), request.getParameter("grant_type"), Constants.INVALID_PARAMETER);
                }
                
            	String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
            	String app_account_id = RedisUtils.get(RedisConnection.getShardedJedis(), authCode);
            	if(app_account_id == null){
            		logger.info("<{}>获取令牌，无效的authCode:{}", client_id, authCode);
    	            return ResponseUtil.getAbnormalResponse(oauthRequest.getClientId(), request.getParameter("grant_type"), Constants.INVALID_AUTH_CODE);
            	}
            	//移除code,授权码智能用一次
            	RedisUtils.remove(RedisConnection.getShardedJedis(), authCode);
                
                //生成refresh Token
                OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                refreshToken = oauthIssuerImpl.refreshToken();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("REFRESHTOKEN", refreshToken);
                map.put("CLIENT_ID", oauthRequest.getClientId());
                map.put("APP_ACCOUNT_ID", app_account_id);
                map.put("EXPIREIN", String.valueOf(refreshToken_expireIn));
                try {
                	oAuthDao.addRefreshToken(map);
				} catch (Exception e) {
					logger.error("{}<{}>{}{}",
	    					new Object[] { oauthRequest.getClientId(), request.getParameter("grant_type"), "addRefreshToken添加刷新token异常:", e });
		            return ResponseUtil.getAbnormalResponse(oauthRequest.getClientId(), request.getParameter("grant_type"), Constants.SERVER_EXCEPTION);
				}
                Map<String, Object> params = new HashMap<String, Object>();
                //获取过期时间
                params.put("EXPIREIN", accessToken_expireIn);
                params.put("SCOPE", scope);
                params.put("APP_ACCOUNT_ID", app_account_id);
                //生成access Token
                accessToken = JwtTokenUtil.generateToken(params);
            }else
            //客户端模式CLIENT_CREDENTIALS
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.CLIENT_CREDENTIALS.toString())) {
                //校验scope
                if("".equals(oauthRequest.getParam(OAuth.OAUTH_SCOPE)) || oauthRequest.getParam(OAuth.OAUTH_SCOPE) == null){
                	return ResponseUtil.getAbnormalResponse(oauthRequest.getClientId(), request.getParameter("grant_type"), Constants.INVALID_SCOPE);
                }
            	Map<String, Object> params = new HashMap<String, Object>();
            	//获取过期时间
            	params.put("EXPIREIN", accessToken_expireIn);
            	params.put("SCOPE", scope);
            	//生成access Token
            	accessToken = JwtTokenUtil.generateToken(params);
            	//生成OAuth响应
                OAuthResponse response = OAuthASResponse
                        .tokenResponse(HttpServletResponse.SC_OK)
                        .setAccessToken(accessToken)
                        .setExpiresIn(String.valueOf(Constants.ACCESS_TOKEN_EXPIREIN))
                        .buildJSONMessage();

                logger.info("{}<{}>获取令牌，应答信息:{}", client_id, request.getParameter("grant_type"), response.getBody());
                JSONObject json = JSONObject.fromObject(response.getBody());  
                return json;
            }else
            
            //更新令牌 REFRESH_TOKEN
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.REFRESH_TOKEN.toString())) {
            	String refresh_token = oauthRequest.getParam(OAuth.OAUTH_REFRESH_TOKEN);
            	//判断refresh_token有效性(用于刷新access_token)
            	try {
					if (!checkRefreshToken(refresh_token)) {
						logger.info("<{}>刷新访问令牌，无效的refresh_token:{}", client_id, refresh_token);
	    	            return ResponseUtil.getAbnormalResponse(client_id, request.getParameter("grant_type"), Constants.INVALID_REFRESH_TOKEN);
					}
				} catch (Exception e) {
					logger.error("{}<{}>{}{}",
							new Object[] { request.getParameter("client_id"), request.getParameter("grant_type"), "checkRefreshToken查询刷新token异常:", e });
		            return ResponseUtil.getAbnormalResponse(request.getParameter("client_id"), request.getParameter("grant_type"), Constants.SERVER_EXCEPTION);
				}
            	//生成refresh Token
                OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                refreshToken = oauthIssuerImpl.refreshToken();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("REFRESHTOKEN_OLD", refresh_token);
                map.put("REFRESHTOKEN", refreshToken);
                map.put("EXPIREIN", String.valueOf(refreshToken_expireIn));
                String app_account_id;
                try {
                	oAuthDao.updateRefreshToken(map);
					//获取app_account_id
					app_account_id = oAuthDao.findAppAccountIDByRefreshToken(refreshToken);
				} catch (Exception e) {
					logger.error("{}<{}>{}{}",
							new Object[] { request.getParameter("client_id"), request.getParameter("grant_type"), "updateRefreshToken或findAppAccountIDByRefreshToken，更新token或查询用户id异常:", e });
					return ResponseUtil.getAbnormalResponse(request.getParameter("client_id"), request.getParameter("grant_type"), Constants.SERVER_EXCEPTION);
				}
                
                Map<String, Object> params = new HashMap<String, Object>();
                //获取过期时间
                params.put("EXPIREIN", accessToken_expireIn);
                params.put("SCOPE", scope);
                params.put("APP_ACCOUNT_ID", app_account_id);
                //生成access Token
                accessToken = JwtTokenUtil.generateToken(params);
            }
            //生成OAuth响应
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(String.valueOf(Constants.ACCESS_TOKEN_EXPIREIN))
                    .setRefreshToken(refreshToken)
                    .buildJSONMessage();

            logger.info("{}<{}>获取令牌，应答信息:{}", client_id, request.getParameter("grant_type"), response.getBody());
            JSONObject json = JSONObject.fromObject(response.getBody()); 
            return json;
        } catch (OAuthProblemException e) {
        	logger.error("{}<{}>{}[error={}，description={}]",
					new Object[] { request.getParameter("client_id"), request.getParameter("grant_type"), "OAuthProblemException异常:", e.getError(), e.getDescription() });
        	//出错处理
        	return ResponseUtil.getAbnormalResponse(e.getDescription());
        }
    }
    
    /**
     * 校验refreshToken有效性 
     * @param refreshToken
     * @return false为无效
     * @throws Exception
     */
    public boolean checkRefreshToken(String refreshToken) throws Exception{
    	String exp = oAuthDao.checkRefreshToken(refreshToken);
    	if(exp == null){
    		return false;
    	}else{
    		//当前时间
        	long expireIn = System.currentTimeMillis()/1000;
    		if(expireIn > Long.parseLong(exp)){
    			return false;
    		}else{
    			return true;
    		}
    	}
    }

}
