package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MyTelemetryTelemetryPayload(
		@JsonProperty @CorfuProperty(CorfuProperty.Type.UINT8) short myIntField,
		@JsonProperty @CorfuProperty(CorfuProperty.Type.FLOAT) float myFloatField
) implements ExampleAppTelemetryPayload {

	public static final String TELEMETRY_PAYLOAD_NAME = "MyTelemetry";
	public static final short TELEMETRY_PAYLOAD_ID = 1;

	@Override
	public short id() {
		return TELEMETRY_PAYLOAD_ID;
	}

	@Override
	public String name() {
		return TELEMETRY_PAYLOAD_NAME;
	}
}
