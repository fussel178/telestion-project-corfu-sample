package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

/**
 * Represents a generic payload of a {@link CorfuApp Corfu application}.
 * This interface acts as a root for the specializations {@link AppTelemetryPayload} and {@link AppTelecommandPayload}.
 * Actual Corfu payloads should implement the specializations in favor of this root interface.
 *
 * @author Ludwig Richter (@fussel178)
 */
public interface CorfuPayload extends CorfuStruct {

	short ANY_NODE_ID = 0xFF;

	/**
	 * Returns the id of the payload as defined in the Corfu configuration as unsigned byte.
	 */
	short id();

	/**
	 * Returns the name of the payload as defined in the Corfu configuration.
	 */
	String name();

	/**
	 * Returns the associated node id. This is needed because standard telemetry have different origins
	 * as nodes run different applications and therefore send different standard telemetry.
	 * Only one application (running on the node) "packs" the standard telemetry and sends them to ground.
	 * This parameter specifies if the payload is a node specific payload and which node is associated.
	 * <p>
	 * Note: Return {@link #ANY_NODE_ID} if the payload is <b>not</b> associated to any particular node.
	 */
	short nodeId();
}
