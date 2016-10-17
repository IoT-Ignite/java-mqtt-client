package com.ardic.mqtt.agent.sensoragent.model;

public class Sensor {

	private String id;
	private String dataType;
	private String vendor;
	private boolean actuator;
	private String type;
	
	public Sensor(String id, String dataType, String vendor, boolean actuator, String type) {
		this.id = id;
		this.dataType = dataType;
		this.vendor = vendor;
		this.actuator = actuator;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public String getDataType() {
		return dataType;
	}

	public String getVendor() {
		return vendor;
	}

	public boolean isActuator() {
		return actuator;
	}

	public String getType() {
		return type;
	}

}
