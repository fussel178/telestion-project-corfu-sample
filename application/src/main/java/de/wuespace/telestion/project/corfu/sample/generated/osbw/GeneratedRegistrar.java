package de.wuespace.telestion.project.corfu.sample.generated.osbw;

import de.wuespace.telestion.project.corfu.sample.converter.store.MessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp.*;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.node.ObcNode;
import de.wuespace.telestion.project.corfu.sample.verticle.converter.Registrar;

public class GeneratedRegistrar implements Registrar {
	@Override
	public void onRegister(MessageTypeStore store) {
		store.registerNode(ObcNode.class, ObcNode.NODE_NAME, ObcNode.NODE_ID, ObcNode.ObcHardware.class);
		store.registerAppTelemetry(ExampleAppTelemetry.class, ExampleAppTelemetry.APP_TELEMETRY_NAME, ExampleAppTelemetry.APP_TELEMETRY_ID);
		store.registerTelemetryPayload(MyTelemetryTelemetryPayload.class, MyTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_NAME, MyTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_ID, ExampleAppTelemetry.class);
		store.registerTelemetryPayload(MyBitarrayTelemetryTelemetryPayload.class, MyBitarrayTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_NAME, MyBitarrayTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_ID, ExampleAppTelemetry.class);
		store.registerAppTelecommand(ExampleAppTelecommand.class, ExampleAppTelecommand.APP_TELECOMMAND_NAME, ExampleAppTelecommand.APP_TELECOMMAND_ID);
		store.registerTelecommandPayload(NopTelecommandPayload.class, NopTelecommandPayload.TELECOMMAND_PAYLOAD_NAME, NopTelecommandPayload.TELECOMMAND_PAYLOAD_ID, ExampleAppTelecommand.class);
		store.registerTelecommandPayload(GenerateAnomalyTelecommandPayload.class, GenerateAnomalyTelecommandPayload.TELECOMMAND_PAYLOAD_NAME, GenerateAnomalyTelecommandPayload.TELECOMMAND_PAYLOAD_ID, ExampleAppTelecommand.class);
		store.registerTelecommandPayload(IncrementCounterTelecommandPayload.class, IncrementCounterTelecommandPayload.TELECOMMAND_PAYLOAD_NAME, IncrementCounterTelecommandPayload.TELECOMMAND_PAYLOAD_ID, ExampleAppTelecommand.class);
		store.registerTelecommandPayload(TwoParametersTelecommandPayload.class, TwoParametersTelecommandPayload.TELECOMMAND_PAYLOAD_NAME, TwoParametersTelecommandPayload.TELECOMMAND_PAYLOAD_ID, ExampleAppTelecommand.class);
	}
}
