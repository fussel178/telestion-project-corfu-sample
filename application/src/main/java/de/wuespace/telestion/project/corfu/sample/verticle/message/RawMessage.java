package de.wuespace.telestion.project.corfu.sample.verticle.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.message.JsonMessage;
import io.vertx.core.buffer.Buffer;

public record RawMessage(@JsonProperty byte[] data) implements JsonMessage {
	public Buffer asBuffer() {
		return Buffer.buffer(data);
	}
}
