package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A reference implementation for the {@link MessageTypeStore} that utilizes {@link HashMap HashMaps}
 * to store class-id and class-name references for:
 *
 * <ul>
 *     <li>{@link CorfuNode}</li>
 *     <li>{@link CorfuAppTelemetry}</li>
 *     <li>{@link AppTelemetryPayload}</li>
 *     <li>{@link CorfuAppTelecommand}</li>
 *     <li>{@link AppTelecommandPayload}</li>
 * </ul>
 *
 * @author Ludwig Richter (@fussel178)
 */
public class HashMessageTypeStore implements MessageTypeStore {
	private final Map<String, Class<? extends CorfuNode>> nodeNameStore;
	private final Map<Short, Class<? extends CorfuNode>> nodeIdStore;
	private final Map<Class<? extends CorfuNode>, Class<? extends CorfuHardware>> nodeHardwareStore;

	private final Map<String, Class<? extends CorfuAppTelemetry>> appTelemetryNameStore;
	private final Map<Short, Class<? extends CorfuAppTelemetry>> appTelemetryIdStore;
	private final Map<Integer, Class<? extends AppTelemetryPayload>> telemetryPayloadStore;

	private final Map<String, Class<? extends CorfuAppTelecommand>> appTelecommandNameStore;
	private final Map<Short, Class<? extends CorfuAppTelecommand>> appTelecommandIdStore;
	private final Map<Integer, Class<? extends AppTelecommandPayload>> telecommandPayloadStore;

	public HashMessageTypeStore() {
		this.nodeNameStore = new HashMap<>();
		this.nodeIdStore = new HashMap<>();
		this.nodeHardwareStore = new HashMap<>();

		this.appTelemetryNameStore = new HashMap<>();
		this.appTelemetryIdStore = new HashMap<>();
		this.telemetryPayloadStore = new HashMap<>();

		this.appTelecommandNameStore = new HashMap<>();
		this.appTelecommandIdStore = new HashMap<>();
		this.telecommandPayloadStore = new HashMap<>();
	}

	@Override
	public void registerNode(Class<? extends CorfuNode> nodeClassType,
							 String nodeName,
							 short nodeId,
							 Class<? extends CorfuHardware> hardwareClassType) {

		if (nodeNameStore.containsKey(nodeName)) {
			var registeredClassType = nodeNameStore.get(nodeName);
			throw new IllegalArgumentException(("Cannot register new node %s with name \"%s\" because another node " +
					"%s is already registered with this name. Please change the name of the node %s or remove the " +
					"already registered node %s from the store and try again.")
					.formatted(
							nodeClassType.getName(),
							nodeName,
							registeredClassType.getName(),
							nodeClassType.getName(),
							registeredClassType.getName()
					)
			);
		}

		if (nodeIdStore.containsKey(nodeId)) {
			var registeredClassType = nodeIdStore.get(nodeId);
			throw new IllegalArgumentException(("Cannot register new node %s with id %d because another node %s is " +
					"already registered with this name. Please change the id of the node %s or remove the already " +
					"registered node %s from the store and try again.")
					.formatted(
							nodeClassType.getName(),
							nodeId,
							registeredClassType.getName(),
							nodeClassType.getName(),
							registeredClassType.getName()
					)
			);
		}

		nodeNameStore.put(nodeName, nodeClassType);
		nodeIdStore.put(nodeId, nodeClassType);
		nodeHardwareStore.put(nodeClassType, hardwareClassType);
	}

