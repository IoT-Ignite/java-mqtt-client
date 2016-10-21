package com.ardic.mqtt.client.service;

import java.util.Base64;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.client.util.Constants;
import com.google.gson.JsonObject;

public class MessagePublisherService {

	private static MessagePublisherService service = new MessagePublisherService();
	private Logger logger = LoggerFactory.getLogger(MessagePublisherService.class);

	public static MessagePublisherService getInstance() {
		return service;
	}

	private MessagePublisherService() {
		
	}

	public void publishMessage(String topic, String message) {
		try {
			String top = generatePublishTopic(topic);
			logger.info(message);
			SessionService.getInstance().getMqttClient().publish(top, message.getBytes(), 1, false);
		} catch (MqttPersistenceException e) {
			logger.error("MqttPersistenceException", e);
		} catch (MqttException e) {
			logger.error("MqttException", e);
		}
	}

	public void publishLevel2Response(String msgId, String type, boolean success) {
		JsonObject response = new JsonObject();
		response.addProperty("uuid", msgId);
		response.addProperty("executionState", type);
		if (success) {
			response.addProperty("description", Base64.getEncoder().encodeToString("SUCCESSFUL".getBytes()));
		} else {
			response.addProperty("description", Base64.getEncoder().encodeToString("FAIL".getBytes()));
		}
		publishMessage(Constants.TOPIC_QUEUE_RESPONSE, response.toString());
	}

	public void publishLevel3Message(String msgId, String type, boolean result, String responseDetail) {
		JsonObject status = new JsonObject();
		status.addProperty("messageId", msgId);
		status.addProperty("command", type);
		JsonObject response = new JsonObject();
		response.addProperty("code", result ? "200" : "400");
		response.addProperty("detail", responseDetail);
		status.add("data", response);

		publishMessage(Constants.TOPIC_QUEUE_STATUS, status.toString());
	}

	private String generatePublishTopic(String topic) {
		MqttClient client = SessionService.getInstance().getMqttClient();
		return client.getClientId() + "/publish/" + topic;
	}

}
