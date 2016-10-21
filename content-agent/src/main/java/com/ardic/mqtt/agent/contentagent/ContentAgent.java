package com.ardic.mqtt.agent.contentagent;

import java.util.ArrayList;
import java.util.List;

import com.ardic.mqtt.agent.contentagent.model.ContentPush;
import com.ardic.mqtt.agent.contentagent.processor.DownloadProcessor;
import com.ardic.mqtt.client.listener.CloudMessageListener;
import com.ardic.mqtt.client.model.AgentMessage;
import com.ardic.mqtt.client.service.CloudMessageService;
import com.ardic.mqtt.client.service.MessagePublisherService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ContentAgent implements CloudMessageListener {

	private static ContentAgent service = new ContentAgent();

	private ContentAgent() {
		CloudMessageService cloudMessageService;

		cloudMessageService = CloudMessageService.getInstance();
		List<String> serviceList = new ArrayList<String>();
		serviceList.add("PUSH_CMD_STORE__SAVE_CONTENT");
		cloudMessageService.addListener(this, serviceList);

	}

	public static ContentAgent getInstance() {
		return service;
	}

	public void onCloudMessageReceive(AgentMessage message) {

		ContentPush[] contentPushMessage = new Gson().fromJson(((JsonObject) message.getBody()).get("params"), ContentPush[].class);
		boolean result = false;
		String errorReport = "";
		for (ContentPush contentPush : contentPushMessage) {
			DownloadProcessor processor = new DownloadProcessor(contentPush);
			result = processor.process();
			if (!result) {
				errorReport = processor.getErrorMessage() + "\n";
			}
		}
		MessagePublisherService.getInstance().publishLevel3Message(message.getMsgId(), "PUSH_CMD_STORE__SAVE_CONTENT", result, errorReport);
	}

}
