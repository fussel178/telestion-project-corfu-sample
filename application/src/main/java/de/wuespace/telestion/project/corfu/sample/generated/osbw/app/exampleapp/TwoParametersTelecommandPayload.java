package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.project.corfu.sample.converter.message.CorfuProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TwoParametersTelecommandPayload(
		@JsonProperty @CorfuProperty(CorfuProperty.Type.UINT8) short firstParameter,
		@JsonProperty @CorfuProperty(CorfuProperty.Type.FLOAT) float secondParameter
) implements ExampleAppTelecommandPayload {

	public static final String TELECOMMAND_PAYLOAD_NAME = "TwoParameters";
	public static final short TELECOMMAND_PAYLOAD_ID = 4;

	@Override
	public short id() {
		return TELECOMMAND_PAYLOAD_ID;
	}

	@Override
	public String name() {
		return TELECOMMAND_PAYLOAD_NAME;
	}
}
