package com.ardic.mqtt.client.listener;

public interface CloudMessageListener {

	void onCloudMessageReceive(String message);
}
