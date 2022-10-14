package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

public interface CorfuAppTelecommand extends CorfuApp {
	String IDENTIFIER_PROPERTY = "type";

	String PAYLOAD_PROPERTY = "payload";

	AppTelecommandPayload payload();
}
