package de.wuespace.telestion.project.corfu.sample.old.message;

import de.wuespace.telestion.api.message.JsonMessage;

public interface CorfuNodeId extends JsonMessage {
	/**
	 * Returns the associated node byte id to this node type.
	 */
	byte getId();

	/**
	 * Returns <code>true</code>, when the hardware id is a broadcast.
	 */
	boolean isHardwareBroadcast();
}
