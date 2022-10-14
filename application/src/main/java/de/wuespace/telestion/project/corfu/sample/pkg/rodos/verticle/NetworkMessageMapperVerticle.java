package de.wuespace.telestion.project.corfu.sample.pkg.rodos.verticle;

import de.wuespace.telestion.api.verticle.TelestionConfiguration;
import de.wuespace.telestion.api.verticle.TelestionVerticle;
import de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.NetworkMessageMapper;

import java.nio.ByteOrder;

public abstract class NetworkMessageMapperVerticle<T extends NetworkMessageMapperVerticle.NetworkMessageMapperConfiguration>
		extends TelestionVerticle<T> {
	public interface NetworkMessageMapperConfiguration extends TelestionConfiguration {
		String endianness();
	}

	protected NetworkMessageMapper mapper;

	@Override
	public void onStart() {
		if (!getConfig().endianness().equals("big") && !getConfig().endianness().equals("little")) {
			throw new IllegalArgumentException(("The endianness configuration parameter needs to be one of \"big\" " +
					"or \"little\". Got: %s").formatted(getConfig().endianness()));
		}

		ByteOrder order = getConfig().endianness().equals("big") ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
		logger.debug("User selected byte order: {}", order);
		mapper = new NetworkMessageMapper(order);
	}
}
