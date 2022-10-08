package de.wuespace.telestion.project.corfu.sample.converter.serializer;

import de.wuespace.telestion.project.corfu.sample.converter.exception.DeserializationException;
import de.wuespace.telestion.project.corfu.sample.converter.exception.SerializationException;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelecommandPayload;
import de.wuespace.telestion.project.corfu.sample.converter.util.DataInputStream;
import de.wuespace.telestion.project.corfu.sample.converter.util.DataOutputStream;

import java.lang.reflect.RecordComponent;
import java.util.Arrays;

public class PayloadFieldArrays {
	public static PayloadField[] from(RecordComponent[] components, PayloadSerializer serializer) {
		return Arrays.stream(components)
				.map(component -> new PayloadField(component, serializer))
				.toArray(PayloadField[]::new);
	}

	public static Class<?>[] getTypes(PayloadField[] fields) {
		return Arrays.stream(fields).map(PayloadField::componentType).toArray(Class[]::new);
	}

	public static int size(PayloadField[] fields) {
		return Arrays.stream(fields).map(PayloadField::size).reduce(0, Integer::sum);
	}

	public static void serialize(PayloadField[] fields, AppTelecommandPayload payload, DataOutputStream stream) throws SerializationException {
		for (var field : fields) {
			field.serialize(stream, payload);
		}
	}

	public static Object[] deserialize(PayloadField[] fields, DataInputStream stream) throws DeserializationException {
		Object[] objects = new Object[fields.length];

		for (int i = 0; i < objects.length; i++) {
			objects[i] = fields[i].deserialize(stream);
		}

		return objects;
	}

	// Suppresses default constructor, ensuring non-instantiability.
	private PayloadFieldArrays() {}
}
