package com.ardic.mqtt.agent.contentagent.processor;

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
		try {
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestProperty("X-Auth-Token", token);
			InputStream stream = conn.getInputStream();
			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(destination + "/" + fileName);

			int bytesRead = -1;
			byte[] buffer = new byte[conn.getContentLength()];
			while ((bytesRead = stream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			outputStream.close();

		} catch (IOException e) {
			logger.error("File IO error: ", e);
			return false;
		}

		return true;
	}
}
