package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelemetryPayload;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = MyTelemetryTelemetryPayload.class, name = MyTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_NAME),
		@JsonSubTypes.Type(value = MyBitarrayTelemetryTelemetryPayload.class, name = MyBitarrayTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_NAME)
})
public interface ExampleAppTelemetryPayload extends AppTelemetryPayload {
}
