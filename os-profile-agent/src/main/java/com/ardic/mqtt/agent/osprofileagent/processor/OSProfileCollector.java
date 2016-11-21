package com.ardic.mqtt.agent.osprofileagent.processor;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.agent.osprofileagent.model.OSProfile;
import com.ardic.mqtt.client.service.SessionService;

public class OSProfileCollector {

	Logger logger = LoggerFactory.getLogger(OSProfileCollector.class);

	public OSProfile getOSProfile(){
		OSProfile profile = new OSProfile();
		profile.setAfexMode("LightGateway");
		profile.setModel(System.getProperty("os.name"));
		profile.setDevice(System.getProperty("os.name") + " " + System.getProperty("os.arch"));
		profile.setOsName(System.getProperty("os.name"));
		profile.setOsVersion(System.getProperty("os.version"));
		profile.setDeviceId(SessionService.getInstance().getMqttClient().getClientId());
		
		return profile;
	}
}
