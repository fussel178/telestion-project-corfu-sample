package de.wuespace.telestion.project.corfu.sample.converter.store;

import de.wuespace.telestion.project.corfu.sample.converter.message.*;

/**
 * <h2>Description</h2>
 *
 * The message type store acts as a translation layer between name or id of a corfu component
 * and the actual JVM class type that represents the corfu component.
 * <p>
 * Currently, the following components can be translated:
 * <ul>
 *     <li>{@link Node}</li>
 *     <li>{@link Hardware}</li>
 *     <li>{@link AppTelemetry}</li>
 *     <li>{@link AppTelemetryPayload}</li>
 *     <li>{@link AppTelecommand}</li>
 *     <li>{@link AppTelecommandPayload}</li>
 * </ul>
 *
 * The {@link HashMessageTypeStore} is the current reference implementation of the message type store.
 *
 * <h2>Usage</h2>
 *
 * <pre>
 * {@code
 * // resolve by name
 * var nodeTypeByName = store.getNodeType("obc");
 * System.out.println(nodeTypeByName.getSimpleName()); // ObcNode
 *
 * // resolve by id
 * var nodeTypeById = store.getNodeType(0x13);
 * System.out.println(nodeTypeById.getSimpleName()); // ObcNode
 * }
 * </pre>
 *
 * @author Ludwig Richter (@fussel178)
 */
public interface MessageTypeStore {

	void registerNode(Class<? extends Node> nodeClassType,
				  String nodeName,
				  short nodeId,
				  Class<? extends Hardware> nodeHardwareType);

	void registerAppTelemetry(Class<? extends AppTelemetry> classType, String appName, short appId);

	void registerTelemetryPayload(Class<? extends AppTelemetryPayload> payloadClassType,
				  String payloadName,
				  short payloadId,
				  Class<? extends AppTelemetry> associatedTelemetryApp);

	void registerAppTelecommand(Class<? extends AppTelecommand> classType, String appName, short appId);

	void registerTelecommandPayload(Class<? extends AppTelecommandPayload> payloadClassType,
									String payloadName,
									short payloadId,
									Class<? extends AppTelecommand> associatedTelecommandApp);

	/**
	 * Returns the node class type that is associated to the node name.
	 * @param nodeName the node name (e.g. <code>"obc"</code>)
	 * @return the node class type (e.g. <code>ObcNode.class</code>)
	 */
	Class<? extends Node> getNodeType(String nodeName);

	/**
	 * Returns the node class type that is associated to the node id.
	 * @param nodeId the node id as unsigned byte (e.g. <code>0x13</code>)
	 * @return the node class type (e.g. <code>ObcNode.class</code>)
	 */
	Class<? extends Node> getNodeType(short nodeId);

	/**
	 * Returns the hardware class type that is associated to the node with the specified node name.
	 * @param nodeName the node name (e.g. <code>"obc"</code>)
	 * @return the hardware class type (e.g. <code>ObcNode.Hardware.class</code>)
	 */
	Class<? extends Hardware> getHardwareType(String nodeName);

	/**
	 * Returns the hardware class type that is associated to the node with the specified node id.
	 * @param nodeId the node id as unsigned byte (e.g. <code>0x13</code>)
	 * @return the hardware class type (e.g. <code>ObcNode.Hardware.class</code>)
	 */
	Class<? extends Hardware> getHardwareType(short nodeId);

	/**
	 * Returns all available hardware definitions that are associated to the node with the specified node name.
	 * @param nodeName the node name (e.g. <code>"obc"</code>)
	 * @return the hardware definitions
	 * (e.g. <code>ObcNode.Hardware.MT0_ID0</code> or <code>ObcNode.Hardware.BROADCAST</code>)
	 */
	default Hardware[] getHardwareList(String nodeName) {
		var type = getHardwareType(nodeName);
		if (!type.isEnum()) {
			throw new IllegalArgumentException(("The hardware %s on node %s is not an enum. Currently, only hardware " +
					"as enum type are supported. Please convert the hardware %s to an enum and try again")
					.formatted(type.getName(), getNodeType(nodeName).getName(), type.getName()));
		}
		return type.getEnumConstants();
	}

