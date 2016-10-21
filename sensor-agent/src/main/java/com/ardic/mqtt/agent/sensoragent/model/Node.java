package com.ardic.mqtt.agent.sensoragent.model;

import java.util.List;

public class Node {

	private String nodeId;
	private List<Sensor> things;

	public Node(String nodeId, List<Sensor> things){
		this.nodeId = nodeId;
		this.things = things;
	}
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public List<Sensor> getThings() {
		return things;
	}

	public void setThings(List<Sensor> things) {
		this.things = things;
	}

}
