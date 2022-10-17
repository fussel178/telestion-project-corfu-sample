package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

import de.wuespace.telestion.api.message.JsonMessage;

/**
 * Represents a generic Corfu application.
 * This interface acts as a root for the specializations {@link CorfuAppTelemetry} and {@link CorfuAppTelecommand}.
 * Actual Corfu application should implement the specializations in favor of this root interface.
 *
 * @author Ludwig Richter (@fussel178)
 */
public interface CorfuApp extends JsonMessage {
	/**
	 * Returns the application id as defined in the Corfu configuration as unsigned byte.
	 */
	short id();

	/**
	 * Returns the application name as defined in the Corfu configuration.
	 */
	String name();

	/**
	 * Returns the actual payload of the application.
	 */
	CorfuPayload payload();
}
