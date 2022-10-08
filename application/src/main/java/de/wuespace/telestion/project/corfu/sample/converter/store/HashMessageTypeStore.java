package de.wuespace.telestion.project.corfu.sample.converter.store;

import de.wuespace.telestion.project.corfu.sample.converter.message.*;

import java.util.HashMap;
import java.util.Map;

public class HashMessageTypeStore implements MessageTypeStore {
	private final Map<String, Class<? extends Node>> nodeNameStore;
	private final Map<Short, Class<? extends Node>> nodeIdStore;
	private final Map<Class<? extends Node>, Class<? extends Hardware>> nodeHardwareStore;

	private final Map<String, Class<? extends AppTelemetry>> appTelemetryNameStore;
	private final Map<Short, Class<? extends AppTelemetry>> appTelemetryIdStore;
	private final Map<Short, Class<? extends AppTelemetryPayload>> telemetryPayloadStore;

	private final Map<String, Class<? extends AppTelecommand>> appTelecommandNameStore;
	private final Map<Short, Class<? extends AppTelecommand>> appTelecommandIdStore;
	private final Map<Short, Class<? extends AppTelecommandPayload>> telecommandPayloadStore;

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
	public void registerNode(Class<? extends Node> nodeClassType,
							 String nodeName,
							 short nodeId,
							 Class<? extends Hardware> nodeHardwareType) {
		nodeNameStore.put(nodeName, nodeClassType);
		nodeIdStore.put(nodeId, nodeClassType);
		nodeHardwareStore.put(nodeClassType, nodeHardwareType);
	}

	@Override
	public void registerAppTelemetry(Class<? extends AppTelemetry> classType, String appName, short appId) {
		appTelemetryNameStore.put(appName, classType);
		appTelemetryIdStore.put(appId, classType);
	}

	@Override
	public void registerTelemetryPayload(Class<? extends AppTelemetryPayload> payloadClassType,
										 String payloadName,
										 short payloadId,
										 Class<? extends AppTelemetry> associatedTelemetryApp) {
		if (!appTelemetryNameStore.containsValue(associatedTelemetryApp)) {
			throw new IllegalArgumentException(("The associated telemetry app %s is not yet registered. Please " +
					"register the telemetry app %s first and then the telemetry payload.")
					.formatted(associatedTelemetryApp.getName(), associatedTelemetryApp.getName()));
		}


		telemetryPayloadStore.put(
				payloadKey(getAppTelemetryId(associatedTelemetryApp), payloadId),
				payloadClassType
		);
	}

	@Override
	public void registerAppTelecommand(Class<? extends AppTelecommand> classType, String appName, short appId) {
		appTelecommandNameStore.put(appName, classType);
		appTelecommandIdStore.put(appId, classType);
	}

	@Override
	public void registerTelecommandPayload(Class<? extends AppTelecommandPayload> payloadClassType,
										   String payloadName,
										   short payloadId,
										   Class<? extends AppTelecommand> associatedTelecommandApp) {
		if (!appTelecommandNameStore.containsValue(associatedTelecommandApp)) {
			throw new IllegalArgumentException(("The associated telecommand app %s is not yet registered. Please " +
					"register the telecommand app %s first and then the telemetry payload.")
					.formatted(associatedTelecommandApp.getName(), associatedTelecommandApp.getName()));
		}

		telecommandPayloadStore.put(
				payloadKey(getAppTelecommandId(associatedTelecommandApp), payloadId),
				payloadClassType
		);
	}

	@Override
	public Class<? extends Node> getNodeType(String nodeName) {
		if (!nodeNameStore.containsKey(nodeName)) {
			throw new IllegalArgumentException(("Node with name %s does not exist. Please register one with name %s " +
					"and try again.").formatted(nodeName, nodeName));
		}

		return nodeNameStore.get(nodeName);
	}

	@Override
	public Class<? extends Node> getNodeType(short nodeId) {
		if (!nodeIdStore.containsKey(nodeId)) {
			throw new IllegalArgumentException(("Node with id 0x%02X does not exist. Please register one with id " +
					"0x%02X and try again.").formatted(nodeId, nodeId));
		}

		return nodeIdStore.get(nodeId);
	}

