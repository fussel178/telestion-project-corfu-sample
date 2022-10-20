package de.wuespace.telestion.project.corfu.sample.verticle.redis;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.message.JsonMessage;

import java.util.List;

@SuppressWarnings("unused")
public record RedisTimeSeriesRequest(
		@JsonProperty List<RedisTimeSeriesSpecification> fields
) implements JsonMessage {
	public RedisTimeSeriesRequest() {
		this(List.of());
	}
}
