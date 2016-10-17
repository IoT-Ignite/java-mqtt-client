package com.ardic.mqtt.client.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.client.exception.ServiceNotAvailableException;
import com.ardic.mqtt.client.listener.CloudMessageListener;
import com.ardic.mqtt.client.util.Constants;

public class CloudMessageService implements MqttCallback {

	private static CloudMessageService service = null;
	private Logger logger = LoggerFactory.getLogger(CloudMessageService.class);
	private Map<CloudMessageListener, List<String>> agentList;
	private Map<CloudMessageListener, List<String>> responseListenersList;
	private String clientId;

	private CloudMessageService(String clientId) {
		agentList = new HashMap<CloudMessageListener, List<String>>();
		responseListenersList = new HashMap<CloudMessageListener, List<String>>();
		this.clientId = clientId;

	}

	public static CloudMessageService initiateService(String clientId) {
		service = new CloudMessageService(clientId);
		return service;
	}

	public static CloudMessageService getInstance() throws ServiceNotAvailableException {
		if (service == null) {
			throw new ServiceNotAvailableException();
		}
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
		} catch (ServiceNotAvailableException e) {
			logger.error("SessionService is not available, terminating...", e);
		} catch (InterruptedException e) {
			logger.error("Thread exception, terminating...", e);
		}
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		logger.info("Topic: " + topic);
		logger.info(new String(message.getPayload()));

		String shortTopic = topic.replace(clientId + "/subscribe/", "");

		JSONObject cloudMessage = new JSONObject(new String(message.getPayload()));
		logger.info("shortTopic: " + shortTopic);

		if (shortTopic.equals(Constants.TOPIC_QUEUE)) {
			String messageType = ((JSONObject) (cloudMessage.getJSONArray("body").get(0))).getString("type");
			String messageId = ((JSONObject) (cloudMessage.getJSONObject("header"))).getString("msgId");
			logger.info("message Type: " + messageType);
			boolean messageSent = false;
			for (Map.Entry<CloudMessageListener, List<String>> listener : agentList.entrySet()) {
				if (listener.getValue().contains(messageType)) {
					listener.getKey().onCloudMessageReceive(new String(message.getPayload()));
					messageSent = true;
				}
			}
			MessagePublisherService.getInstance().publishLevel2Response(messageId, messageType, messageSent);
		} else if (shortTopic.equals(Constants.TOPIC_ERROR)) {
			logger.debug(cloudMessage.toString());
		} else {
			for (Map.Entry<CloudMessageListener, List<String>> listener : responseListenersList.entrySet()) {
				if (listener.getValue().contains(shortTopic)) {
					listener.getKey().onCloudMessageReceive(new String(message.getPayload()));
				}
			}
		}

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
