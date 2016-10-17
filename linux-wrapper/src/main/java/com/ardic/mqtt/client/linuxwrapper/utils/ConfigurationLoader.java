package com.ardic.mqtt.client.linuxwrapper.utils;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationLoader {

	Logger logger = LoggerFactory.getLogger(ConfigurationLoader.class);

	public String MQTT_USERNAME;
	public String MQTT_PASSWORD;
	public String MQTT_CLIENT_ID;

	public static ConfigurationLoader service = null;

	public static ConfigurationLoader getInstance() {
		if (service == null) {
			service = new ConfigurationLoader();
		}
		return service;
	}

	private ConfigurationLoader() {
		Configurations configs = new Configurations();
		Configuration config = null;
		try {
			File test = new File(System.getProperty("mqtt.properties"));
			config = configs.properties(test);
		} catch (ConfigurationException e) {
			logger.error("ConfigurationLoaderException", e);
		}

		MQTT_USERNAME = config.getString("MQTT_USERNAME", "mqtt_test");
		MQTT_PASSWORD = config.getString("MQTT_PASSWORD", "a12345");
		MQTT_CLIENT_ID = config.getString("MQTT_CLIENT_ID", "mqtt-test-0913");
	}
}
