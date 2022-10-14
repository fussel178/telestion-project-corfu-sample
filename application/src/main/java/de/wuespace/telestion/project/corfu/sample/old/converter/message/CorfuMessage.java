package de.wuespace.telestion.project.corfu.sample.old.converter.message;

import de.wuespace.telestion.api.message.JsonMessage;

public interface CorfuMessage extends JsonMessage {
	static CorfuMessage cast(Object instance) throws CorfuSerializationException {
		try {
			return (CorfuMessage) instance;
		} catch (ClassCastException e) {
			throw new CorfuSerializationException(("Cannot cast instance %s to CorfuMessage. " +
					"Please provide a valid instance whose type is a subclass of CorfuMessage " +
					"and try again.").formatted(instance));
		}
	}
}
