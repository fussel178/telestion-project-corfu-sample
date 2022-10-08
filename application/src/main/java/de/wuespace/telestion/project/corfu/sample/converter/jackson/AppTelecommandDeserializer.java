package de.wuespace.telestion.project.corfu.sample.converter.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelecommand;
import de.wuespace.telestion.project.corfu.sample.converter.store.MessageTypeStore;

import java.io.IOException;

public class AppTelecommandDeserializer extends StdNodeBasedDeserializer<AppTelecommand> {

	private final MessageTypeStore store;

	public AppTelecommandDeserializer(MessageTypeStore store) {
		this(null, store);
	}

	public AppTelecommandDeserializer(Class<AppTelecommand> targetType, MessageTypeStore store) {
		super(targetType);
		this.store = store;
	}

	@Override
	public AppTelecommand convert(JsonNode root, DeserializationContext context) throws IOException {
		if (!root.isObject()) return null;
		var identifierNode = root.get(AppTelecommand.IDENTIFIER_PROPERTY);
		if (!identifierNode.isTextual()) return null;

		return context.readTreeAsValue(root, store.getAppTelecommandType(identifierNode.asText()));
	}
}
