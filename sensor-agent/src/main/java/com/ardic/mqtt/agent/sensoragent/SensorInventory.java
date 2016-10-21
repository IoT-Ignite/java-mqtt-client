package com.ardic.mqtt.agent.sensoragent;

import java.util.ArrayList;
import java.util.List;

import com.ardic.mqtt.agent.sensoragent.model.Node;
import com.ardic.mqtt.agent.sensoragent.model.Sensor;
import com.ardic.mqtt.agent.sensoragent.model.SensorInventoryModel;

public class SensorInventory {

	static SensorInventory service = new SensorInventory();
	private List<Sensor> sensorList = new ArrayList<Sensor>();

	private SensorInventory() {

	}

	public static SensorInventory getInstance() {
		return service;
	}

	public void addSensor(Sensor item) {
		sensorList.add(item);
	}

	public SensorInventoryModel generateDeviceNodeInventory() {
		List<Node> nodeList = new ArrayList<Node>();
		nodeList.add(new Node("Built-in Sensors", sensorList));

		SensorInventoryModel inventory = new SensorInventoryModel();
		inventory.setData(nodeList);
		return inventory;
	}
}
