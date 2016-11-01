package com.ardic.mqtt.agent.osprofileagent;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.agent.osprofileagent.model.CloudMessagePackage;
import com.ardic.mqtt.agent.osprofileagent.model.OSProfile;
import com.ardic.mqtt.agent.osprofileagent.processor.OSProfileCollector;
import com.ardic.mqtt.client.listener.CloudMessageListener;
import com.ardic.mqtt.client.model.AgentMessage;
import com.ardic.mqtt.client.service.CloudMessageService;
import com.ardic.mqtt.client.service.MessagePublisherService;
import com.google.gson.Gson;

public class OSProfileAgent implements CloudMessageListener {

	private static OSProfileAgent service = new OSProfileAgent();
	Logger logger = LoggerFactory.getLogger(OSProfileAgent.class);

	private OSProfileAgent() {
		CloudMessageService cloudMessageService = CloudMessageService.getInstance();
		List<String> serviceList = new ArrayList<String>();
		serviceList.add("PUSH_CMD_GET_CURRENT_STATUS");
		cloudMessageService.addListener(this, serviceList);
		sendOSProfile();

	}

	public static OSProfileAgent getInstance() {
		return service;
	}

	@Override
	public void onCloudMessageReceive(AgentMessage agentMessage) {
		logger.info("OSProfile requested...");
		boolean result = sendOSProfile();
		MessagePublisherService publisher = MessagePublisherService.getInstance();
		publisher.publishLevel3Message(agentMessage.getMsgId(), agentMessage.getBody().getAsJsonObject().get("type").getAsString(), result, null);

	}

	private boolean sendOSProfile() {
		OSProfileCollector collector = new OSProfileCollector();
		OSProfile profile = collector.getOSProfile();
		CloudMessagePackage message = new CloudMessagePackage();

		message.setData(profile);

		MessagePublisherService publisher = MessagePublisherService.getInstance();
		publisher.publishMessage("DeviceProfile/Status/OSProfile", new Gson().toJson(message));
		return true;

	}
}
