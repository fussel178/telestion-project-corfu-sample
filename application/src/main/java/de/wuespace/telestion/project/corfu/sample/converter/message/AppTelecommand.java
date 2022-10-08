package de.wuespace.telestion.project.corfu.sample.converter.message;

public interface AppTelecommand extends App {
	String IDENTIFIER_PROPERTY = "type";

	String PAYLOAD_PROPERTY = "payload";

	AppTelecommandPayload payload();
}
