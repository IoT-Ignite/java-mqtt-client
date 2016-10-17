package com.ardic.mqtt.agent.contentagent;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.agent.contentagent.model.ContentPush;
import com.ardic.mqtt.agent.contentagent.processor.DownloadProcessor;
import com.ardic.mqtt.client.exception.ServiceNotAvailableException;
import com.ardic.mqtt.client.listener.CloudMessageListener;
import com.ardic.mqtt.client.model.CloudMessage;
import com.ardic.mqtt.client.model.MessageBody;
import com.ardic.mqtt.client.service.CloudMessageService;
import com.ardic.mqtt.client.service.MessagePublisherService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ContentAgent implements CloudMessageListener {

	private static ContentAgent service = null;
	private Logger logger = LoggerFactory.getLogger(ContentAgent.class);

	public void onCloudMessageReceive(String message) {

		ContentPush[] contentPushMessage = new Gson().fromJson(new Gson().fromJson(new Gson().fromJson(message, CloudMessage.class).getBody().get(0), MessageBody.class)
				.getParams(), ContentPush[].class);
		boolean result = false;
		for (ContentPush contentPush : contentPushMessage) {
			DownloadProcessor processor = new DownloadProcessor(contentPush);
			 result = processor.process();
		}
		if (result) {
			JsonObject responseJson = new JsonObject();
			responseJson.addProperty("status", "Sucessfully downloaded");
			try {
				MessagePublisherService.getInstance().publishLevel3Message(new JSONObject(message).getJSONObject("header").getString("msgId"), "PUSH_CMD_STORE__SAVE_CONTENT",
						responseJson);
			} catch (ServiceNotAvailableException e) {
				logger.error("ServiceNotAvailableException", e);
				e.printStackTrace();
			} catch (JSONException e) {
				logger.error("JSONException", e);
			}
		}
	}

	public static ContentAgent getInstance(){
		if (service == null) {
			service = new ContentAgent();
		}
		return service;
	}

	private ContentAgent() {
		CloudMessageService cloudMessageService;
		try {
			cloudMessageService = CloudMessageService.getInstance();
			List<String> serviceList = new ArrayList<String>();
			serviceList.add("PUSH_CMD_STORE__SAVE_CONTENT");
			cloudMessageService.addListener(this, serviceList);
		} catch (ServiceNotAvailableException e) {
			logger.error("Cannot register to CloudMessageService",e);
		}

	}

}
