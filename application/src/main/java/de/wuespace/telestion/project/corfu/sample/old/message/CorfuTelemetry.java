package de.wuespace.telestion.project.corfu.sample.old.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.message.JsonMessage;

public record CorfuTelemetry(
		@JsonProperty long timeUTC,
		@JsonProperty long uptime,
		@JsonProperty CorfuNodeId nodeId,
		@JsonProperty boolean isHistoric,
		@JsonProperty CorfuAppTelemetry payload
) implements JsonMessage {
}
