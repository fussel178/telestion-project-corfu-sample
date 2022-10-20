package de.wuespace.telestion.project.corfu.sample.verticle.redis.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.verticle.TelestionConfiguration;

public interface RedisBaseConfiguration extends TelestionConfiguration {
	@JsonProperty String connectionString();
	@JsonProperty int reconnectAttempts();
}
