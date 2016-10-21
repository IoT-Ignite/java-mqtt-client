package com.ardic.mqtt.agent.sensoragent.model;

public class SensorData<T> {

	private SensorDataPackage<T> data;

	public SensorDataPackage<T> getData() {
		return data;
	}

	public void setData(SensorDataPackage<T> data) {
		this.data = data;
	}

}
