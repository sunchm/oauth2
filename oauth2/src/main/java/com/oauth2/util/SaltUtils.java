package com.oauth2.util;

import java.util.UUID;

import org.bouncycastle.util.encoders.Base64;

public class SaltUtils {

	/**
	 * 生成salt  就是UUID
	 * @return
	 */
	public static String getSaltString(){
		 return UUID.randomUUID().toString(); 
	}

	/**
	 * 
	 * @param password 原始密码
	 * @param salt salt值
	 * @return
	 */
	public static String encryptPassword(String password,String salt){
	    byte[] hashBytes = SHA256Utils.SHA(password + salt);
	    return Base64.toBase64String(hashBytes);  
	}
	
	/**
	 * 
	 * @param password 原始密码
	 * @return
	 */
	public static String encryptPassword(String password){
		byte[] hashBytes = SHA256Utils.SHA(password);
		return Base64.toBase64String(SHA256Utils.bytetoHex(hashBytes).getBytes());  
	}
	
	/**
	 * 校验密码
	 * @param password  原始密码
	 * @param salt  salt值
	 * @param saltPasswd salt加密后的密码
	 * @return
	 */
	public static boolean verification(String password,String salt,String saltPasswd){
		return saltPasswd.equals(encryptPassword(password,salt));
	}
	
	
}
