package com.ardic.mqtt.agent.sensoragent.model;

import java.util.List;

public class ListSensorData<T> {

	private List<T> data;
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
}
