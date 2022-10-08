package de.wuespace.telestion.project.corfu.sample.converter.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelemetry;
import de.wuespace.telestion.project.corfu.sample.converter.store.MessageTypeStore;

import java.io.IOException;

public class AppTelemetryDeserializer extends StdNodeBasedDeserializer<AppTelemetry> {

	private final MessageTypeStore store;

	public AppTelemetryDeserializer(MessageTypeStore store) {
		this(null, store);
	}

	public AppTelemetryDeserializer(Class<AppTelemetry> targetType, MessageTypeStore store) {
		super(targetType);
		this.store = store;
	}

	@Override
	public AppTelemetry convert(JsonNode root, DeserializationContext context) throws IOException {
		if (!root.isObject()) return null;
		var identifierNode = root.get(AppTelemetry.IDENTIFIER_PROPERTY);
		if (!identifierNode.isTextual()) return null;

		return context.readTreeAsValue(root, store.getAppTelemetryType(identifierNode.asText()));
	}
}
