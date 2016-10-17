package com.ardic.mqtt.client.model;

public class AuthCredentials {

	private String clientId;
	private String username;
	private char[] password;
	
	public AuthCredentials(String clientId, String username, String password) {
		this.clientId = clientId;
		this.username = username;
		this.password = password.toCharArray();
	}

	public String getClientId() {
		return clientId;
	}

	public String getUsername() {
		return username;
	}

	public char[] getPassword() {
		return password;
	}
}
