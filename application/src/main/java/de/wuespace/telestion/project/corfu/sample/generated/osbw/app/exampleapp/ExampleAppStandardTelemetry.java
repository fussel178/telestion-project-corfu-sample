package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.StandardTelemetry;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuProperty;

public record ExampleAppStandardTelemetry(
		@JsonProperty @CorfuProperty(CorfuProperty.Type.UINT8) short counter
) implements StandardTelemetry {
}
