package com.ardic.mqtt.agent.contentagent.model;

public class ContentPush {

	private String arg;
	private String destinationPath;
	private boolean isZippedContent;
	private String token;
	private String storageType;
	private String fileName;
	private String url;
	private String version;
	private String size;

	public String getArg() {
		return arg;
	}
	public void setArg(String arg) {
		this.arg = arg;
	}
	public String getDestinationPath() {
		return destinationPath;
	}
	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
	}
	public boolean isZippedContent() {
		return isZippedContent;
	}
	public void setZippedContent(boolean isZippedContent) {
		this.isZippedContent = isZippedContent;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}

}
