package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuDeserializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuSerializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.*;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.PrimitiveInputStream;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.PrimitiveOutputStream;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements a Corfu payload transformer that can map
 * {@link AppTelemetryPayload} and {@link AppTelecommandPayload} from their binary format into a
 * {@link de.wuespace.telestion.api.message.JsonMessage JsonMessage} format and back.
 *
 * @author Ludwig Richter (@fussel178)
 */
public class CorfuPayloadMapper {
	private final Map<Class<? extends CorfuStruct>, PayloadField[]> cachedFields;
	private final Map<Class<? extends CorfuStruct>, Constructor<? extends CorfuStruct>> cachedConstructors;
	private final MessageTypeStore store;

	public CorfuPayloadMapper(MessageTypeStore store) {
		this.store = store;
		this.cachedFields = new HashMap<>();
		this.cachedConstructors = new HashMap<>();
	}

	/**
	 * Returns all fields of a {@link CorfuPayload} wrapped in {@link PayloadField} for better handling.
	 *
	 * @param type the class type of the {@link CorfuPayload}
	 * @return fields of the {@link CorfuPayload}
	 */
	public PayloadField[] getPayloadFields(Class<? extends CorfuStruct> type) {
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

	/**
	 * Returns the constructor for a {@link CorfuPayload} that accepts parameters to fill all record fields.
	 * (aka the invisible predefined constructor of a {@link Record})
	 *
	 * @param type the class type of the {@link CorfuPayload}
	 * @return the constructor that can create new objects of type {@link CorfuPayload}
	 * @throws NoSuchMethodException gets thrown if no constructor can be found
	 *                               that accepts all payload fields as parameters
	 */
	public <T extends CorfuStruct> Constructor<T> getConstructor(Class<T> type) throws NoSuchMethodException {
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

	/**
	 * Returns the size of the {@link CorfuPayload}.
	 *
	 * @param type the class type of the {@link CorfuPayload}
	 * @return the sum of all payload fields inside the {@link CorfuPayload}
	 */
	public int size(Class<? extends CorfuPayload> type) {
		return PayloadFieldArrays.size(getPayloadFields(type));
	}

	/**
	 * Serializes a {@link CorfuPayload} to its binary format.
	 *
	 * @param stream  the stream that accepts the encoded binary data
	 * @param payload the payload object that contains values for all payload fields
	 * @throws CorfuSerializationException if errors happen during serialization
	 */
	public void serialize(PrimitiveOutputStream stream, AppTelecommandPayload payload)
			throws CorfuSerializationException {

		var fields = getPayloadFields(payload.getClass());
		PayloadFieldArrays.serialize(fields, stream, payload);
	}

	/**
	 * Deserializes a {@link CorfuPayload} from its binary format to an actual Java object.
	 *
	 * @param stream    the stream that provides the encoded binary format
	 * @param appId     the id of the telemetry app extracted from the corfu message header as unsigned byte
	 * @param payloadId the id of the payload extracted from the corfu message header as unsigned byte
	 * @param nodeId    the id of the sending node extracted from the corfu message header as unsigned byte
	 * @return an instantiated Java object that holds the decoded values
	 * @throws CorfuDeserializationException if errors happen during deserialization
	 */
	public CorfuPayload deserialize(PrimitiveInputStream stream, short appId, short payloadId, short nodeId)
			throws CorfuDeserializationException {

		return (CorfuPayload) deserialize(stream, store.getTelemetryPayloadType(appId, payloadId, nodeId));
	}

	/**
	 * Deserializes a {@link CorfuPayload} from its binary format to an actual Java object.
	 *
	 * @param stream      the stream that provides the encoded binary format
	 * @param payloadType the class type of the {@link CorfuPayload}
	 * @return an instantiated Java object that holds the decoded values
	 * @throws CorfuDeserializationException if errors happen during deserialization
	 */
	public CorfuStruct deserialize(PrimitiveInputStream stream, Class<? extends CorfuStruct> payloadType)
			throws CorfuDeserializationException {

		var fields = getPayloadFields(payloadType);
		var initArgs = PayloadFieldArrays.deserialize(fields, stream);

		try {
			return getConstructor(payloadType).newInstance(initArgs);
		} catch (InvocationTargetException e) {
			throw new CorfuDeserializationException("During instantiation the constructor threw an exception. " +
					"Please fix the following error and try again:", e);
		} catch (InstantiationException e) {
			throw new CorfuDeserializationException(("The payload %s is abstract. This is not supported. Please " +
					"convert the payload to a real class or convert the payload to a record and try again.")
					.formatted(payloadType.getName()));
		} catch (IllegalAccessException e) {
			throw new CorfuDeserializationException(("The constructor of payload %s is not accessible. Please make " +
					"the constructor public and accessible or convert the payload to a record and try again.")
					.formatted(payloadType.getName()));
		} catch (NoSuchMethodException e) {
			throw new CorfuDeserializationException(("The payload %s does not provide a constructor that accepts " +
					"all payload fields as constructor parameters. Please provide this type of constructor or " +
					"convert the payload to a record and try again. Needed constructor parameters: %s")
					.formatted(payloadType.getName(), PayloadFieldArrays.toTypeString(fields)));
		}
	}
}
