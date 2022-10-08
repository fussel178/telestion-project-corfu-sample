package de.wuespace.telestion.project.corfu.sample.converter.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelecommand;
import de.wuespace.telestion.project.corfu.sample.converter.store.MessageTypeStore;

import java.io.IOException;

public class AppTelecommandSerializer extends StdSerializer<AppTelecommand> {

	private final MessageTypeStore store;

	public AppTelecommandSerializer(MessageTypeStore store) {
		this(null, store);
	}

	public AppTelecommandSerializer(Class<AppTelecommand> targetType, MessageTypeStore store) {
		super(targetType);
		this.store = store;
	}

	@Override
	public void serialize(AppTelecommand app, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField(AppTelecommand.IDENTIFIER_PROPERTY, app.name());
		gen.writeObjectField(AppTelecommand.PAYLOAD_PROPERTY, app.payload());
		gen.writeEndObject();
	}
}
