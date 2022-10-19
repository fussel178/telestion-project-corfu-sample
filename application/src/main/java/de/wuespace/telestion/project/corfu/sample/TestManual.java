package de.wuespace.telestion.project.corfu.sample;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.CorfuMessageMapper;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuSerializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.HashMessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.pkg.util.ByteArrayUtils;
import de.wuespace.telestion.project.corfu.sample.pkg.util.ReflectionUtils;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.jackson.CorfuModule;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuTelecommand;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuTelemetry;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp.*;
import de.wuespace.telestion.project.corfu.sample.generated.osbw.node.obc.ObcNode;
import io.vertx.core.json.Json;
import io.vertx.core.json.jackson.DatabindCodec;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class TestManual {

	private static final List<Integer> intList = new ArrayList<>();

	public static void main(String[] args) throws NoSuchFieldException {

		int count = 17;
		int requiredBytes = count > 0 ? (count - 1) / 8 + 1 : 0;
		System.out.printf("Required bytes: %d, for needed bits: %d%n".formatted(requiredBytes, count));

		intList.add(1);
		intList.add(2);
		intList.add(3);
		intList.add(4);
		intList.add(5);

		var intListField = TestManual.class.getDeclaredField("intList");

		System.out.printf("Integer list type: %s%n", intList.getClass().getName());
		System.out.printf("List is assignable from: %b%n", List.class.isAssignableFrom(intList.getClass()));
		System.out.printf("List type: %s%n", ReflectionUtils.getListElementType(intListField));

		var bitSet = new BitSet(12);

		bitSet.set(1);
		bitSet.set(3);
		bitSet.set(6);
		bitSet.set(11);
		bitSet.set(12);

		var telemetry = new CorfuTelemetry(
				0,
				23478923L,
				2378297398L,
				false,
				new ObcNode(ObcNode.ObcHardware.MT0_ID0),
				new ExampleAppTelemetry(new MyBitarrayTelemetryTelemetryPayload(bitSet))
		);

		var telecommand = new CorfuTelecommand(
				(short) 20,
				(short) 0,
				0L,
				new ObcNode(ObcNode.ObcHardware.MT0_ID0),
				new ExampleAppTelecommand(new IncrementCounterTelecommandPayload((short) 1)));

		// register custom serializers and deserializers
		var store = new HashMessageTypeStore();
		DatabindCodec.mapper().registerModule(new CorfuModule(store));

		// TODO: Implement better method to register corfu components like an event listener
		store.registerNode(ObcNode.class, ObcNode.NODE_NAME, ObcNode.NODE_ID, ObcNode.ObcHardware.class);
		store.registerAppTelemetry(ExampleAppTelemetry.class, ExampleAppTelemetry.APP_TELEMETRY_NAME, ExampleAppTelemetry.APP_TELEMETRY_ID);
		store.registerTelemetryPayload(MyTelemetryTelemetryPayload.class, MyTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_NAME, MyTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_ID, MyTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_NODE_ID, ExampleAppTelemetry.class);
		store.registerTelemetryPayload(MyBitarrayTelemetryTelemetryPayload.class, MyBitarrayTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_NAME, MyBitarrayTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_ID, MyBitarrayTelemetryTelemetryPayload.TELEMETRY_PAYLOAD_NODE_ID, ExampleAppTelemetry.class);
		store.registerAppTelecommand(ExampleAppTelecommand.class, ExampleAppTelecommand.APP_TELECOMMAND_NAME, ExampleAppTelecommand.APP_TELECOMMAND_ID);
		store.registerTelecommandPayload(NopTelecommandPayload.class, NopTelecommandPayload.TELECOMMAND_PAYLOAD_NAME, NopTelecommandPayload.TELECOMMAND_PAYLOAD_ID, NopTelecommandPayload.TELECOMMAND_PAYLOAD_NODE_ID, ExampleAppTelecommand.class);
		store.registerTelecommandPayload(GenerateAnomalyTelecommandPayload.class, GenerateAnomalyTelecommandPayload.TELECOMMAND_PAYLOAD_NAME, GenerateAnomalyTelecommandPayload.TELECOMMAND_PAYLOAD_ID, GenerateAnomalyTelecommandPayload.TELECOMMAND_PAYLOAD_NODE_ID, ExampleAppTelecommand.class);
		store.registerTelecommandPayload(IncrementCounterTelecommandPayload.class, IncrementCounterTelecommandPayload.TELECOMMAND_PAYLOAD_NAME, IncrementCounterTelecommandPayload.TELECOMMAND_PAYLOAD_ID, IncrementCounterTelecommandPayload.TELECOMMAND_PAYLOAD_NODE_ID, ExampleAppTelecommand.class);
		store.registerTelecommandPayload(TwoParametersTelecommandPayload.class, TwoParametersTelecommandPayload.TELECOMMAND_PAYLOAD_NAME, TwoParametersTelecommandPayload.TELECOMMAND_PAYLOAD_ID, TwoParametersTelecommandPayload.TELECOMMAND_PAYLOAD_NODE_ID, ExampleAppTelecommand.class);

		//
		// JSON ENCODE / DECODE
		//

		// encode
		var encoded = Json.CODEC.toString(telemetry);
		System.out.println(encoded);
		System.out.printf("Encoded message size (from JSON string): %d bytes%n", encoded.length());

		// decode
		var decoded = Json.CODEC.fromString(encoded, CorfuTelemetry.class);
		System.out.println(decoded);

		//
		// CORFU BINARY ENCODE / DECODE
		//

		// serialize in Corfu binary format
		var serializer = new CorfuMessageMapper(store);
		try {
			byte[] serializedData = serializer.serialize(telecommand);
			System.out.println(telecommand);
			System.out.println(ByteArrayUtils.toString(serializedData));
		} catch (CorfuSerializationException e) {
			throw new RuntimeException(e);
		}

		try {
			byte[] serializedData = serializer.serialize(telecommand);
			System.out.println(telecommand);
			System.out.println(ByteArrayUtils.toString(serializedData));
		} catch (CorfuSerializationException e) {
			throw new RuntimeException(e);
		}


		byte[] serializedTM = new byte[]{
				(byte) 0xC9, //  0: checksum (type: uint16, block: 0)
				(byte) 0x3E, //  1: checksum (type: uint16, block: 1)
				(byte) 0x00, //  2: packet number (type: uint16, block: 2)
				(byte) 0x01, //  3: packet number (type: uint16, block: 3)
				(byte) 0x00, //  4: time UTC (type: int64, block: 0)
				(byte) 0x00, //  5: time UTC (type: int64, block: 1)
				(byte) 0x00, //  6: time UTC (type: int64, block: 2)
				(byte) 0x00, //  7: time UTC (type: int64, block: 3)
				(byte) 0x00, //  8: time UTC (type: int64, block: 4)
				(byte) 0x00, //  9: time UTC (type: int64, block: 5)
				(byte) 0x00, // 10: time UTC (type: int64, block: 6)
				(byte) 0x02, // 11: time UTC (type: int64, block: 7)
				(byte) 0x00, // 12: uptime (type: int64, block: 0)
				(byte) 0x00, // 13: uptime (type: int64, block: 1)
				(byte) 0x00, // 14: uptime (type: int64, block: 2)
				(byte) 0x00, // 15: uptime (type: int64, block: 3)
				(byte) 0x00, // 16: uptime (type: int64, block: 4)
				(byte) 0x00, // 17: uptime (type: int64, block: 5)
				(byte) 0x00, // 18: uptime (type: int64, block: 6)
				(byte) 0x04, // 19: uptime (type: int64, block: 7)
				(byte) 0x50, // 20: Node ID (type: uint8, block: 0)
				(byte) 0x13, // 21: Hardware ID (type: uint8, block: 0)
				(byte) 0x89, // 22: Service / App ID (type: uint8, block: 0)
				(byte) 0x01, // 23: Subservice / Telemetry / Payload ID (type: uint8, block: 0)
				(byte) 0x00, // 24: Payload length (type: uint16, block: 0)
				(byte) 0x05, // 25: Payload length (type: uint16, block: 1)
				(byte) 0x00, // 26: is historic telemetry (type: boolean, block: 0)
				(byte) 0x04, // 27: myIntField from MyTelemetryPayload from ExampleApp (type: uint8, block: 0)
				(byte) 0x00, // 28: myFloatField from MyTelemetryPayload from ExampleApp (type: float, block: 0)
				(byte) 0x00, // 29: myFloatField from MyTelemetryPayload from ExampleApp (type: float, block: 1)
				(byte) 0x00, // 30: myFloatField from MyTelemetryPayload from ExampleApp (type: float, block: 2)
				(byte) 0x00, // 31: myFloatField from MyTelemetryPayload from ExampleApp (type: float, block: 3)
		};

		float myFloatValue = 3.41f;
		int encodedFloatValue = Float.floatToIntBits(myFloatValue);
		// insert encoded float into serialized data
		serializedTM[28] = (byte)(encodedFloatValue >>> 24);
		serializedTM[29] = (byte)(encodedFloatValue >>> 16);
		serializedTM[30] = (byte)(encodedFloatValue >>>  8);
		serializedTM[31] = (byte)(encodedFloatValue);

		System.out.printf("Serialized telemetry: %s%n", ByteArrayUtils.toString(serializedTM));

		CorfuTelemetry deserializedTM;
		try {
			deserializedTM = serializer.deserialize(serializedTM);
			System.out.printf("Deserialized telemetry: %s%n", deserializedTM);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		var jsonEncodedTM = Json.CODEC.toString(deserializedTM);
		System.out.printf("JSON encoded deserialized telemetry: %s%n", jsonEncodedTM);

		var jsonDecodedTM = Json.CODEC.fromString(jsonEncodedTM, CorfuTelemetry.class);
		System.out.printf("JSON decoded deserialized telemetry: %s%n", jsonDecodedTM);
	}
}
