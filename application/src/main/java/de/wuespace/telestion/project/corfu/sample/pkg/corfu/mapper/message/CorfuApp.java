package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

import de.wuespace.telestion.api.message.JsonMessage;

public interface CorfuApp extends JsonMessage {
	short id();

	String name();

	CorfuPayload payload();
}
