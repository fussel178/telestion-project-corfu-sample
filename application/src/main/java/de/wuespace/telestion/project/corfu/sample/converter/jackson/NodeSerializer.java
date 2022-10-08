package de.wuespace.telestion.project.corfu.sample.converter.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.wuespace.telestion.project.corfu.sample.converter.message.Node;
import de.wuespace.telestion.project.corfu.sample.converter.store.MessageTypeStore;

import java.io.IOException;

public class NodeSerializer extends StdSerializer<Node> {

	private final MessageTypeStore store;

	public NodeSerializer(MessageTypeStore store) {
		this(null, store);
	}

	public NodeSerializer(Class<Node> targetType, MessageTypeStore store) {
		super(targetType);
		this.store = store;
	}

	@Override
	public void serialize(Node node, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField(Node.IDENTIFIER_PROPERTY, node.name());
		gen.writeStringField(Node.HARDWARE_PROPERTY, node.hardware().toString());
		gen.writeEndObject();
	}
}
