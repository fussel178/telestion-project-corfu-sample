package de.wuespace.telestion.project.corfu.sample.verticle.converter;

import de.wuespace.telestion.project.corfu.sample.converter.jackson.CorfuModule;
import io.vertx.core.json.jackson.DatabindCodec;

public class GlobalCorfuModule {
	private static final CorfuModule module = new CorfuModule(GlobalStore.store());
	private static boolean isRegistered = false;

	public static CorfuModule module() {
		return module;
	}

	public synchronized static void register() {
		if (isRegistered) return;

		DatabindCodec.mapper().registerModule(module());
		isRegistered = true;
	}

	private GlobalCorfuModule() {
	}
}
