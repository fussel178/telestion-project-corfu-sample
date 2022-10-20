package de.wuespace.telestion.project.corfu.sample.verticle.redis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.message.JsonMessage;

import java.util.List;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public record RedisLatestRequest(
		@JsonProperty List<String> fields
) implements JsonMessage {
	public RedisLatestRequest() {
		this(List.of());
	}
}
