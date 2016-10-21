package com.ardic.mqtt.agent.sensoragent.model;

public class SensorDataPackage <T> {

	private SensorDataValue<T>[] sensorData;

	public SensorDataValue<T>[] getSensorData() {
		return sensorData;
	}

	public void setSensorData(SensorDataValue<T>[] sensorData) {
		this.sensorData = sensorData;
	}
}
