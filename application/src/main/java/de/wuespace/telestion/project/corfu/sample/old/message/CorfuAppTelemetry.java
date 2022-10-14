package de.wuespace.telestion.project.corfu.sample.old.message;

import de.wuespace.telestion.api.message.JsonMessage;

public interface CorfuAppTelemetry extends JsonMessage {
	/**
	 * Returns the associated byte id of the app that can send this telecommand.
	 */
	byte getAppId();

	/**
	 * Returns the associated byte id of this telemetry.
	 */
	byte getTelecommandId();
}
