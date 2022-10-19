package de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.verticle.trait.WithEventBus;
import de.wuespace.telestion.api.verticle.trait.WithSharedData;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuSerializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuTelecommand;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle.message.SerializerCommand;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle.message.SerializerResetCommand;
import de.wuespace.telestion.services.connection.rework.RawMessage;
import io.vertx.core.json.JsonObject;

import java.util.Map;

@SuppressWarnings("unused")
public class CorfuTelecommandSerializer extends CorfuMapperVerticle<CorfuTelecommandSerializer.Configuration>
		implements WithEventBus, WithSharedData {

	public static final String LOCAL_STATUS_MAP = "status";

	public record Configuration(
			@JsonProperty String inAddress,
			@JsonProperty String outAddress,
			@JsonProperty String stsAddress,
			@JsonProperty String cmdAddress,
			@JsonProperty String endianness
	) implements CorfuMapperConfiguration {
		public Configuration() {
			this(null,
					null,
					"corfu/serializer/status",
					"corfu/serializer/command",
					"little"
			);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		register(getConfig().inAddress, this::handle, CorfuTelecommand.class);
		register(getConfig().cmdAddress, this::handleCommand, SerializerCommand.class);
		logger.info("Ready for telecommands");
	}

	private void handle(CorfuTelecommand telecommand) {
		logger.debug("Serialize new telecommand: {}", telecommand);

		try {
			byte[] serialized = mapper.serialize(telecommand);
			publish(getConfig().outAddress, new RawMessage(serialized));
			incrementCounter(Counter.GOOD);
		} catch (CorfuSerializationException e) {
			logger.warn(e.getMessage(), e);
			incrementCounter(Counter.BAD_SERIALIZATION);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			incrementCounter(Counter.BAD_UNKNOWN);
		}
	}

	private void handleCommand(SerializerCommand command) {
		if (command instanceof SerializerResetCommand) {
			resetCounters();
		} else {
			logger.warn("Unknown serializer command: {}", command.getClass().getName());
		}
	}

	private void resetCounters() {
		Map<String, Integer> map = localMap(LOCAL_STATUS_MAP);
		map.replaceAll((key, value) -> 0);
		publishStatusUpdate();
	}

	private void incrementCounter(Counter counter) {
		Map<String, Integer> map = localMap(LOCAL_STATUS_MAP);
		var newCount = map.getOrDefault(counter.name(), 0) + 1;
		map.put(counter.name(), newCount);
		publishStatusUpdate();
	}

	private void publishStatusUpdate() {
		var message = new JsonObject()
				.put("good", getCounter(Counter.GOOD))
				.put("bad-serialization", getCounter(Counter.BAD_SERIALIZATION))
				.put("bad-unknown", getCounter(Counter.BAD_UNKNOWN));
		publish(getConfig().stsAddress, message);
	}

	private int getCounter(Counter counter) {
		Map<String, Integer> map = localMap(LOCAL_STATUS_MAP);
		return map.getOrDefault(counter.name(), 0);
	}

	private enum Counter {
		GOOD, BAD_SERIALIZATION, BAD_UNKNOWN
	}
}
