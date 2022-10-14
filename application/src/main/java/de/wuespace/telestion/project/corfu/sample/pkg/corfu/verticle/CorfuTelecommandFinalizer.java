package de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.verticle.TelestionConfiguration;
import de.wuespace.telestion.api.verticle.TelestionVerticle;
import de.wuespace.telestion.api.verticle.trait.WithEventBus;
import de.wuespace.telestion.api.verticle.trait.WithSharedData;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuTelecommand;

import java.util.Map;

@SuppressWarnings("unused")
public class CorfuTelecommandFinalizer extends TelestionVerticle<CorfuTelecommandSerializer.Configuration>
		implements WithEventBus, WithSharedData {

	public static final String LOCAL_MAP_KEY = "counters";

	public record Configuration(
			@JsonProperty String inAddress,
			@JsonProperty String outAddress
	) implements TelestionConfiguration {
	}

	@Override
	public void onStart() throws Exception {
		register(getConfig().inAddress(), this::handle, CorfuTelecommand.class);
		logger.info("Ready to finalize telecommands");
	}

	private void handle(CorfuTelecommand telecommand) {
		logger.debug("New telecommand to finalize");
		var newTelecommand = new CorfuTelecommand(telecommand.commandIndex(),
				getAndIncrement(),
				telecommand.timeToExecute(),
				telecommand.node(),
				telecommand.app()
		);
		publish(getConfig().outAddress(), newTelecommand);
		logger.debug("Sent telecommand with sequence counter: {}", newTelecommand.sequenceCounter());
	}

	private short getAndIncrement() {
		Map<String, Short> map = localMap(LOCAL_MAP_KEY);

		short value = map.getOrDefault(SEQUENCE_COUNTER_KEY, (short) 0);
		short newValue = value >= 255 ? (short) 0 : (short) (value + 1);
		map.put("sequenceCounter", newValue);
		// TODO: Add counters and status address
		return value;
	}

	private static final String SEQUENCE_COUNTER_KEY = "sequenceCounter";
}
