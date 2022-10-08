package de.wuespace.telestion.project.corfu.sample.converter.serializer;

import de.wuespace.telestion.project.corfu.sample.converter.message.CorfuProperty;
import de.wuespace.telestion.project.corfu.sample.converter.exception.DeserializationException;
import de.wuespace.telestion.project.corfu.sample.converter.exception.SerializationException;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelecommandPayload;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelemetryPayload;
import de.wuespace.telestion.project.corfu.sample.converter.util.DataInputStream;
import de.wuespace.telestion.project.corfu.sample.converter.util.DataOutputStream;
import de.wuespace.telestion.project.corfu.sample.converter.util.DefaultValues;
import de.wuespace.telestion.project.corfu.sample.converter.util.ReflectionUtils;
import de.wuespace.telestion.project.corfu.sample.converter.message.Payload;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;

public class PayloadField {

	private final RecordComponent component;
	private final PayloadSerializer payloadSerializer;
	private final CorfuProperty parameters;

	public PayloadField(RecordComponent component, PayloadSerializer payloadSerializer) {
		this.component = component;
		this.payloadSerializer = payloadSerializer;
		this.parameters = component.getAnnotation(CorfuProperty.class);

		validate();
	}

	public RecordComponent component() {
		return component;
	}

	public PayloadSerializer payloadSerializer() {
		return payloadSerializer;
	}

	public CorfuProperty parameters() {
		return parameters;
	}

	public Class<?> componentType() {
		return component.getType();
	}

	public boolean isList() {
		return List.class.isAssignableFrom(component.getType());
	}

	public boolean isPayload() {
		return Payload.class.isAssignableFrom(component.getType());
	}

	public boolean hasAnnotation() {
		return Objects.nonNull(parameters);
	}

	public void validate() {
		if (!hasAnnotation()) return;

		var type = parameters.value();

		if (!type.hasSuitableType(component.getType())) {
			throw new IllegalArgumentException(("The field %s in payload %s has an incompatible type. " +
					"The specified corfu type %s only allows a type %s or a specialization of it. " +
					"Please update the field type and try again").formatted(component.getName(),
					Object.class.getSimpleName(), type, type.suitableType().getSimpleName()));
		}

		if (!type.hasValidCount(this.parameters.count())) {
			throw new IllegalArgumentException(("The corfu type %s on field %s in payload %s has an invalid count. " +
					"Only a maximum of %d elements are allowed for this corfu type. " +
					"Please update the count parameter in the corfu type annotation and try again")
					.formatted(type, component.getName(), Object.class.getSimpleName(), type.maxCount()));
		}
	}

	public int size() {
		if (!hasAnnotation()) return 0;

		if (parameters.value() != CorfuProperty.Type.OBJECT) {
			return parameters.value().size(parameters.count());
		}

		Class<?> elementType = isList() ? ReflectionUtils.getListElementType(component) : component.getType();
		try {
			return payloadSerializer.size(elementType.asSubclass(Payload.class)) * parameters.count();
		} catch (ClassCastException e) {
			// TODO: Better exception
			throw new RuntimeException(e);
		}
	}

	public void serialize(DataOutputStream stream, AppTelecommandPayload payload) throws SerializationException {
		if (!hasAnnotation()) return;
		try {
			var value = component.getAccessor().invoke(payload);

			switch (parameters.value()) {
				case BOOLEAN -> stream.writeBoolean((boolean) value);
				case FLOAT -> stream.writeFloat((float) value);
				case DOUBLE -> stream.writeDouble((double) value);
				case INT8 -> stream.writeSignedByte((byte) value);
				case INT16 -> stream.writeSignedShort((short) value);
				case INT32 -> stream.writeSignedInt((int) value);
				case INT64 -> stream.writeSignedLong((long) value);
				case UINT8 -> stream.writeUnsignedByte((short) value);
				case UINT16 -> stream.writeUnsignedShort((int) value);
				case UINT32 -> stream.writeUnsignedInt((long) value);
				case UINT64 -> stream.writeUnsignedLong((BigInteger) value);
				case BITARRAY -> stream.writeBitSet((BitSet) value, parameters.count());
				case OBJECT -> serializeAsObject(stream, value);
			}
		} catch (IllegalAccessException e) {
			throw new SerializationException(("Cannot access value of record component %s in payload %s " +
					"because it is prohibited by the security manager. " +
					"Please allow access to package and private properties and try again. " +
					"Current instance: %s").formatted(component.getName(), component.getDeclaringRecord().getName(), payload));
		} catch (InvocationTargetException e) {
			// TODO: Better exception
			throw new RuntimeException(e);
		}
	}

	public Object deserialize(DataInputStream stream) throws DeserializationException {
		// fill up non-annotated components with default values or "null"
		if (!hasAnnotation()) {
			return DefaultValues.getDefaultValue(componentType());
		}

		return switch (parameters.value()) {
			case BOOLEAN -> stream.readBoolean();
			case FLOAT -> stream.readFloat();
			case DOUBLE -> stream.readDouble();
			case INT8 -> stream.readSignedByte();
			case INT16 -> stream.readSignedShort();
			case INT32 -> stream.readSignedInt();
			case INT64 -> stream.readSignedLong();
			case UINT8 -> stream.readUnsignedByte();
			case UINT16 -> stream.readUnsignedShort();
			case UINT32 -> stream.readUnsignedInt();
			case UINT64 -> stream.readUnsignedLong();
			case BITARRAY -> stream.readBitSet(parameters.count());
			case OBJECT -> deserializeAsObject(stream);
		};
	}

	private void serializeAsObject(DataOutputStream stream, Object value)
			throws IllegalAccessException, SerializationException {
		// TODO: Better check if component type is payload (maybe only once?)
		if (isList()) {
			var list = (List<?>) value;
			for (var element : list) {
				payloadSerializer.serialize(stream, (AppTelecommandPayload) element);
			}
		} else {
			payloadSerializer.serialize(stream, (AppTelecommandPayload) value);
		}
	}

	private Object deserializeAsObject(DataInputStream stream) throws DeserializationException {
		if (isList()) {
			var listType = ReflectionUtils.getListElementType(component);
			var list = new ArrayList<Payload>();
			for (int i = 0; i < parameters.count(); i++) {
				list.add(i, payloadSerializer.deserialize(stream, listType.asSubclass(AppTelemetryPayload.class)));
			}
			return list;
		} else {
			return payloadSerializer.deserialize(stream, component.getType().asSubclass(AppTelemetryPayload.class));
		}
	}
}
