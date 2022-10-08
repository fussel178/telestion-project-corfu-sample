package de.wuespace.telestion.project.corfu.sample.converter.message;

public interface AppTelemetry extends App {
	String IDENTIFIER_PROPERTY = "type";

	String PAYLOAD_PROPERTY = "payload";

	AppTelemetryPayload payload();

	short id();
}
