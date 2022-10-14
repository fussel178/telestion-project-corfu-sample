package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

import de.wuespace.telestion.api.message.JsonMessage;

public interface CorfuPayload extends JsonMessage {
	static void assertIsPayload(Class<?> type) {
		if (!CorfuPayload.class.isAssignableFrom(type)) {
			throw new IllegalArgumentException(("The type %s does not implement interface Payload but is annotated " +
					"as corfu type. Please implement the interface Payload and try again").formatted(type.getName()));
		}
	}

	short id();

	String name();
}