	@Override
	public void registerAppTelemetry(Class<? extends CorfuAppTelemetry> classType, String appName, short appId) {
		if (appTelemetryNameStore.containsKey(appName)) {
			var registeredClassType = appTelemetryNameStore.get(appName);
			throw new IllegalArgumentException(("Cannot register new telemetry app %s with name \"%s\" because " +
					"another telemetry app %s is already registered with this name. Please change the name of the " +
					"telemetry app %s or remove the already registered telemetry app %s and try again.")
					.formatted(
							classType.getName(),
							appName,
							registeredClassType.getName(),
							classType.getName(),
							registeredClassType.getName()
					)
			);
		}

		if (appTelemetryIdStore.containsKey(appId)) {
			var registeredClassType = appTelemetryIdStore.get(appId);
			throw new IllegalArgumentException(("Cannot register new telemetry app %s with id %d because " +
					"another telemetry app %s is already registered with this name. Please change the id of the " +
					"telemetry app %s or remove the already registered telemetry app %s and try again.")
					.formatted(
							classType.getName(),
							appId,
							registeredClassType.getName(),
							classType.getName(),
							registeredClassType.getName()
					)
			);
		}

		appTelemetryNameStore.put(appName, classType);
		appTelemetryIdStore.put(appId, classType);
	}

	@Override
	public void registerAppTelecommand(Class<? extends CorfuAppTelecommand> classType, String appName, short appId) {
		if (appTelecommandNameStore.containsKey(appName)) {
			var registeredClassType = appTelecommandNameStore.get(appName);
			throw new IllegalArgumentException(("Cannot register new telecommand app %s with name \"%s\" because " +
					"another telecommand app %s is already registered with this name. Please change the name of the " +
					"telecommand app %s or remove the already registered telecommand app %s and try again.")
					.formatted(
							classType.getName(),
							appName,
							registeredClassType.getName(),
							classType.getName(),
							registeredClassType.getName()
					)
			);
		}

		if (appTelecommandIdStore.containsKey(appId)) {
			var registeredClassType = appTelecommandIdStore.get(appId);
			throw new IllegalArgumentException(("Cannot register new telecommand app %s with id %d because " +
					"another telecommand app %s is already registered with this name. Please change the id of the " +
					"telecommand app %s or remove the already registered telecommand app %s and try again.")
					.formatted(
							classType.getName(),
							appId,
							registeredClassType.getName(),
							classType.getName(),
							registeredClassType.getName()
					)
			);
		}

		appTelecommandNameStore.put(appName, classType);
		appTelecommandIdStore.put(appId, classType);
	}

	@Override
	public void registerTelemetryPayload(Class<? extends AppTelemetryPayload> payloadClassType,
										 String payloadName,
										 short payloadId,
										 short nodeId,
										 Class<? extends CorfuAppTelemetry> associatedTelemetryApp) {

		if (!appTelemetryNameStore.containsValue(associatedTelemetryApp)) {
			throw new IllegalArgumentException(("Cannot register new telemetry payload %s because the associated " +
					"telemetry app %s is not yet registered. Please register the telemetry app %s first and then " +
					"try to register the telemetry payload %s again.")
					.formatted(
							payloadClassType.getName(),
							associatedTelemetryApp.getName(),
							associatedTelemetryApp.getName(),
							payloadClassType.getName()
					)
			);
		}

		var key = payloadKey(getAppTelemetryId(associatedTelemetryApp), payloadId, nodeId);
		if (telemetryPayloadStore.containsKey(key)) {
			var registeredClassType = telemetryPayloadStore.get(key);
			throw new IllegalArgumentException(("Cannot register new telemetry payload %s because another telemetry " +
					"payload %s is already registered with the combination of associated app %s, payload id %d and " +
					"node id %d. Please change at least one of the parameters in telemetry payload %s or remove " +
					"the already registered telemetry payload %s and try again.")
					.formatted(
							payloadClassType.getName(),
							registeredClassType.getName(),
							associatedTelemetryApp.getName(),
							payloadId,
							nodeId,
							payloadClassType.getName(),
							registeredClassType.getName()
					)
			);
		}

		telemetryPayloadStore.put(key, payloadClassType);
	}

