package com.ardic.mqtt.agent.sensoragent.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.agent.sensoragent.SensorInventory;
import com.ardic.mqtt.agent.sensoragent.model.Sensor;
import com.ardic.mqtt.client.exception.ServiceNotAvailableException;
import com.ardic.mqtt.client.service.MessagePublisherService;

public class TemperatureDataCollector implements Runnable {

	BufferedReader br = null;
	FileReader fr = null;
	private MessagePublisherService publisher;
	private Logger logger = LoggerFactory.getLogger(TemperatureDataCollector.class);
	private static final Sensor TEMPERATURE_SENSOR = new Sensor("CpuTemp", "INTEGER", "ARDIC", false, "Temperature");

	public TemperatureDataCollector() {
		try {
			publisher = MessagePublisherService.getInstance();
			SensorInventory.getInstance().addSensor(TEMPERATURE_SENSOR);
		} catch (ServiceNotAvailableException e) {
			logger.error("MessagePublishService is not initialized.", e);
		}
	}

	public void run() {
		while (true) {
			try {
				fr = new FileReader("/sys/class/thermal/thermal_zone2/temp");
				br = new BufferedReader(fr);
				String data = "{\"data\":{\"sensorData\": [{\"date\":" + new Date().getTime() + ",\"values\":[\"" + br.readLine() + "\"]}]}}";
				publisher.publishMessage("DeviceProfile/Built-in Sensors/" + TEMPERATURE_SENSOR.getId(), data);
				br.close();
			} catch (IOException e) {
				logger.error("IOException", e);
			}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				logger.error("Thread sleep interruptException:", e);
			}
		}

	}
}
