package de.wuespace.telestion.project.corfu.sample.generated.osbw.node.obc;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp.ExampleAppStandardTelemetry;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.app.housekeeper.HousekeeperAppTelemetryPayload;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.app.timemanager.TimeManagerStandardTelemetry;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuProperty;

public record ObcStandardTelemetryPayload(
		@JsonProperty @CorfuProperty ExampleAppStandardTelemetry exampleApp,
		@JsonProperty @CorfuProperty TimeManagerStandardTelemetry timeManager
) implements HousekeeperAppTelemetryPayload {

	public static final String TELEMETRY_PAYLOAD_NAME = "ObcStandardTelemetry";

	public static final short TELEMETRY_PAYLOAD_ID = 1;

	public static final short TELEMETRY_PAYLOAD_NODE_ID = ObcNode.NODE_ID;

	@Override
	public short id() {
		return TELEMETRY_PAYLOAD_ID;
	}

	@Override
	public String name() {
		return TELEMETRY_PAYLOAD_NAME;
	}

	@Override
	public short nodeId() {
		return TELEMETRY_PAYLOAD_NODE_ID;
	}
}
