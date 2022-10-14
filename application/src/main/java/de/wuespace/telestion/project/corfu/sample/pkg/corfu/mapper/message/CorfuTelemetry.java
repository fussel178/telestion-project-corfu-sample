package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CorfuTelemetry(
		@JsonProperty int packetNumber,
		@JsonProperty long timeUTC,
		@JsonProperty long uptime,
		@JsonProperty boolean isHistoric,
		@JsonProperty CorfuNode node,
		@JsonProperty CorfuAppTelemetry app
) implements CorfuMessage {
}
