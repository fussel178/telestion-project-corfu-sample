package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelemetry;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExampleAppTelemetry(ExampleAppTelemetryPayload payload) implements AppTelemetry {

	public static final String APP_TELEMETRY_NAME = "example-app";
	public static final short APP_TELEMETRY_ID = 137;

	@Override
	public short id() {
		return APP_TELEMETRY_ID;
	}

	@Override
	public String name() {
		return APP_TELEMETRY_NAME;
	}
}