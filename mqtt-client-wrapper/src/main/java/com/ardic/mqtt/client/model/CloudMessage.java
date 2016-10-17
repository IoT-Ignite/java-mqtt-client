package com.ardic.mqtt.client.model;

import com.google.gson.JsonArray;

public class CloudMessage {

	MessageHeader header;
	JsonArray body;

	public JsonArray getBody() {
		return body;
	}

	public void setBody(JsonArray body) {
		this.body = body;
	}

	public MessageHeader getHeader() {
		return header;
	}

	public void setHeader(MessageHeader header) {
		this.header = header;
	}
}
