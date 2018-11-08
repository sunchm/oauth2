package com.oauth2.entity;

import java.io.Serializable;

public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long key_id;
	private String salt;
	private String password;
	
	public Long getKey_id() {
		return key_id;
	}
	public void setKey_id(Long key_id) {
		this.key_id = key_id;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	} 

    
}
