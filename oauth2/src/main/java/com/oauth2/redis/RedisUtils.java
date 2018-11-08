package com.oauth2.redis;

import redis.clients.jedis.JedisCluster;

import com.oauth2.entity.Constants;

public class RedisUtils {
	
	//存储带过期时间的键值对
	public static void setex(JedisCluster jedis, String key, String value) {
		try {
			jedis.setex(key, Constants.CODE_EXPIREIN, value);
		} catch (Exception e) {
			// 释放redis对象
			e.printStackTrace();
		}
	}
	
	public static String get(JedisCluster jedis, String key){
		String result = null;
		try{
			result = jedis.get(key);
		}catch (Exception e) {
			// 释放redis对象
			e.printStackTrace();
		}
		return result;
	}
	
	public static void remove(JedisCluster jedis, String key) {
		try {
			jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
