package de.wuespace.telestion.project.corfu.sample.converter.message;

import de.wuespace.telestion.api.message.JsonMessage;

public interface Payload extends JsonMessage {
	static void assertIsPayload(Class<?> type) {
		if (!Payload.class.isAssignableFrom(type)) {
			throw new IllegalArgumentException(("The type %s does not implement interface Payload but is annotated " +
					"as corfu type. Please implement the interface Payload and try again").formatted(type.getName()));
		}
	}

	short id();

	String name();
}
