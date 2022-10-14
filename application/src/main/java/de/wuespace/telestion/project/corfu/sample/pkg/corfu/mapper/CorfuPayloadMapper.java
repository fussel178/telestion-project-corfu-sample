package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuDeserializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuSerializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuProperty;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.AppTelecommandPayload;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.AppTelemetryPayload;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.PrimitiveInputStream;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.PrimitiveOutputStream;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuPayload;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CorfuPayloadMapper {
	private final Map<Class<? extends CorfuPayload>, PayloadField[]> cachedFields;
	private final Map<Class<? extends CorfuPayload>, Constructor<? extends CorfuPayload>> cachedConstructors;
	private final MessageTypeStore store;

	public CorfuPayloadMapper(MessageTypeStore store) {
		this.store = store;
		this.cachedFields = new HashMap<>();
		this.cachedConstructors = new HashMap<>();
	}

	public PayloadField[] getPayloadFields(Class<? extends CorfuPayload> type) {
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

	public <T extends CorfuPayload> Constructor<T> getConstructor(Class<T> type) throws NoSuchMethodException {
		// return cached result
		if (cachedConstructors.containsKey(type)) {
			// This cast is safe because we only put the right type of constructor in the map later in this method.
			//noinspection unchecked
			return (Constructor<T>) cachedConstructors.get(type);
		}

		var fields = getPayloadFields(type);
		var constructor = type.getConstructor(PayloadFieldArrays.getTypes(fields));
		cachedConstructors.put(type, constructor);
		return constructor;
	}

	public int size(Class<? extends CorfuPayload> type) {
		return PayloadFieldArrays.size(getPayloadFields(type));
	}

	public void serialize(PrimitiveOutputStream stream, AppTelecommandPayload payload)
			throws CorfuSerializationException {

		var fields = getPayloadFields(payload.getClass());
		PayloadFieldArrays.serialize(fields, payload, stream);
	}

	public CorfuPayload deserialize(PrimitiveInputStream stream, short appId, short payloadId)
			throws CorfuDeserializationException {

		return deserialize(stream, store.getTelemetryPayloadType(appId, payloadId));
	}

	public CorfuPayload deserialize(PrimitiveInputStream stream, Class<? extends AppTelemetryPayload> payloadType)
			throws CorfuDeserializationException {

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
		} catch (NoSuchMethodException e) {
			// TODO: Write better exception message
			throw new RuntimeException(e);
		}
	}
}