	/**
	 * Returns all available hardware definitions that are associated to the node with the specified node id.
	 * @param nodeId the node id as unsigned byte (e.g. <code>"obc"</code>)
	 * @return the hardware definitions
	 * (e.g. <code>ObcNode.Hardware.MT0_ID0</code> or <code>ObcNode.Hardware.BROADCAST</code>)
	 */
	default Hardware[] getHardwareList(short nodeId) {
		var type = getHardwareType(nodeId);
		if (!type.isEnum()) {
			throw new IllegalArgumentException(("The hardware %s on node %s is not an enum. Currently, only hardware " +
					"as enum type are supported. Please convert the hardware %s to an enum and try again")
					.formatted(type.getName(), getNodeType(nodeId).getName(), type.getName()));
		}
		return type.getEnumConstants();
	}

	/**
	 * Returns the hardware definition that has the specified hardware id and is associated to the node with the
	 * specified node id.
	 * @param nodeId the node id as unsigned byte (e.g. <code>0x13</code>)
	 * @param hardwareId the hardware id as unsigned byte (e.g. <code>0x50</code>)
	 * @return the hardware definition (e.g. <code>ObcNode.Hardware.MT0_ID0</code>)
	 */
	default Hardware getHardware(short nodeId, short hardwareId) {
		var list = getHardwareList(nodeId);
		for (Hardware hardware : list) {
			if (hardware.id() == hardwareId) return hardware;
		}

		throw new IllegalArgumentException(("No hardware for id 0x%02X on node %s found. Please provide hardware " +
				"type with the id 0x%02X and try again.")
				.formatted(hardwareId, getNodeType(nodeId).getName(), hardwareId));
	}

	/**
	 * Returns the app telemetry class type that is associated to the app name.
	 * @param appName the app name (e.g. <code>"example-app"</code>)
	 * @return the app telemetry class type (e.g. <code>ExampleAppTelemetry.class</code>)
	 */
	Class<? extends AppTelemetry> getAppTelemetryType(String appName);

	/**
	 * Returns the app telemetry class type that is associated to the app id.
	 * @param appId the app id as unsigned byte (e.g. <code>0x89</code>)
	 * @return the app telemetry class type (e.g. <code>ExampleAppTelemetry.class</code>)
	 */
	Class<? extends AppTelemetry> getAppTelemetryType(short appId);

	/**
	 * Returns the telemetry payload class type that associated the specified payload id and the app telemetry with the
	 * specified app id.
	 * @param appId the app id as unsigned byte (e.g. <code>0x89</code>)
	 * @param payloadId the payload id as unsigned byte (e.g. <code>0x01</code>)
	 * @return the payload class type (e.g. <code>MyTelemetryTelemetryPayload.class</code>)
	 */
	Class<? extends AppTelemetryPayload> getTelemetryPayloadType(short appId, short payloadId);

	/**
	 * Returns the app telecommand class type that is associated to the app name.
	 * @param appName the app name (e.g. <code>"example-app"</code>)
	 * @return the app telecommand class type (e.g. <code>ExampleAppTelecommand.class</code>)
	 */
	Class<? extends AppTelecommand> getAppTelecommandType(String appName);

	/**
	 * Returns the app telecommand class type that is associated to the app id.
	 * @param appId the app id as unsigned byte (e.g. <code>0x89</code>)
	 * @return the app telecommand class type (e.g. <code>ExampleAppTelecommand.class</code>)
	 */
	Class<? extends AppTelecommand> getAppTelecommandType(short appId);

	/**
	 * Returns the telecommand payload class type that associated the specified payload id and the app telecommand with
	 * the specified app id.
	 * @param appId the app id as unsigned byte (e.g. <code>0x89</code>)
	 * @param payloadId the payload id as unsigned byte (e.g. <code>0x03</code>)
	 * @return the payload class type (e.g. <code>IncrementCounterTelecommandPayload.class</code>)
	 */
	Class<? extends AppTelecommandPayload> getTelecommandPayloadType(short appId, short payloadId);
}
