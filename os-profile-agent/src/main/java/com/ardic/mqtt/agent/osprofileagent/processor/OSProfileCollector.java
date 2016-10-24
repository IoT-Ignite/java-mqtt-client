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
		profile.setAfexMode("Light Gateway");
		profile.setDevice(System.getProperty("os.name") + " " + System.getProperty("os.arch"));
		profile.setOsName(System.getProperty("os.name"));
		profile.setOsVersion(System.getProperty("os.version"));
		profile.setDeviceId(SessionService.getInstance().getMqttClient().getClientId());
		try {
			profile.setLocalIp(Inet4Address.getLocalHost().getHostAddress());
			profile.setHost(Inet4Address.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			logger.error("Cannot get Local IP Address",e);
		}
		
		return profile;
	}
}