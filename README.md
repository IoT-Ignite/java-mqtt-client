# IoT-Ignite MQTT Client Example

IoT-Ignite MQTT Client can be used for connecting gateways to IoT-Ignite cloud with MQTT Protocol. 

This project includes 4 main components:

- MQTT Client Wrapper powered by Eclipse Paho project.
- Content-Agent for downloading published files from IoT-Ignite Platform
- Sensor-Agent for collecting and sending sensor data to IoT-Ignite Platform
- OS-Profile-Agent for sending client info to IoT-Ignite Platform

Linux-Wrapper is runnable project for these 4 components, compilition can be done with Maven 3.3.+ and JDK 8.

MQTT credentials must be provided from configuration file. Sample configuration file format given below:

```
MQTT_USERNAME = {mqtt-username}
MQTT_PASSWORD = {mqtt-password}
MQTT_CLIENT_ID = {mqtt-client-id}
```

These credentials can be generated from [Devzone Console](https://devzone.iot-ignite.com/dpanel). 
Configuration file should be given as parameter when starting MQTT-Client.

```bash
$ java -Dmqtt.properties="./mqtt.configurations" -jar linux-wrapper-1.0.0-jar-with-dependencies.jar
```

For more information about MQTT Client please visit [Devzone documentation](https://devzone.iot-ignite.com/knowledge-base/using-iot-ignite-mqtt-client/)