	@Override
	public Class<? extends Hardware> getHardwareType(short nodeId) {
		return nodeHardwareStore.get(getNodeType(nodeId));
	}

	@Override
	public Class<? extends Hardware> getHardwareType(String nodeName) {
		return nodeHardwareStore.get(getNodeType(nodeName));
	}

	@Override
	public Class<? extends AppTelemetry> getAppTelemetryType(String appName) {
		if (!appTelemetryNameStore.containsKey(appName)) {
			throw new IllegalArgumentException(("App Telemetry with name %s does not exist. Please register one " +
					"with name %s and try again.").formatted(appName, appName));
		}

		return appTelemetryNameStore.get(appName);
	}

	@Override
	public Class<? extends AppTelemetry> getAppTelemetryType(short appId) {
		if (!appTelemetryIdStore.containsKey(appId)) {
			throw new IllegalArgumentException(("App Telemetry with id 0x%02X does not exist. Please register one " +
					"with id 0x%02X and try again.").formatted(appId, appId));
		}

		return appTelemetryIdStore.get(appId);
	}

	@Override
	public Class<? extends AppTelemetryPayload> getTelemetryPayloadType(short appId, short payloadId) {
		short payloadKey = payloadKey(appId, payloadId);

		if (!telemetryPayloadStore.containsKey(payloadKey)) {
			throw new IllegalArgumentException(("Telemetry Payload with id 0x%02X on app with id 0x%02X does not " +
					"exist. Please register one with id 0x%02X on app with id 0x%02X and try again.")
					.formatted(payloadId, appId, payloadId, appId));
		}

		return telemetryPayloadStore.get(payloadKey);
	}

	@Override
	public Class<? extends AppTelecommand> getAppTelecommandType(String appName) {
		if (!appTelecommandNameStore.containsKey(appName)) {
			throw new IllegalArgumentException(("App Telemetry with name %s does not exist. Please register one " +
					"with name %s and try again.").formatted(appName, appName));
		}

		return appTelecommandNameStore.get(appName);
	}

	@Override
	public Class<? extends AppTelecommand> getAppTelecommandType(short appId) {
		if (!appTelecommandIdStore.containsKey(appId)) {
			throw new IllegalArgumentException(("App Telecommand with id 0x%02X does not exist. Please register one " +
					"with id 0x%02X and try again.").formatted(appId, appId));
		}

		return appTelecommandIdStore.get(appId);
	}

	@Override
	public Class<? extends AppTelecommandPayload> getTelecommandPayloadType(short appId, short payloadId) {
		short payloadKey = payloadKey(appId, payloadId);

		if (!telecommandPayloadStore.containsKey(payloadKey)) {
			throw new IllegalArgumentException(("Telecommand Payload with id 0x%02X on app with id 0x%02X does not " +
					"exist. Please register one with id 0x%02X on app with id 0x%02X and try again.")
					.formatted(payloadId, appId, payloadId, appId));
		}

		return telecommandPayloadStore.get(payloadKey);
	}

	private short getAppTelemetryId(Class<? extends AppTelemetry> classType) {
		for (var entry : appTelemetryIdStore.entrySet()) {
			if (entry.getValue().equals(classType)) {
				return entry.getKey();
			}
		}

		throw new IllegalStateException(("Sorry for the inconvenience. This is an unexpected error. Please contact " +
				"the maintainers and show this error message. Details: The app telemetry id store doesn't have " +
				"the requested class %s.").formatted(classType.getName()));
	}

	private short getAppTelecommandId(Class<? extends AppTelecommand> classType) {
		for (var entry : appTelecommandIdStore.entrySet()) {
			if (entry.getValue().equals(classType)) {
				return entry.getKey();
			}
		}

		throw new IllegalStateException(("Sorry for the inconvenience. This is an unexpected error. Please contact " +
				"the maintainers and show this error message. Details: The app telecommand id store doesn't have " +
				"the requested class %s.").formatted(classType.getName()));
	}

	private static short payloadKey(short appId, short payloadId) {
		return (short) (appId << 8 + payloadId);
	}
}
