package com.ardic.mqtt.agent.sensoragent.model;

import java.util.List;

public class SensorInventoryModel {

	private List<Node> data;
	public List<Node> getData() {
		return data;
	}
	public void setData(List<Node> data) {
		this.data = data;
	}
}
