package com.ardic.mqtt.client.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.client.exception.ServiceNotAvailableException;
import com.ardic.mqtt.client.model.AuthCredentials;
import com.ardic.mqtt.client.model.Dms;
import com.ardic.mqtt.client.wsclient.DmsInformationServiceClient;

public class SessionService {

	private static SessionService service;
	private static MqttClient mqttClient;
	private final String clientId;
	private final String username;
	private final char[] password;

	private Logger logger = LoggerFactory.getLogger(SessionService.class);

	private SessionService(AuthCredentials credentials) {
		clientId = credentials.getClientId();
		username = credentials.getUsername();
		password = credentials.getPassword();
	}

	public static synchronized SessionService getInstance() throws ServiceNotAvailableException {
		if (service == null) {
			throw new ServiceNotAvailableException();
		}
		return service;
	}

	public static SessionService initiateService(AuthCredentials credentials){
		service = new SessionService(credentials);
		return service;
	}

	public void connect() {

		MemoryPersistence persistence = new MemoryPersistence();
		DmsInformationServiceClient client = new DmsInformationServiceClient();
		Dms dms = client.getAvailableDms(clientId);

		String broker = "ssl://" + dms.getDomain() + ":" + dms.getPort();

		try {
			mqttClient = new MqttClient(broker, clientId, persistence);
			CloudMessageService listener = CloudMessageService.initiateService(clientId);
			mqttClient.setCallback(listener);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setPassword(password);
			connOpts.setUserName(username);
			connOpts.setCleanSession(false);
			mqttClient.setTimeToWait(3000);
			logger.info("Connecting to broker: " + broker);
			mqttClient.connect(connOpts);

			MessagePublisherService.initiateService(mqttClient);

		} catch (MqttException e) {
			logger.error(e.getMessage());
		}

	}

	public void disconnect() {
		try {
			mqttClient.disconnect();
		} catch (MqttException e) {
			logger.error(e.getMessage());
		}
	}

	public String getClientId(){
		return clientId;
	}

	public boolean isConnected(){
		return mqttClient.isConnected();
	}

}
