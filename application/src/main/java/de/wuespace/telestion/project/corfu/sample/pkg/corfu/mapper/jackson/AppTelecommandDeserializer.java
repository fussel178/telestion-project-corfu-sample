package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuAppTelecommand;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeStore;

import java.io.IOException;

public class AppTelecommandDeserializer extends StdNodeBasedDeserializer<CorfuAppTelecommand> {

	private final MessageTypeStore store;

	public AppTelecommandDeserializer(MessageTypeStore store) {
		this(null, store);
	}

	public AppTelecommandDeserializer(Class<CorfuAppTelecommand> targetType, MessageTypeStore store) {
		super(targetType);
		this.store = store;
	}

	@Override
	public CorfuAppTelecommand convert(JsonNode root, DeserializationContext context) throws IOException {
		if (!root.isObject()) return null;
		var identifierNode = root.get(CorfuAppTelecommand.IDENTIFIER_PROPERTY);
		if (!identifierNode.isTextual()) return null;

		return context.readTreeAsValue(root, store.getAppTelecommandType(identifierNode.asText()));
	}
}
