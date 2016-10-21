package com.ardic.mqtt.client.model;

import com.google.gson.JsonElement;

public class AgentMessage {

	private String msgId;
	private JsonElement body;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public JsonElement getBody() {
		return body;
	}

	public void setBody(JsonElement body) {
		this.body = body;
	}
}
