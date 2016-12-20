package com.ardic.mqtt.agent.sensoragent.model;

public class SensorDataValue<T> {

	private long date;
	private T[] values;

	public SensorDataValue(long time, T[] values) {
		this.date = time;
		this.values = values;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public T[] getValue() {
		return values;
	}

	public void setValue(T[] values) {
		this.values = values;
	}
}
