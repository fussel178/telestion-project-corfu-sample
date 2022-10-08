package de.wuespace.telestion.project.corfu.sample.converter.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.message.JsonMessage;

public record Telemetry(
		@JsonProperty int packetNumber,
		@JsonProperty long timeUTC,
		@JsonProperty long uptime,
		@JsonProperty boolean isHistoric,
		@JsonProperty Node node,
		@JsonProperty AppTelemetry app
) implements JsonMessage {
}
