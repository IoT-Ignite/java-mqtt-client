package com.ardic.mqtt.client.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.client.model.AuthCredentials;
import com.ardic.mqtt.client.model.Dms;
import com.ardic.mqtt.client.util.ConfigurationLoader;

public class SessionService {

	private static SessionService service = new SessionService();
	private MqttClient mqttClient;
	private AuthCredentials credentials;

	private Logger logger = LoggerFactory.getLogger(SessionService.class);

	private final Dms dms = new Dms("mqtt-tr.iot-ignite.com", 8883);
	private String broker = "ssl://" + dms.getDomain() + ":" + dms.getPort();
	private MqttConnectOptions connOpts; 

	private SessionService() {
		
		MemoryPersistence persistence = new MemoryPersistence();
		credentials = ConfigurationLoader.getInstance().getCredentials();
		try {
			mqttClient = new MqttClient(broker, credentials.getClientId(), persistence);
			CloudMessageService listener = CloudMessageService.getInstance();
			mqttClient.setCallback(listener);
			connOpts = new MqttConnectOptions();
			connOpts.setPassword(credentials.getPassword());
			connOpts.setUserName(credentials.getUsername());
			connOpts.setCleanSession(false);
			mqttClient.setTimeToWait(3000);
		} catch (MqttException e) {
			logger.error("MqttException on connect", e);
		}
	}

	public static SessionService getInstance() {
		return service;
	}

	public synchronized boolean connect() {

		try {
			logger.info("Connecting to broker: " + broker);
			mqttClient.connect(connOpts);

			MessagePublisherService.getInstance();

		} catch (MqttException e) {
			logger.error("MqttException on connect", e);
		}

		return mqttClient.isConnected();
	}

	public void disconnect() {
		try {
			mqttClient.disconnect();
		} catch (MqttException e) {
			logger.error("MqttException on disconnect", e);
		}
	}

	public MqttClient getMqttClient() {
		return mqttClient;
	}

	public synchronized boolean isConnected() {
		return mqttClient.isConnected();
	}
}
