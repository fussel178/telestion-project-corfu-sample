package de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle.global;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.*;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.HashMessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeStore;

public class GlobalStore extends HashMessageTypeStore {

	private static final MessageTypeStore store = new GlobalStore();

	public static MessageTypeStore store() {
		return store;
	}

	private GlobalStore() {
	}

	@Override
	public synchronized void registerNode(Class<? extends CorfuNode> nodeClassType,
										  String nodeName,
										  short nodeId,
										  Class<? extends CorfuHardware> nodeHardwareType) {
		super.registerNode(nodeClassType, nodeName, nodeId, nodeHardwareType);
	}

	@Override
	public synchronized void registerAppTelemetry(Class<? extends CorfuAppTelemetry> classType,
												  String appName,
												  short appId) {
		super.registerAppTelemetry(classType, appName, appId);
	}

	@Override
	public synchronized void registerTelemetryPayload(Class<? extends AppTelemetryPayload> payloadClassType,
													  String payloadName,
													  short payloadId,
													  short nodeId,
													  Class<? extends CorfuAppTelemetry> associatedTelemetryApp) {
		super.registerTelemetryPayload(payloadClassType, payloadName, payloadId, nodeId, associatedTelemetryApp);
	}

	@Override
	public synchronized void registerAppTelecommand(Class<? extends CorfuAppTelecommand> classType,
													String appName,
													short appId) {
		super.registerAppTelecommand(classType, appName, appId);
	}

	@Override
	public synchronized void registerTelecommandPayload(Class<? extends AppTelecommandPayload> payloadClassType,
														String payloadName,
														short payloadId,
														short nodeId,
														Class<? extends CorfuAppTelecommand> associatedTelecommandApp) {
		super.registerTelecommandPayload(payloadClassType, payloadName, payloadId, nodeId, associatedTelecommandApp);
	}
}
