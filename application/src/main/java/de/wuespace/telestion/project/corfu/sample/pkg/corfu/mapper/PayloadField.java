package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuDeserializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuSerializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuProperty;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.AppTelecommandPayload;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.AppTelemetryPayload;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.PrimitiveInputStream;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.PrimitiveOutputStream;
import de.wuespace.telestion.project.corfu.sample.pkg.util.ReflectionUtils;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuPayload;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.InputStreamEmptyException;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.OutputStreamFullException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;

public class PayloadField {

	private final RecordComponent component;
	private final CorfuPayloadMapper payloadMapper;
	private final CorfuProperty parameters;

	public PayloadField(RecordComponent component, CorfuPayloadMapper payloadMapper) {
		this.component = component;
		this.payloadMapper = payloadMapper;
		this.parameters = component.getAnnotation(CorfuProperty.class);

		validate();
	}

	public RecordComponent component() {
		return component;
	}

	public CorfuPayloadMapper payloadSerializer() {
		return payloadMapper;
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
		return CorfuPayload.class.isAssignableFrom(component.getType());
	}

	public boolean isAnnotated() {
		return Objects.nonNull(parameters);
	}

	public void validate() {
		if (!isAnnotated()) return;

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
		if (!isAnnotated()) return 0;

		if (parameters.value() != CorfuProperty.Type.OBJECT) {
			return parameters.value().size(parameters.count());
		}

		Class<?> elementType = isList() ? ReflectionUtils.getListElementType(component) : component.getType();
		try {
			return payloadMapper.size(elementType.asSubclass(CorfuPayload.class)) * parameters.count();
		} catch (ClassCastException e) {
			// TODO: Better exception
			throw new RuntimeException(e);
		}
	}

	public void serialize(PrimitiveOutputStream stream, AppTelecommandPayload payload) throws CorfuSerializationException {
		if (!isAnnotated()) return;
		try {
			var value = component.getAccessor().invoke(payload);

			switch (parameters.value()) {
				case BOOLEAN -> stream.writeBoolean((boolean) value);
				case FLOAT -> stream.writeFloat((float) value);
				case DOUBLE -> stream.writeDouble((double) value);
				case INT8 -> stream.writeSignedByte((byte) value);
				case INT16 -> stream.writeSignedShort((short) value);
				case INT32 -> stream.writeSignedInteger((int) value);
				case INT64 -> stream.writeSignedLong((long) value);
				case UINT8 -> stream.writeUnsignedByte((short) value);
				case UINT16 -> stream.writeUnsignedShort((int) value);
				case UINT32 -> stream.writeUnsignedInteger((long) value);
				case UINT64 -> stream.writeUnsignedLong((BigInteger) value);
				case BITARRAY -> stream.writeBitSet((BitSet) value, parameters.count());
				case OBJECT -> serializeAsObject(stream, value);
			}
		} catch (IllegalAccessException e) {
			throw new CorfuSerializationException(("Cannot access value of record component %s in payload %s " +
					"because it is prohibited by the security manager. Please allow access to package and private " +
					"properties and try again. Current instance: %s")
					.formatted(component.getName(), component.getDeclaringRecord().getName(), payload), e);
		} catch (InvocationTargetException e) {
			throw new CorfuSerializationException(("Errors occurred during access of record component %s in " +
					"payload %s. Please fix the thrown exception and try again.")
					.formatted(component.getName(), component.getDeclaringRecord().getName()), e);
		} catch (OutputStreamFullException e) {
			throw new CorfuSerializationException("The output stream is full. Is the payload too large?", e);
		}
	}

	public Object deserialize(PrimitiveInputStream stream) throws CorfuDeserializationException {
		// fill up non-annotated components with default values or "null"
		if (!isAnnotated()) {
			return DefaultValues.getDefaultValue(componentType());
		}

		try {
			return switch (parameters.value()) {
				case BOOLEAN -> stream.readBoolean();
				case FLOAT -> stream.readFloat();
				case DOUBLE -> stream.readDouble();
				case INT8 -> stream.readSignedByte();
				case INT16 -> stream.readSignedShort();
				case INT32 -> stream.readSignedInteger();
				case INT64 -> stream.readSignedLong();
				case UINT8 -> stream.readUnsignedByte();
				case UINT16 -> stream.readUnsignedShort();
				case UINT32 -> stream.readUnsignedInteger();
				case UINT64 -> stream.readUnsignedLong();
				case BITARRAY -> stream.readBitSet(parameters.count());
				case OBJECT -> deserializeAsObject(stream);
			};
		} catch (InputStreamEmptyException e) {
			throw new CorfuDeserializationException(
					"The input stream is empty. Did you choose the wrong payload type?",
					e
			);
		}
	}

	private void serializeAsObject(PrimitiveOutputStream stream, Object value) throws CorfuSerializationException {
		if (isList()) {
			var list = (List<?>) value;
			for (var element : list) {
				payloadMapper.serialize(stream, (AppTelecommandPayload) element);
			}
		} else {
			payloadMapper.serialize(stream, (AppTelecommandPayload) value);
		}
	}

	private Object deserializeAsObject(PrimitiveInputStream stream) throws CorfuDeserializationException {
		if (isList()) {
			var listType = ReflectionUtils.getListElementType(component);
			var list = new ArrayList<CorfuPayload>();
			for (int i = 0; i < parameters.count(); i++) {
				list.add(i, payloadMapper.deserialize(stream, listType.asSubclass(AppTelemetryPayload.class)));
			}
			return list;
		} else {
			return payloadMapper.deserialize(stream, component.getType().asSubclass(AppTelemetryPayload.class));
		}
	}
}
