package com.ardic.mqtt.client.util;

import java.io.File;

import javax.security.auth.login.CredentialException;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.client.model.AuthCredentials;

public class ConfigurationLoader {

	Logger logger = LoggerFactory.getLogger(ConfigurationLoader.class);

	private AuthCredentials credentials = null;

	public static final ConfigurationLoader service = new ConfigurationLoader();

	public static ConfigurationLoader getInstance() {
		return service;
	}

	private ConfigurationLoader() {
		Configurations configs = new Configurations();
		Configuration config = null;
		try {
			File configurationFile = new File(System.getProperty("mqtt.properties"));
			config = configs.properties(configurationFile);
			if (config.getString("MQTT_PASSWORD") == null || config.getString("MQTT_USERNAME") == null || config.getString("MQTT_CLIENT_ID") == null) {
				logger.error("Crendentials not found.");
				return;
			}
			credentials = new AuthCredentials(config.getString("MQTT_CLIENT_ID"), config.getString("MQTT_USERNAME"), config.getString("MQTT_PASSWORD"));
		} catch (ConfigurationException e) {
			logger.error("ConfigurationLoaderException - mqtt.properties not found.", e);
		}

	}

	public AuthCredentials getCredentials() {
		return credentials;
	}
}
