package de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.verticle.trait.WithEventBus;
import de.wuespace.telestion.api.verticle.trait.WithSharedData;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.*;
import de.wuespace.telestion.project.corfu.sample.pkg.util.ByteArrayUtils;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle.message.DeserializerCommand;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle.message.DeserializerResetCommand;
import de.wuespace.telestion.services.connection.rework.RawMessage;
import io.vertx.core.json.JsonObject;

import java.util.Map;

@SuppressWarnings("unused")
public class CorfuTelemetryDeserializer extends CorfuMapperVerticle<CorfuTelemetryDeserializer.Configuration>
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
					"corfu/deserializer/status",
					"corfu/deserializer/command",
					"little"
			);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		register(getConfig().inAddress, this::handle, RawMessage.class);
		register(getConfig().cmdAddress, this::handleCommand, DeserializerCommand.class);
		logger.info("Ready for telemetry");
	}

	public void handle(RawMessage message) {
		logger.debug("Deserialize new telemetry: {}", ByteArrayUtils.toString(message.data()));

		try {
			var telemetry = mapper.deserialize(message.data());
			logger.debug("Deserialized telemetry: {}", telemetry);
			publish(getConfig().outAddress, telemetry);
			incrementCounter(Counter.GOOD);
		} catch (PacketTooSmallException e) {
			logger.warn(e.getMessage());
			incrementCounter(Counter.BAD_PACKET_TO_SMALL);
		} catch (PacketTooLargeException e) {
			logger.warn(e.getMessage());
			incrementCounter(Counter.BAD_PACKET_TO_LARGE);
		} catch (InvalidCheckSumException e) {
			logger.warn(e.getMessage());
			incrementCounter(Counter.BAD_INVALID_CHECKSUM);
		} catch (InvalidPayloadLengthException e) {
			logger.warn(e.getMessage());
			incrementCounter(Counter.BAD_INVALID_PAYLOAD_LENGTH);
		} catch (CorfuDeserializationException e) {
			logger.warn(e.getMessage());
			incrementCounter(Counter.BAD_DESERIALIZATION);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			incrementCounter(Counter.BAD_UNKNOWN);
		}
	}

	public void handleCommand(DeserializerCommand command) {
		if (command instanceof DeserializerResetCommand) {
			resetCounters();
		} else {
			logger.warn("Unknown deserializer command: {}", command.getClass().getName());
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
				.put("bad-packetToSmall", getCounter(Counter.BAD_PACKET_TO_SMALL))
				.put("bad-packetToLarge", getCounter(Counter.BAD_PACKET_TO_LARGE))
				.put("bad-invalidCheckSum", getCounter(Counter.BAD_INVALID_CHECKSUM))
				.put("bad-invalidPayloadLength", getCounter(Counter.BAD_INVALID_PAYLOAD_LENGTH))
				.put("bad-deserialization", getCounter(Counter.BAD_DESERIALIZATION))
				.put("bad-unknown", getCounter(Counter.BAD_UNKNOWN));
		publish(getConfig().stsAddress, message);
	}

	private int getCounter(Counter counter) {
		Map<String, Integer> map = localMap(LOCAL_STATUS_MAP);
		return map.getOrDefault(counter.name(), 0);
	}

	private static final String goodCounterKey = "good";
	private static final String packetToSmallCounterKey = "bad-packetToSmall";
	private static final String packetToLargeCounterKey = "bad-packetToLarge";
	private static final String invalidCheckSumCounterKey = "bad-invalidCheckSum";
	private static final String invalidPayloadLengthCounterKey = "bad-invalidPayloadLength";
	private static final String badDeserializationCounterKey = "bad-deserialization";
	private static final String badUnknownCounterKey = "bad-unknown";

	private enum Counter {
		GOOD, BAD_PACKET_TO_SMALL, BAD_PACKET_TO_LARGE, BAD_INVALID_CHECKSUM, BAD_INVALID_PAYLOAD_LENGTH,
		BAD_DESERIALIZATION, BAD_UNKNOWN
	}
}
