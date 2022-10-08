package de.wuespace.telestion.project.corfu.sample.converter.message;

import de.wuespace.telestion.api.message.JsonMessage;

public interface App extends JsonMessage {
	short id();

	String name();

	Payload payload();
}
