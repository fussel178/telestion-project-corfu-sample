package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.timemanager;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.StandardTelemetry;
import de.wuespace.telestion.project.corfu.sample.converter.message.CorfuProperty;

public record TimeManagerStandardTelemetry(
		@JsonProperty @CorfuProperty(CorfuProperty.Type.INT64) long utc,
		@JsonProperty @CorfuProperty(CorfuProperty.Type.INT64) long uptime
) implements StandardTelemetry {
}
