package com.ardic.mqtt.client.listener;

import com.ardic.mqtt.client.model.AgentMessage;

public interface CloudMessageListener {

	void onCloudMessageReceive(AgentMessage agentMessage);
}
