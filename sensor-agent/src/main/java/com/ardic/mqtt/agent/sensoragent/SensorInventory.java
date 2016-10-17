package com.ardic.mqtt.agent.sensoragent;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ardic.mqtt.agent.sensoragent.model.Sensor;

public class SensorInventory {

	static SensorInventory service = null;
	private List<Sensor> sensorList = new ArrayList<Sensor>();

	public static SensorInventory getInstance(){
		if(service==null){
			service = new SensorInventory();
		}
		return service;
	}

	private SensorInventory() {
		
	}

	public void addSensor(Sensor item){
		sensorList.add(item);
	}

	public JSONObject generateDeviceNodeInventory() {
		JSONObject json = new JSONObject();
		JSONArray data = new JSONArray();
		JSONObject builtInNode = new JSONObject();
		builtInNode.put("nodeId", "Built-in Sensors");
		builtInNode.put("things", new JSONArray(sensorList));
		data.put(builtInNode);
		json.put("data", data);

		return json;
	}
}
