package com.oauth2.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.oauth2.entity.User;

@Repository
public interface OAuthDao {

    //添加 refresh token
    public void addRefreshToken(Map<String, Object> params) throws Exception;

    //验证refresh token是否有效
    public String checkRefreshToken(@Param("REFRESHTOKEN")String refreshToken) throws Exception;
    
    //更新refresh token
    public void updateRefreshToken(Map<String, Object> params) throws Exception;
    
    //查询用户信息
    public User findByUsername(@Param("USERNAME")String username) throws Exception;
    
    //查询app_account_id
    public String findAppAccountIDByRefreshToken(@Param("REFRESHTOKEN")String refreshToken) throws Exception;
    
    //检验第三方用户合法性
    public Map<String, Object> checkOAuthThridparty(@Param("CLIENT_ID")String client_id, @Param("CLIENT_SECRET")String client_secret) throws Exception;

}
