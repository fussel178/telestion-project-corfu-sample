package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

/**
 * Represents a Corfu application as telecommand specialization.
 * Every application that receive and interpret telecommand messages should implement this interface.
 *
 * @author Ludwig Richter (@fussel178)
 */
public interface CorfuAppTelecommand extends CorfuApp {
	/**
	 * The name of the JSON object property that represents the application name.
	 */
	String IDENTIFIER_PROPERTY = "type";

	/**
	 * The name of the JSON object property that represents the stored payload.
	 */
	String PAYLOAD_PROPERTY = "payload";

	/**
	 * Returns the telecommand payload that this app receives and interprets on the onboard target.
	 */
	AppTelecommandPayload payload();
}
