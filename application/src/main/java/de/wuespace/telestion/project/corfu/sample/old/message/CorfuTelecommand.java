package de.wuespace.telestion.project.corfu.sample.old.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.message.JsonMessage;

public record CorfuTelecommand(
		@JsonProperty int commandIndex,
		@JsonProperty short sequenceCounter,
		@JsonProperty CorfuNodeId nodeId,
		@JsonProperty long timeToExecute,
		@JsonProperty CorfuAppTelecommand payload
) implements JsonMessage {
}
