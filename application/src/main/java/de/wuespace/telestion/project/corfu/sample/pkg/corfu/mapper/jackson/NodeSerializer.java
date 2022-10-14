package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuNode;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeStore;

import java.io.IOException;

public class NodeSerializer extends StdSerializer<CorfuNode> {

	private final MessageTypeStore store;

	public NodeSerializer(MessageTypeStore store) {
		this(null, store);
	}

	public NodeSerializer(Class<CorfuNode> targetType, MessageTypeStore store) {
		super(targetType);
		this.store = store;
	}

	@Override
	public void serialize(CorfuNode node, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField(CorfuNode.IDENTIFIER_PROPERTY, node.name());
		gen.writeStringField(CorfuNode.HARDWARE_PROPERTY, node.hardware().toString());
		gen.writeEndObject();
	}
}
