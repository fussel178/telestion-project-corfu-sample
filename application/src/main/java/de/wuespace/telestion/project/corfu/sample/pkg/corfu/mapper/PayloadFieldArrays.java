package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuDeserializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuSerializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.AppTelecommandPayload;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.PrimitiveInputStream;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.PrimitiveOutputStream;

import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Utility class to handle arrays of {@link PayloadField PayloadFields} in a better way.
 *
 * @author Ludwig Richter (@fussel178)
 */
public class PayloadFieldArrays {
	/**
	 * Transforms an array of record components to payload fields.
	 *
	 * @param components the array of record components
	 * @param serializer the serializer that the payload fields should receive during creation
	 * @return an array of payload fields that wrap the given record components
	 */
	public static PayloadField[] from(RecordComponent[] components, CorfuPayloadMapper serializer) {
		return Arrays.stream(components)
				.map(component -> new PayloadField(component, serializer))
				.toArray(PayloadField[]::new);
	}

	/**
	 * Returns an array of class types representing the types of the payloads fields.
	 *
	 * @param fields the payload fields that specify the class types
	 * @return an array of class types
	 */
	public static Class<?>[] getTypes(PayloadField[] fields) {
		return Arrays.stream(fields).map(PayloadField::componentType).toArray(Class[]::new);
	}

	/**
	 * Returns the size of an array of payload fields by summing up all individual sizes of every payload field.
	 *
	 * @param fields the payload fields to measure
	 * @return the size of all payload fields
	 */
	public static int size(PayloadField[] fields) {
		return Arrays.stream(fields).map(PayloadField::size).reduce(0, Integer::sum);
	}

	/**
	 * Serializes an array of payload fields to its binary formats.
	 *
	 * @param fields  the payload fields to serialize
	 * @param stream  the stream that accepts the encoded binary data
	 * @param payload the payload object that contains the actual values that should be encoded
	 * @throws CorfuSerializationException if errors happen during serialization
	 */
	public static void serialize(PayloadField[] fields, PrimitiveOutputStream stream, AppTelecommandPayload payload)
			throws CorfuSerializationException {

		for (var field : fields) {
			field.serialize(stream, payload);
		}
	}

	/**
	 * Deserializes an array of payload fields from its binary formats to an array of Java objects.
	 *
	 * @param fields the payload fields to deserialize
	 * @param stream the stream that provides the encoded binary format
	 * @return an array of Java objects representing the deserialized values in the order of payload fields
	 * @throws CorfuDeserializationException if errors happen during deserialization
	 */
	public static Object[] deserialize(PayloadField[] fields, PrimitiveInputStream stream)
			throws CorfuDeserializationException {
		Object[] objects = new Object[fields.length];

		for (int i = 0; i < objects.length; i++) {
			objects[i] = fields[i].deserialize(stream);
		}

		return objects;
	}

	/**
	 * Stringifies the class type binary names of all payload fields.
	 *
	 * @param fields the payload fields that specify the class types
	 * @return a string that contains the binary name of all class types
	 */
	public static String toTypeString(PayloadField[] fields) {
		return "[" + Arrays.stream(fields)
				.map(field -> field.componentType().getName())
				.collect(Collectors.joining(", ")) + "]";
	}

	// Suppresses default constructor, ensuring non-instantiability.
	private PayloadFieldArrays() {
	}
}