	@Override
	public void registerTelecommandPayload(Class<? extends AppTelecommandPayload> payloadClassType,
										   String payloadName,
										   short payloadId,
										   short nodeId,
										   Class<? extends CorfuAppTelecommand> associatedTelecommandApp) {

		if (!appTelecommandNameStore.containsValue(associatedTelecommandApp)) {
			throw new IllegalArgumentException(("Cannot register new telecommand payload %s because the associated " +
					"telecommand app %s is not yet registered. Please register the telecommand app %s first and then " +
					"try to register the telecommand payload %s again.")
					.formatted(
							payloadClassType.getName(),
							associatedTelecommandApp.getName(),
							associatedTelecommandApp.getName(),
							payloadClassType.getName()
					)
			);
		}

		var key = payloadKey(getAppTelecommandId(associatedTelecommandApp), payloadId, nodeId);
		if (telecommandPayloadStore.containsKey(key)) {
			var registeredClassType = telecommandPayloadStore.get(key);
			throw new IllegalArgumentException(("Cannot register new telecommand payload %s because another " +
					"telecommand payload %s is already registered with the combination of associated app %s, payload " +
					"id %d and node id %d. Please change at least one of the parameters in telecommand payload %s or " +
					"remove the already registered telecommand payload %s and try again.")
					.formatted(
							payloadClassType.getName(),
							registeredClassType.getName(),
							associatedTelecommandApp.getName(),
							payloadId,
							nodeId,
							payloadClassType.getName(),
							registeredClassType.getName()
					)
			);
		}

		telecommandPayloadStore.put(key, payloadClassType);
	}

	@Override
	public Class<? extends CorfuNode> getNodeType(String nodeName) {
		if (!nodeNameStore.containsKey(nodeName)) {
			throw new IllegalArgumentException(("Node with name %s does not exist. Please register one with name %s " +
					"and try again.").formatted(nodeName, nodeName));
		}

		return nodeNameStore.get(nodeName);
	}

	@Override
	public Class<? extends CorfuNode> getNodeType(short nodeId) {
		if (!nodeIdStore.containsKey(nodeId)) {
			throw new IllegalArgumentException(("Node with id 0x%02X does not exist. Please register one with id " +
					"0x%02X and try again.").formatted(nodeId, nodeId));
		}

		return nodeIdStore.get(nodeId);
	}

	@Override
	public Class<? extends CorfuHardware> getHardwareType(short nodeId) {
		return nodeHardwareStore.get(getNodeType(nodeId));
	}

	@Override
	public Class<? extends CorfuHardware> getHardwareType(String nodeName) {
		return nodeHardwareStore.get(getNodeType(nodeName));
	}

	@Override
	public Class<? extends CorfuAppTelemetry> getAppTelemetryType(String appName) {
		if (!appTelemetryNameStore.containsKey(appName)) {
			throw new IllegalArgumentException(("App Telemetry with name %s does not exist. Please register one " +
					"with name %s and try again.").formatted(appName, appName));
		}

		return appTelemetryNameStore.get(appName);
	}

	@Override
	public Class<? extends CorfuAppTelemetry> getAppTelemetryType(short appId) {
		if (!appTelemetryIdStore.containsKey(appId)) {
			throw new IllegalArgumentException(("App Telemetry with id 0x%02X does not exist. Please register one " +
					"with id 0x%02X and try again.").formatted(appId, appId));
		}

		return appTelemetryIdStore.get(appId);
	}

	@Override
	public Class<? extends AppTelemetryPayload> getTelemetryPayloadType(short appId, short payloadId, short nodeId) {
		int nodeSpecificKey = payloadKey(appId, payloadId, nodeId);
		int genericKey = payloadKey(appId, payloadId, CorfuPayload.ANY_NODE_ID);

		if (telemetryPayloadStore.containsKey(nodeSpecificKey)) {
			return telemetryPayloadStore.get(nodeSpecificKey);
		}

		if (!telemetryPayloadStore.containsKey(genericKey)) {
			throw new IllegalArgumentException(("Telemetry Payload with id 0x%02X on app with id 0x%02X does not " +
					"exist. Please register one with id 0x%02X on app with id 0x%02X and try again.")
					.formatted(payloadId, appId, payloadId, appId));
		}

		return telemetryPayloadStore.get(genericKey);
	}

