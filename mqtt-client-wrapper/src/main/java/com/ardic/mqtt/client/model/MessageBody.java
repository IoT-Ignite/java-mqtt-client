package com.ardic.mqtt.client.model;

import com.google.gson.JsonArray;

public class MessageBody {

	private String type;
	private JsonArray params;

	public JsonArray getParams() {
		return params;
	}

	public void setParams(JsonArray params) {
		this.params = params;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
