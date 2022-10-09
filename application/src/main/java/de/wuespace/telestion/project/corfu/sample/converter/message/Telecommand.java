package de.wuespace.telestion.project.corfu.sample.converter.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.message.JsonMessage;

public record Telecommand(
		@JsonProperty int commandIndex,
		@JsonProperty short sequenceCounter,
		@JsonProperty long timeToExecute,
		@JsonProperty Node node,
		@JsonProperty AppTelecommand app
) implements JsonMessage {
}
