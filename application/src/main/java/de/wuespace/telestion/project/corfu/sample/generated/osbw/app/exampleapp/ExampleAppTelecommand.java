package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuAppTelecommand;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExampleAppTelecommand(ExampleAppTelecommandPayload payload) implements CorfuAppTelecommand {

	public static final String APP_TELECOMMAND_NAME = "example-app";
	public static final short APP_TELECOMMAND_ID = 137;

	@Override
	public short id() {
		return APP_TELECOMMAND_ID;
	}

	@Override
	public String name() {
		return APP_TELECOMMAND_NAME;
	}
}
