package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuProperty;

import java.util.BitSet;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MyBitarrayTelemetryTelemetryPayload(
		@JsonProperty @CorfuProperty(value = CorfuProperty.Type.BITARRAY, count = 12) BitSet bits
) implements ExampleAppTelemetryPayload {

	public static final String TELEMETRY_PAYLOAD_NAME = "MyBitarrayTelemetry";
	public static final short TELEMETRY_PAYLOAD_ID = 2;

	@Override
	public short id() {
		return TELEMETRY_PAYLOAD_ID;
	}

	@Override
	public String name() {
		return TELEMETRY_PAYLOAD_NAME;
	}
}