	@Override
	public Class<? extends CorfuAppTelecommand> getAppTelecommandType(String appName) {
		if (!appTelecommandNameStore.containsKey(appName)) {
			throw new IllegalArgumentException(("App Telemetry with name %s does not exist. Please register one " +
					"with name %s and try again.").formatted(appName, appName));
		}

		return appTelecommandNameStore.get(appName);
	}

	@Override
	public Class<? extends CorfuAppTelecommand> getAppTelecommandType(short appId) {
		if (!appTelecommandIdStore.containsKey(appId)) {
			throw new IllegalArgumentException(("App Telecommand with id 0x%02X does not exist. Please register one " +
					"with id 0x%02X and try again.").formatted(appId, appId));
		}

		return appTelecommandIdStore.get(appId);
	}

	@Override
	public Class<? extends AppTelecommandPayload> getTelecommandPayloadType(short appId, short payloadId, short nodeId) {
		int nodeSpecificKey = payloadKey(appId, payloadId, nodeId);
		int genericKey = payloadKey(appId, payloadId, CorfuPayload.ANY_NODE_ID);

		if (telecommandPayloadStore.containsKey(nodeSpecificKey)) {
			return telecommandPayloadStore.get(nodeSpecificKey);
		}

		if (!telecommandPayloadStore.containsKey(genericKey)) {
			throw new IllegalArgumentException(("Telecommand Payload with id 0x%02X on app with id 0x%02X does not " +
					"exist. Please register one with id 0x%02X on app with id 0x%02X and try again.")
					.formatted(payloadId, appId, payloadId, appId));
		}

		return telecommandPayloadStore.get(genericKey);
	}

	@Override
	public int numberOfNodes() {
		return nodeNameStore.size();
	}

	@Override
	public int numberOfAppTelemetries() {
		return appTelemetryNameStore.size();
	}

	@Override
	public int numberOfAppTelecommands() {
		return appTelecommandNameStore.size();
	}

	@Override
	public int numberOfTelemetryPayloads() {
		return telemetryPayloadStore.size();
	}

	@Override
	public int numberOfTelecommandPayloads() {
		return telecommandPayloadStore.size();
	}

	/**
	 * Returns the id of the registered telemetry app by searching through every registered app.
	 *
	 * @param classType the class type of the telemetry app
	 * @return the id of the telemetry app
	 */
	private short getAppTelemetryId(Class<? extends CorfuAppTelemetry> classType) {
		for (var entry : appTelemetryIdStore.entrySet()) {
			if (entry.getValue().equals(classType)) {
				return entry.getKey();
			}
		}

		throw new IllegalStateException(("Sorry for the inconvenience. This is an unexpected error. Please contact " +
				"the maintainers and show this error message. Details: The app telemetry id store doesn't have " +
				"the requested class %s.").formatted(classType.getName()));
	}

	/**
	 * Returns the id of the registered telecommand app by searching through every registered app.
	 *
	 * @param classType the class type of the telecommand app
	 * @return the id of the telecommand app
	 */
	private short getAppTelecommandId(Class<? extends CorfuAppTelecommand> classType) {
		for (var entry : appTelecommandIdStore.entrySet()) {
			if (entry.getValue().equals(classType)) {
				return entry.getKey();
			}
		}

		throw new IllegalStateException(("Sorry for the inconvenience. This is an unexpected error. Please contact " +
				"the maintainers and show this error message. Details: The app telecommand id store doesn't have " +
				"the requested class %s.").formatted(classType.getName()));
	}

	/**
	 * Transfer function to calculate the key of the hash map that stores the payloads.
	 *
	 * @param appId     the id of the app that provides this payload
	 * @param payloadId the id of the actual payload
	 * @param nodeId    the id of the associated node (use {@link CorfuPayload#ANY_NODE_ID} if there is no associated node)
	 * @return the key for the payload hash map
	 */
	private static int payloadKey(short appId, short payloadId, short nodeId) {
		return (appId << 16) + (payloadId << 8) + nodeId;
	}
}
