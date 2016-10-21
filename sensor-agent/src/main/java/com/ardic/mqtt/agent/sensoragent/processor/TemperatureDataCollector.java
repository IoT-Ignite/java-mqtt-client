package com.ardic.mqtt.agent.sensoragent.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ardic.mqtt.agent.sensoragent.SensorInventory;
import com.ardic.mqtt.agent.sensoragent.model.Sensor;
import com.ardic.mqtt.agent.sensoragent.model.SensorData;
import com.ardic.mqtt.agent.sensoragent.model.SensorDataPackage;
import com.ardic.mqtt.agent.sensoragent.model.SensorDataValue;
import com.ardic.mqtt.client.service.MessagePublisherService;
import com.google.gson.Gson;

public class TemperatureDataCollector implements Runnable {

	FileReader fr = null;
	private MessagePublisherService publisher;
	private Logger logger = LoggerFactory.getLogger(TemperatureDataCollector.class);
	private static final Sensor TEMPERATURE_SENSOR = new Sensor("CpuTemp", "INTEGER", "ARDIC", false, "Temperature");
	private boolean readData = true;

	public TemperatureDataCollector() {
		publisher = MessagePublisherService.getInstance();
		SensorInventory.getInstance().addSensor(TEMPERATURE_SENSOR);
	}

	public void run() {
		while (readData) {
			try (BufferedReader br = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone2/temp"));) {
				String[] value = new String[1];
				value[0] = br.readLine();
				SensorDataValue<String>[] sensorValue = new SensorDataValue[1];
				sensorValue[0] = new SensorDataValue<String>(new Date().getTime(), value);

				SensorDataPackage<String> dataPackage = new SensorDataPackage<String>();
				dataPackage.setSensorData(sensorValue);
				SensorData<String> data = new SensorData<String>();
				data.setData(dataPackage);
				publisher.publishMessage("DeviceProfile/Built-in Sensors/" + TEMPERATURE_SENSOR.getId(), new Gson().toJson(data));
				br.close();
			} catch (IOException e) {
				logger.error("IOException", e);
				readData = false;
			}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				logger.error("Thread sleep interruptException:", e);
				Thread.currentThread().interrupt();
			}
		}

	}

	/**
	 * Stop reading temperature data.
	 */
	public void stopReading() {
		readData = false;
	}
}
