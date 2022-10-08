package de.wuespace.telestion.project.corfu.sample.converter.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelemetry;
import de.wuespace.telestion.project.corfu.sample.converter.store.MessageTypeStore;

import java.io.IOException;

public class AppTelemetrySerializer extends StdSerializer<AppTelemetry> {

	private final MessageTypeStore store;

	public AppTelemetrySerializer(MessageTypeStore store) {
		this(null, store);
	}

	public AppTelemetrySerializer(Class<AppTelemetry> targetType, MessageTypeStore store) {
		super(targetType);
		this.store = store;
	}

	@Override
	public void serialize(AppTelemetry app, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField(AppTelemetry.IDENTIFIER_PROPERTY, app.name());
		gen.writeObjectField(AppTelemetry.PAYLOAD_PROPERTY, app.payload());
		gen.writeEndObject();
	}
}
