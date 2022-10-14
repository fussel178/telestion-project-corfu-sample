package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.timemanager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuAppTelecommand;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TimeManagerTelecommand(TimeManagerTelecommandPayload payload) implements CorfuAppTelecommand {

	public static final String APP_TELECOMMAND_NAME = "time-manager";
	public static final short APP_TELECOMMAND_ID = 5;

	@Override
	public short id() {
		return APP_TELECOMMAND_ID;
	}

	@Override
	public String name() {
		return APP_TELECOMMAND_NAME;
	}
}
