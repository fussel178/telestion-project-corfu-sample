package de.wuespace.telestion.project.corfu.sample.old.message;

import de.wuespace.telestion.api.message.JsonMessage;

public interface CorfuAppTelecommand extends JsonMessage {
	/**
	 * Returns the associated byte id of the app that can process this telecommand.
	 */
	byte getAppId();

	/**
	 * Returns the associated byte id of this telecommand.
	 */
	byte getTelecommandId();
}
