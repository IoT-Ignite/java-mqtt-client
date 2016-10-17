package com.ardic.mqtt.client.linuxwrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.agent.contentagent.ContentAgent;
import com.ardic.mqtt.agent.sensoragent.SensorAgent;
import com.ardic.mqtt.client.linuxwrapper.utils.ConfigurationLoader;
import com.ardic.mqtt.client.model.AuthCredentials;
import com.ardic.mqtt.client.service.SessionService;

public class MqttClient {

	private static Logger logger;
	private static ConfigurationLoader configs;
	static boolean connectionClose = false;

	public static void main(String[] args) throws InterruptedException {

		logger = LoggerFactory.getLogger(MqttClient.class);

		configs = ConfigurationLoader.getInstance(); 

		AuthCredentials credentials = InitializeAuthCredentials();
		SessionService session = SessionService.initiateService(credentials);
		session.connect();

		initiateAgents();

		while (!connectionClose) {
			Thread.sleep(1000);
		}
		session.disconnect();
		System.exit(0);

	}

	private static AuthCredentials InitializeAuthCredentials() {

		logger.debug("Initializing credentials: ClientId:[" + configs.MQTT_CLIENT_ID + "], Username:[" + configs.MQTT_USERNAME + "]");
		return new AuthCredentials(configs.MQTT_CLIENT_ID, configs.MQTT_USERNAME, configs.MQTT_PASSWORD);
	}

	private static void initiateAgents() {
		logger.info("Content Agent initiating...");
		ContentAgent.getInstance();
		logger.info("Sensor Agent initiating...");
		SensorAgent.getInstance();
		
		
	}

}