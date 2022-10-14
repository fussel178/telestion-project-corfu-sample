package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.timemanager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SetUtcTelecommandPayload(
		@JsonProperty @CorfuProperty(CorfuProperty.Type.INT64) long nanoSeconds
) implements TimeManagerTelecommandPayload {

	public static final String TELECOMMAND_PAYLOAD_NAME = "SetUtc";
	public static final short TELECOMMAND_PAYLOAD_ID = 2;

	@Override
	public short id() {
		return TELECOMMAND_PAYLOAD_ID;
	}

	@Override
	public String name() {
		return TELECOMMAND_PAYLOAD_NAME;
	}
}
