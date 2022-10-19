package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NopTelecommandPayload() implements ExampleAppTelecommandPayload {

	public static final String TELECOMMAND_PAYLOAD_NAME = "Nop";
	public static final short TELECOMMAND_PAYLOAD_ID = 1;
	public static final short TELECOMMAND_PAYLOAD_NODE_ID = ANY_NODE_ID;

	@Override
	public short id() {
		return TELECOMMAND_PAYLOAD_ID;
	}

	@Override
	public String name() {
		return TELECOMMAND_PAYLOAD_NAME;
	}

	@Override
	public short nodeId() {
		return TELECOMMAND_PAYLOAD_NODE_ID;
	}
}
