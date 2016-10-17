package com.ardic.mqtt.agent.sensoragent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.agent.sensoragent.processor.TemperatureDataCollector;
import com.ardic.mqtt.client.exception.ServiceNotAvailableException;
import com.ardic.mqtt.client.service.MessagePublisherService;

public class SensorAgent {

	private static SensorAgent service = null;
	Logger logger = LoggerFactory.getLogger(SensorAgent.class); 

	public static SensorAgent getInstance() {
		if (service == null) {
			service = new SensorAgent();
		}
		return service;
	}

	private SensorAgent() {
		initializeSensors();
	}

	public void initializeSensors() {

		SensorInventory inventory = SensorInventory.getInstance();

		// Create sensor collectors
		Thread temperature = new Thread(new TemperatureDataCollector());

		// Publish deviceNodeInventory
		MessagePublisherService publisher;
		try {
			publisher = MessagePublisherService.getInstance();

			publisher.publishMessage("DeviceProfile/Status/DeviceNodeInventory", inventory.generateDeviceNodeInventory().toString());

		} catch (ServiceNotAvailableException e) {
			logger.error("Message Publisher service is not initialized.", e);
		}

		temperature.run();
	}
}
