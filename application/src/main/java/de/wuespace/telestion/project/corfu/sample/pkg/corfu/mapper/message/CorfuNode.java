package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

import de.wuespace.telestion.api.message.JsonMessage;
public interface CorfuNode extends JsonMessage {
	String IDENTIFIER_PROPERTY = "type";

	String HARDWARE_PROPERTY = "hardware";

	CorfuHardware hardware();

	short id();

	String name();
}
