package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.housekeeper;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.node.obc.ObcStandardTelemetryPayload;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.AppTelemetryPayload;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = ObcStandardTelemetryPayload.class, name = ObcStandardTelemetryPayload.TELEMETRY_PAYLOAD_NAME)
})
public interface HousekeeperAppTelemetryPayload extends AppTelemetryPayload {
}
