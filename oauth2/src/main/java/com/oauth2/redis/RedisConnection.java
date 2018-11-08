package com.oauth2.redis;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {
	
	// jedispool配置
	private static JedisPoolConfig config;

	private static Integer maxTotal;
	private static Integer maxIdle;
	private static Integer maxWaitMillis;
	private static Integer timeout = 30000;
	private static Integer maxRedirections = 6;
	private static Boolean testOnBorrow = true;
	private static Boolean testOnReturn = true;
	
	private static String url="";
	
    //redis 集群模式Cluster
	private static JedisCluster jedisCluster;
	
	private RedisConnection() {

	}

	public static void initJedisPoolConfig() {
		config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMaxWaitMillis(maxWaitMillis);
		config.setTestOnBorrow(testOnReturn);
		config.setTestOnReturn(testOnReturn);
	}

	public static void init() {
		initJedisPoolConfig();
		Set<HostAndPort> haps = new HashSet<HostAndPort>();
		String[] urls = url.split(";");
		System.out.println("Redis url"+url);
		for (String url : urls) {
			String[] host = url.split(":");
			try{
			HostAndPort hap = new HostAndPort(host[0], Integer.valueOf(host[1]));
			haps.add(hap);
			System.out.println("Redis Server Add--"+hap.getHost()+":"+hap.getPort());
			}catch(Exception e){
			}
		}
		jedisCluster = new JedisCluster(haps, timeout, maxRedirections,config);
	}

	/**
	 * 获取redis连接，使用完后一定要调用close 方法关闭连接
	 * @return
	 */
	public static JedisCluster getShardedJedis() {
		if(jedisCluster!=null){
			return jedisCluster;
		}else{
			return null;
		}
	}

	public static Integer getMaxTotal() {
		return maxTotal;
	}

	public static void setMaxTotal(Integer maxTotal) {
		RedisConnection.maxTotal = maxTotal;
	}

	public static Integer getMaxIdle() {
		return maxIdle;
	}

	public static void setMaxIdle(Integer maxIdle) {
		RedisConnection.maxIdle = maxIdle;
	}

	public static Integer getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public static void setMaxWaitMillis(Integer maxWaitMillis) {
		RedisConnection.maxWaitMillis = maxWaitMillis;
	}

	public static Boolean getTestOnBorrow() {
		return testOnBorrow;
	}

	public static void setTestOnBorrow(Boolean testOnBorrow) {
		RedisConnection.testOnBorrow = testOnBorrow;
	}

	public static Boolean getTestOnReturn() {
		return testOnReturn;
	}

	public static void setTestOnReturn(Boolean testOnReturn) {
		RedisConnection.testOnReturn = testOnReturn;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		RedisConnection.url = url;
	}

	public static Integer getTimeout() {
		return timeout;
	}

	public static void setTimeout(Integer timeout) {
		RedisConnection.timeout = timeout;
	}

	public static Integer getMaxRedirections() {
		return maxRedirections;
	}

	public static void setMaxRedirections(Integer maxRedirections) {
		RedisConnection.maxRedirections = maxRedirections;
	}

}