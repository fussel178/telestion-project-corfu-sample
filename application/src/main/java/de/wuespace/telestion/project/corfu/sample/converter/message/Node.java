package de.wuespace.telestion.project.corfu.sample.converter.message;

import de.wuespace.telestion.api.message.JsonMessage;
public interface Node extends JsonMessage {
	String IDENTIFIER_PROPERTY = "type";

	String HARDWARE_PROPERTY = "hardware";

	Hardware hardware();

	short id();

	String name();
}
