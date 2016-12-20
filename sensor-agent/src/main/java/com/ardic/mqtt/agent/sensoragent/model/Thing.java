package com.ardic.mqtt.agent.sensoragent.model;

public class Thing {

	private String nodeId;
	private String thingId;
	private String description;
	private int connected;

	public Thing(String nodeId, String thingId, String description, int connected) {
		this.nodeId = nodeId;
		this.thingId = thingId;
		this.description = description;
		this.connected = connected;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getThingId() {
		return thingId;
	}

	public void setThingId(String thingId) {
		this.thingId = thingId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getConnected() {
		return connected;
	}

	public void setConnected(int connected) {
		this.connected = connected;
	}

}
