package de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle;

import de.wuespace.telestion.api.verticle.TelestionConfiguration;
import de.wuespace.telestion.api.verticle.TelestionVerticle;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.CorfuMessageMapper;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle.global.GlobalStore;

import java.nio.ByteOrder;

public abstract class CorfuMapperVerticle<T extends CorfuMapperVerticle.CorfuMapperConfiguration> extends TelestionVerticle<T> {
	public interface CorfuMapperConfiguration extends TelestionConfiguration {
		String endianness();
	}

	protected CorfuMessageMapper mapper;

	@Override
	public void onStart() {
		if (!getConfig().endianness().equals("big") && !getConfig().endianness().equals("little")) {
			throw new IllegalArgumentException(("The endianness configuration parameter needs to be one of \"big\" " +
					"or \"little\". Got: %s").formatted(getConfig().endianness()));
		}

		ByteOrder order = getConfig().endianness().equals("big") ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
		logger.debug("User selected byte order: {}", order);
		mapper = new CorfuMessageMapper(GlobalStore.store(), order);
	}
}
