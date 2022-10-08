package de.wuespace.telestion.project.corfu.sample.converter.serializer;

import de.wuespace.telestion.project.corfu.sample.converter.message.CorfuProperty;
import de.wuespace.telestion.project.corfu.sample.converter.exception.DeserializationException;
import de.wuespace.telestion.project.corfu.sample.converter.exception.SerializationException;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelecommandPayload;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelemetryPayload;
import de.wuespace.telestion.project.corfu.sample.converter.store.MessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.converter.util.DataInputStream;
import de.wuespace.telestion.project.corfu.sample.converter.util.DataOutputStream;
import de.wuespace.telestion.project.corfu.sample.converter.message.Payload;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class PayloadSerializer {
	private final Map<Class<? extends Payload>, PayloadField[]> cachedFields;
	private final Map<Class<? extends Payload>, Constructor<? extends Payload>> cachedConstructors;
	private final MessageTypeStore store;

	public PayloadSerializer(MessageTypeStore store) {
		this.store = store;
		this.cachedFields = new HashMap<>();
		this.cachedConstructors = new HashMap<>();
	}

	public PayloadField[] getPayloadFields(Class<? extends Payload> type) {
		// return cached result
		if (cachedFields.containsKey(type)) {
			return cachedFields.get(type);
		}

		if (!type.isRecord()) {
			throw new IllegalArgumentException(("The payload %s is not a record. Currently, the serializer only " +
					"supports records as payloads. Please convert the payload %s to a record, annotate the suitable " +
					"fields with the %s annotation and try again.")
					.formatted(type.getName(), type.getName(), CorfuProperty.class.getName()));
		}

		var fields = PayloadFieldArrays.from(type.getRecordComponents(), this);
		cachedFields.put(type, fields);
		return fields;
	}

	public <T extends Payload> Constructor<T> getConstructor(Class<T> type) {
		// return cached result
		if (cachedConstructors.containsKey(type)) {
			// This cast is safe because we only put the right type of constructor in the map later in this method.
			//noinspection unchecked
			return (Constructor<T>) cachedConstructors.get(type);
		}

		var fields = getPayloadFields(type);
		try {
			var constructor = type.getConstructor(PayloadFieldArrays.getTypes(fields));
			cachedConstructors.put(type, constructor);
			return constructor;
		} catch (NoSuchMethodException e) {
			// TODO: Write better exception message
			throw new RuntimeException(e);
		}
	}

	public int size(Class<? extends Payload> type) {
		return PayloadFieldArrays.size(getPayloadFields(type));
	}

	public void serialize(DataOutputStream stream, AppTelecommandPayload payload) throws SerializationException {
		var fields = getPayloadFields(payload.getClass());
		PayloadFieldArrays.serialize(fields, payload, stream);
	}

	public Payload deserialize(DataInputStream stream, short appId, short payloadId) throws DeserializationException {
		return deserialize(stream, store.getTelemetryPayloadType(appId, payloadId));
	}

	public Payload deserialize(DataInputStream stream, Class<? extends AppTelemetryPayload> payloadType)
			throws DeserializationException {
		var fields = getPayloadFields(payloadType);
		var initArgs = PayloadFieldArrays.deserialize(fields, stream);

		try {
			return getConstructor(payloadType).newInstance(initArgs);
		} catch (InvocationTargetException e) {
			// TODO: Write better exception message
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			// TODO: Write better exception message
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO: Write better exception message
			throw new RuntimeException(e);
		}
	}
}
