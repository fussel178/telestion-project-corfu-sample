package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser.type;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer;

import java.io.IOException;

public class MessageTypeDeserializer extends StdNodeBasedDeserializer<MessageType> {

	public MessageTypeDeserializer() {
		this(null);
	}

	public MessageTypeDeserializer(Class<MessageType> targetType) {
		super(targetType);
	}

	@Override
	public MessageType convert(JsonNode root, DeserializationContext ctxt) throws IOException {
		if (root.isTextual()) {
			return new MessageType(MessageType.Type.SIMPLE, root.asText(), 1);
		}

		if (root.isObject()) {
			// array test
			if (root.has("array")) {
				var property = root.get("array");
				if (!property.isObject()) {
					return null;
				}

				return new MessageType(MessageType.Type.ARRAY,
						property.get("type").asText(),
						property.get("length").asInt()
				);
			}

			// bit array test
			if (root.has("bit_array")) {
				var property = root.get("bit_array");
				if (!property.isObject()) {
					return null;
				}

				return new MessageType(MessageType.Type.BIT_ARRAY,
						null,
						property.get("length").asInt()
				);
			}
		}

		// don't know what's the type, sorry
		return null;
	}
}
