package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

public interface CorfuAppTelemetry extends CorfuApp {
	String IDENTIFIER_PROPERTY = "type";

	String PAYLOAD_PROPERTY = "payload";

	AppTelemetryPayload payload();

	short id();
}
