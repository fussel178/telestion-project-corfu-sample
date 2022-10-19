package de.wuespace.telestion.project.corfu.sample.generated.osbw;

import de.wuespace.telestion.project.corfu.sample.generated.osbw.app.housekeeper.HousekeeperAppTelemetry;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.app.timemanager.SetClockDriftTelecommandPayload;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.app.timemanager.SetUtcTelecommandPayload;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.app.timemanager.TimeManagerTelecommand;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.node.obc.ObcNode;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.node.obc.ObcStandardTelemetryPayload;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp.*;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle.global.Registrar;

@SuppressWarnings("unused")
public class GeneratedRegistrar implements Registrar {
	@Override
	public void onRegister(MessageTypeStore store) {
		// nodes
		store.registerNode(
				ObcNode.class,
				ObcNode.NODE_NAME,
				ObcNode.NODE_ID,
				ObcNode.ObcHardware.class
		);
		// app telemetries
		store.registerAppTelemetry(
				ExampleAppTelemetry.class,
				ExampleAppTelemetry.APP_TELEMETRY_NAME,
				ExampleAppTelemetry.APP_TELEMETRY_ID
		);
		store.registerAppTelemetry(
				HousekeeperAppTelemetry.class,
				HousekeeperAppTelemetry.APP_TELEMETRY_NAME,
				HousekeeperAppTelemetry.APP_TELEMETRY_ID
		);
		// telemetry payloads
		store.registerTelemetryPayload(
				MyTelemetryTelemetryPayload.class,
				MyTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_NAME,
				MyTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_ID,
				MyTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_NODE_ID,
				ExampleAppTelemetry.class
		);
		store.registerTelemetryPayload(
				MyBitarrayTelemetryTelemetryPayload.class,
				MyBitarrayTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_NAME,
				MyBitarrayTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_ID,
				MyBitarrayTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_NODE_ID,
				ExampleAppTelemetry.class
		);
		// app telecommands
		store.registerAppTelecommand(
				ExampleAppTelecommand.class,
				ExampleAppTelecommand.APP_TELECOMMAND_NAME,
				ExampleAppTelecommand.APP_TELECOMMAND_ID
		);
		store.registerAppTelecommand(
				TimeManagerTelecommand.class,
				TimeManagerTelecommand.APP_TELECOMMAND_NAME,
				TimeManagerTelecommand.APP_TELECOMMAND_ID
		);
		store.registerTelemetryPayload(
				ObcStandardTelemetryPayload.class,
				ObcStandardTelemetryPayload.TELEMETRY_PAYLOAD_NAME,
				ObcStandardTelemetryPayload.TELEMETRY_PAYLOAD_ID,
				ObcStandardTelemetryPayload.TELEMETRY_PAYLOAD_NODE_ID,
				HousekeeperAppTelemetry.class
		);
		// telecommand payloads
		store.registerTelecommandPayload(
				NopTelecommandPayload.class,
				NopTelecommandPayload.TELECOMMAND_PAYLOAD_NAME,
				NopTelecommandPayload.TELECOMMAND_PAYLOAD_ID,
				NopTelecommandPayload.TELECOMMAND_PAYLOAD_NODE_ID,
				ExampleAppTelecommand.class
		);
		store.registerTelecommandPayload(
				GenerateAnomalyTelecommandPayload.class,
				GenerateAnomalyTelecommandPayload.TELECOMMAND_PAYLOAD_NAME,
				GenerateAnomalyTelecommandPayload.TELECOMMAND_PAYLOAD_ID,
				GenerateAnomalyTelecommandPayload.TELECOMMAND_PAYLOAD_NODE_ID,
				ExampleAppTelecommand.class
		);
		store.registerTelecommandPayload(
				IncrementCounterTelecommandPayload.class,
				IncrementCounterTelecommandPayload.TELECOMMAND_PAYLOAD_NAME,
				IncrementCounterTelecommandPayload.TELECOMMAND_PAYLOAD_ID,
				IncrementCounterTelecommandPayload.TELECOMMAND_PAYLOAD_NODE_ID,
				ExampleAppTelecommand.class
		);
		store.registerTelecommandPayload(
				TwoParametersTelecommandPayload.class,
				TwoParametersTelecommandPayload.TELECOMMAND_PAYLOAD_NAME,
				TwoParametersTelecommandPayload.TELECOMMAND_PAYLOAD_ID,
				TwoParametersTelecommandPayload.TELECOMMAND_PAYLOAD_NODE_ID,
				ExampleAppTelecommand.class
		);
		store.registerTelecommandPayload(
				SetClockDriftTelecommandPayload.class,
				SetClockDriftTelecommandPayload.TELECOMMAND_PAYLOAD_NAME,
				SetClockDriftTelecommandPayload.TELECOMMAND_PAYLOAD_ID,
				SetClockDriftTelecommandPayload.TELECOMMAND_PAYLOAD_NODE_ID,
				TimeManagerTelecommand.class
		);
		store.registerTelecommandPayload(
				SetUtcTelecommandPayload.class,
				SetUtcTelecommandPayload.TELECOMMAND_PAYLOAD_NAME,
				SetUtcTelecommandPayload.TELECOMMAND_PAYLOAD_ID,
				SetUtcTelecommandPayload.TELECOMMAND_PAYLOAD_NODE_ID,
				TimeManagerTelecommand.class
		);
	}
}
