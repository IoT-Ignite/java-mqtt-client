package com.ardic.mqtt.client.exception;

public class ServiceNotAvailableException extends Exception {

	private static final long serialVersionUID = -6015541832199124798L;

	public ServiceNotAvailableException() {
		super("Service not available");
	}

}
