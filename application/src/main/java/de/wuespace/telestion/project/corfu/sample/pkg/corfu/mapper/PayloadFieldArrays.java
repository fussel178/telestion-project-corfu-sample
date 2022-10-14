package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuDeserializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuSerializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.AppTelecommandPayload;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.PrimitiveInputStream;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.PrimitiveOutputStream;

import java.lang.reflect.RecordComponent;
import java.util.Arrays;

public class PayloadFieldArrays {
	public static PayloadField[] from(RecordComponent[] components, CorfuPayloadMapper serializer) {
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

	public static void serialize(PayloadField[] fields, AppTelecommandPayload payload, PrimitiveOutputStream stream)
			throws CorfuSerializationException {

		for (var field : fields) {
			field.serialize(stream, payload);
		}
	}

	public static Object[] deserialize(PayloadField[] fields, PrimitiveInputStream stream)
			throws CorfuDeserializationException {
		Object[] objects = new Object[fields.length];

		for (int i = 0; i < objects.length; i++) {
			objects[i] = fields[i].deserialize(stream);
		}

		return objects;
	}

	// Suppresses default constructor, ensuring non-instantiability.
	private PayloadFieldArrays() {}
}
