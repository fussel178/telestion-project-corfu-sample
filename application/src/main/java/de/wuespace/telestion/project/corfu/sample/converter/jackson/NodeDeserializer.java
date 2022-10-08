package de.wuespace.telestion.project.corfu.sample.converter.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer;
import de.wuespace.telestion.project.corfu.sample.converter.message.Node;
import de.wuespace.telestion.project.corfu.sample.converter.store.MessageTypeStore;

import java.io.IOException;

public class NodeDeserializer extends StdNodeBasedDeserializer<Node> {

	private final MessageTypeStore store;

	public NodeDeserializer(MessageTypeStore store) {
		this(null, store);
	}

	public NodeDeserializer(Class<Node> targetType, MessageTypeStore store) {
		super(targetType);
		this.store = store;
	}

	@Override
	public Node convert(JsonNode root, DeserializationContext context) throws IOException {
		if (!root.isObject()) return null;
		var identifierNode = root.get(Node.IDENTIFIER_PROPERTY);
		if (!identifierNode.isTextual()) return null;

		return context.readTreeAsValue(root, store.getNodeType(identifierNode.asText()));
	}
}
