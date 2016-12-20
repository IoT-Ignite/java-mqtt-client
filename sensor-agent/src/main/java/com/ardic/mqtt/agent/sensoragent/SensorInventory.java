package com.ardic.mqtt.agent.sensoragent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ardic.mqtt.agent.sensoragent.model.ListSensorData;
import com.ardic.mqtt.agent.sensoragent.model.Node;
import com.ardic.mqtt.agent.sensoragent.model.Sensor;
import com.ardic.mqtt.agent.sensoragent.model.Thing;

public class SensorInventory {

	static SensorInventory service = new SensorInventory();
	private Map<String, List<Sensor>> inventory = new HashMap<String, List<Sensor>>();
	private ListSensorData<Thing> thingList = new ListSensorData<Thing>();

	private SensorInventory() {
		thingList.setData(new ArrayList<Thing>());
	}

	public static SensorInventory getInstance() {
		return service;
	}

	public void addSensor(String nodeId, Sensor item) {
		inventory.get(nodeId).add(item);
		thingList.getData().add(new Thing(nodeId, item.getId(), item.getType(), 0));
	}

	public void sensorConnectionStatus(String sensorId, int connected) {
		for (Thing thing : thingList.getData()) {
			if (thing.getThingId() != null) {
				if (thing.getThingId().equals(sensorId)) {
					thing.setConnected(connected);
					break;
				}
			}
		}
	}

	public void addNode(String nodeId, String description) {
		inventory.put(nodeId, new ArrayList<Sensor>());
		thingList.getData().add(new Thing(nodeId, null, description, 1));
	}

	public ListSensorData<Node> generateDeviceNodeInventory() {
		ListSensorData<Node> inventoryList = new ListSensorData<Node>();
		List<Node> nodeList = new ArrayList<Node>();

		for (Map.Entry<String, List<Sensor>> value : inventory.entrySet()) {
			nodeList.add(new Node(value.getKey(), value.getValue()));
		}

		inventoryList.setData(nodeList);
		return inventoryList;
	}

	public ListSensorData<Thing> generateDeviceNodePresence() {
		return thingList;
	}
}
