package com.ardic.mqtt.client.linuxwrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.agent.contentagent.ContentAgent;
import com.ardic.mqtt.agent.sensoragent.SensorAgent;
import com.ardic.mqtt.client.service.SessionService;

public class MqttClient {

	private static Logger logger;
	static boolean connectionClose = false;

	private MqttClient() {
	}

	public static void main(String[] args) throws InterruptedException {

		logger = LoggerFactory.getLogger(MqttClient.class);

		SessionService session = SessionService.getInstance();
		if (session.connect()) {

			initiateAgents();

			while (!connectionClose) {
				Thread.sleep(1000);
			}
			session.disconnect();
			System.exit(0);
		}else {
			logger.error("Connection is not initialized.");
			System.exit(1);
		}

	}

	private static void initiateAgents() {
		logger.info("Content Agent initiating...");
		ContentAgent.getInstance();
		logger.info("Sensor Agent initiating...");
		SensorAgent.getInstance();

	}

}