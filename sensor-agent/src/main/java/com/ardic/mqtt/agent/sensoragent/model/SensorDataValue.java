package com.ardic.mqtt.agent.sensoragent.model;

public class SensorDataValue<T> {

	private long date;
	private T[] value;

	public SensorDataValue(long time, T[] value) {
		this.date = time;
		this.value = value;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public T[] getValue() {
		return value;
	}

	public void setValue(T[] value) {
		this.value = value;
	}
}
