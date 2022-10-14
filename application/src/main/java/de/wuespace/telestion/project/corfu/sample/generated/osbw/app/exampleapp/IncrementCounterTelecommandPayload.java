package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IncrementCounterTelecommandPayload(
		@JsonProperty @CorfuProperty(CorfuProperty.Type.UINT8) short incrementStep
) implements ExampleAppTelecommandPayload {

	public static final String TELECOMMAND_PAYLOAD_NAME = "IncrementCounter";
	public static final short TELECOMMAND_PAYLOAD_ID = 3;

	@Override
	public short id() {
		return TELECOMMAND_PAYLOAD_ID;
	}

	@Override
	public String name() {
		return TELECOMMAND_PAYLOAD_NAME;
	}
}
