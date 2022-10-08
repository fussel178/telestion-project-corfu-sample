package de.wuespace.telestion.project.corfu.sample.converter.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelecommand;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelemetry;
import de.wuespace.telestion.project.corfu.sample.converter.message.Node;
import de.wuespace.telestion.project.corfu.sample.converter.store.MessageTypeStore;

import java.util.BitSet;

public class CorfuModule extends SimpleModule {
	public CorfuModule(MessageTypeStore store) {
		// utility types
		addSerializer(BitSet.class, new BitSetSerializer());
		addDeserializer(BitSet.class, new BitSetDeserializer());
		// node
		addSerializer(Node.class, new NodeSerializer(store));
		addDeserializer(Node.class, new NodeDeserializer(store));
		// app telemetry
		addSerializer(AppTelemetry.class, new AppTelemetrySerializer(store));
		addDeserializer(AppTelemetry.class, new AppTelemetryDeserializer(store));
		// app telecommand
		addSerializer(AppTelecommand.class, new AppTelecommandSerializer(store));
		addDeserializer(AppTelecommand.class, new AppTelecommandDeserializer(store));
	}
}
