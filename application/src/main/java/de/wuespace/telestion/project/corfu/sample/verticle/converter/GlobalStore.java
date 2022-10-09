package de.wuespace.telestion.project.corfu.sample.verticle.converter;

import de.wuespace.telestion.project.corfu.sample.converter.message.*;
import de.wuespace.telestion.project.corfu.sample.converter.store.HashMessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.converter.store.MessageTypeStore;

public class GlobalStore extends HashMessageTypeStore {

	private static final MessageTypeStore store = new GlobalStore();

	public static MessageTypeStore store() {
		return store;
	}

	private GlobalStore() {
	}

	@Override
	public synchronized void registerNode(Class<? extends Node> nodeClassType,
										  String nodeName,
										  short nodeId,
										  Class<? extends Hardware> nodeHardwareType) {
		super.registerNode(nodeClassType, nodeName, nodeId, nodeHardwareType);
	}

	@Override
	public synchronized void registerAppTelemetry(Class<? extends AppTelemetry> classType,
												  String appName,
												  short appId) {
		super.registerAppTelemetry(classType, appName, appId);
	}

	@Override
	public synchronized void registerTelemetryPayload(Class<? extends AppTelemetryPayload> payloadClassType,
													  String payloadName,
													  short payloadId,
													  Class<? extends AppTelemetry> associatedTelemetryApp) {
		super.registerTelemetryPayload(payloadClassType, payloadName, payloadId, associatedTelemetryApp);
	}

	@Override
	public synchronized void registerAppTelecommand(Class<? extends AppTelecommand> classType,
													String appName,
													short appId) {
		super.registerAppTelecommand(classType, appName, appId);
	}

	@Override
	public synchronized void registerTelecommandPayload(Class<? extends AppTelecommandPayload> payloadClassType,
														String payloadName,
														short payloadId,
														Class<? extends AppTelecommand> associatedTelecommandApp) {
		super.registerTelecommandPayload(payloadClassType, payloadName, payloadId, associatedTelecommandApp);
	}
}
