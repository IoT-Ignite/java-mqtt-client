package com.ardic.mqtt.client.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.client.listener.CloudMessageListener;
import com.ardic.mqtt.client.model.AgentMessage;
import com.ardic.mqtt.client.model.CloudMessage;
import com.ardic.mqtt.client.util.ConfigurationLoader;
import com.ardic.mqtt.client.util.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CloudMessageService implements MqttCallback {

	private static CloudMessageService service = new CloudMessageService();
	private Logger logger = LoggerFactory.getLogger(CloudMessageService.class);
	private Map<CloudMessageListener, List<String>> agentList;
	private Map<CloudMessageListener, List<String>> responseListenersList;
	private String clientId;

	private CloudMessageService() {
		agentList = new HashMap<CloudMessageListener, List<String>>();
		responseListenersList = new HashMap<CloudMessageListener, List<String>>();
		ConfigurationLoader configs = ConfigurationLoader.getInstance();
		this.clientId = configs.getCredentials().getClientId();

	}

	public static CloudMessageService getInstance() {
		return service;
	}

	public void connectionLost(Throwable cause) {
		logger.info("Connection lost.", cause);
		logger.info("Trying to reconnect");
		try {
			int attempt = 1;
			int waitTime = 1000;
			do {
				logger.info("Attempt #" + attempt);
				Thread.sleep(waitTime);
				SessionService.getInstance().connect();
				waitTime *= 1.2;
				attempt++;
			} while (!SessionService.getInstance().isConnected());
		} catch (InterruptedException e) {
			logger.error("Thread exception, terminating...", e);
			Thread.currentThread().interrupt();
		}
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		logger.info("Topic: " + topic);
		logger.info(new String(message.getPayload()));

		String shortTopic = topic.replace(clientId + "/subscribe/", "");

		CloudMessage cloudMessage = new Gson().fromJson(new String(message.getPayload()), CloudMessage.class);

		if (shortTopic.equals(Constants.TOPIC_QUEUE)) {
			String messageType = ((JsonObject) cloudMessage.getBody().get(0)).get("type").getAsString();
			String messageId = cloudMessage.getHeader().getMsgId();

			boolean messageSent = false;
			for (Map.Entry<CloudMessageListener, List<String>> listener : agentList.entrySet()) {
				if (listener.getValue().contains(messageType)) {
					listener.getKey().onCloudMessageReceive(simplifyMessage(cloudMessage));
					messageSent = true;
				}
			}
			MessagePublisherService.getInstance().publishLevel2Response(messageId, messageType, messageSent);
		} else if (shortTopic.equals(Constants.TOPIC_ERROR)) {
			logger.debug(cloudMessage.toString());
		} else {
			for (Map.Entry<CloudMessageListener, List<String>> listener : responseListenersList.entrySet()) {
				if (listener.getValue().contains(shortTopic)) {
					listener.getKey().onCloudMessageReceive(simplifyMessage(cloudMessage));
				}
			}
		}

	}

	private AgentMessage simplifyMessage(CloudMessage cloudMessage){
		AgentMessage agentMessage = new AgentMessage();
		agentMessage.setBody((JsonObject) cloudMessage.getBody().get(0));
		agentMessage.setMsgId(cloudMessage.getHeader().getMsgId());
		return agentMessage;
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		logger.info("Message id: " + token.getMessageId());

	}

	public void addListener(CloudMessageListener listener, List<String> typeList) {
		agentList.put(listener, typeList);
	}

	public void addResponseListener(CloudMessageListener listener, List<String> topicList) {
		responseListenersList.put(listener, topicList);
	}
}
