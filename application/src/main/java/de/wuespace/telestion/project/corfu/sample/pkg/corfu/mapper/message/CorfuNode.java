package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

import de.wuespace.telestion.api.message.JsonMessage;

/**
 * Represents a node from Corfu with its hardware.
 * Every node definition should implement this interface.
 *
 * @author Ludwig Richter (@fussel178)
 */
public interface CorfuNode extends JsonMessage {
	/**
	 * The name of the JSON object property that represents the node name.
	 */
	String IDENTIFIER_PROPERTY = "type";

	/**
	 * The name of the JSON object property that represents the stored hardware.
	 */
	String HARDWARE_PROPERTY = "hardware";

	/**
	 * Returns the node hardware that is associated with the telecommand or telemetry message.
	 */
	CorfuHardware hardware();

	/**
	 * Returns the node id as defined in the Corfu configuration as unsigned byte.
	 */
	short id();

	/**
	 * Returns the node name as defined in the Corfu configuration.
	 */
	String name();
}
