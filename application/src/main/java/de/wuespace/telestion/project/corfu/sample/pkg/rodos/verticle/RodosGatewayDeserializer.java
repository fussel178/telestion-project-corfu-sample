package de.wuespace.telestion.project.corfu.sample.pkg.rodos.verticle;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.verticle.trait.WithEventBus;
import de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.exception.*;
import de.wuespace.telestion.services.connection.rework.RawMessage;

@SuppressWarnings("unused")
public class RodosGatewayDeserializer extends NetworkMessageMapperVerticle<RodosGatewayDeserializer.Configuration>
		implements WithEventBus {

	public record Configuration(
			@JsonProperty String inAddress,
			@JsonProperty String outAddress,
			@JsonProperty String endianness
	) implements NetworkMessageMapperConfiguration {
		public Configuration() {
			this(null, null, "big");
		}
	}

	@Override
	public void onStart() throws IllegalArgumentException {
		super.onStart();
		register(getConfig().inAddress, this::handle, RawMessage.class);
		logger.info("Ready for deserialization");
	}

	public void handle(RawMessage message) {
		logger.debug("Deserialize new network message");
		try {
			var networkMessage = mapper.deserialize(message.data());
			publish(getConfig().outAddress, networkMessage);
		} catch (RodosException e) {
			// TODO: Add counters and status address
			logger.error(e.getMessage());
		}
	}
}
