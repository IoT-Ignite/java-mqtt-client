package com.ardic.mqtt.agent.contentagent.processor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.agent.contentagent.model.ContentPush;

public class DownloadProcessor {

	private URL url;
	private String fileName;
	private String token;
	private String destination;
	private String errorMessage;
	private Logger logger = LoggerFactory.getLogger(DownloadProcessor.class);

	public DownloadProcessor(ContentPush content) {
		try {
			url = new URL(content.getUrl());
		} catch (MalformedURLException e) {
			logger.error("Malformed url", e);
		}
		fileName = content.getFileName();
		destination = content.getDestinationPath();
		token = content.getToken();
	}

	public boolean process() {

		HttpsURLConnection conn = null;
		try {
			conn = (HttpsURLConnection) url.openConnection();
		} catch (IOException e1) {
			logger.error("Cannot open HTTPS connection.", e1);
			return false;
		}

		conn.setRequestProperty("X-Auth-Token", token);
		try (FileOutputStream outputStream = new FileOutputStream(destination + "/" + fileName); InputStream stream = conn.getInputStream()) {
			int bytesRead = -1;
			byte[] buffer = new byte[conn.getContentLength()];
			while ((bytesRead = stream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

		} catch (FileNotFoundException e) {
			errorMessage = "Cannot open destination file"+ destination + "/" + fileName + "for writing.";
			logger.error(errorMessage,e);
			return false;
		} catch (IOException e) {
			errorMessage = "IOError";
			logger.error(errorMessage,e);
			return false;
		}

		return true;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
