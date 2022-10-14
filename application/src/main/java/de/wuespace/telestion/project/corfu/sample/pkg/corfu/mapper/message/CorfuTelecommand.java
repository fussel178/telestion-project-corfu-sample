package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CorfuTelecommand(
		@JsonProperty short commandIndex,
		@JsonProperty short sequenceCounter,
		@JsonProperty long timeToExecute,
		@JsonProperty CorfuNode node,
		@JsonProperty CorfuAppTelecommand app
) implements CorfuMessage {
}
