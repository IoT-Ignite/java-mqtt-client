package com.ardic.mqtt.agent.sensoragent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.agent.sensoragent.processor.TemperatureDataCollector;
import com.ardic.mqtt.client.service.MessagePublisherService;
import com.google.gson.Gson;

public class SensorAgent {

	private static SensorAgent service = new SensorAgent();
	Logger logger = LoggerFactory.getLogger(SensorAgent.class);

	private SensorAgent() {
		initializeSensors();
	}

	public static SensorAgent getInstance() {
		return service;
	}

	public void initializeSensors() {

		SensorInventory inventory = SensorInventory.getInstance();

		// Create sensor collectors
		Thread temperature = new Thread(new TemperatureDataCollector());

		// Publish deviceNodeInventory
		MessagePublisherService publisher;
		publisher = MessagePublisherService.getInstance();
		publisher.publishMessage("DeviceProfile/Status/DeviceNodeInventory", new Gson().toJson(inventory.generateDeviceNodeInventory()));

		temperature.start();
	}
}
