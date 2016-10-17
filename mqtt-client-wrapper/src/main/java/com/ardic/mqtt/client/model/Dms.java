package com.ardic.mqtt.client.model;

public class Dms {

	private String domain;
	private int port;

	public Dms(String domain, int port) {
		this.domain = domain;
		this.port = port;
	}

	public String getDomain(){
		return domain;
	}

	public int getPort(){
		return port;
	}
}
