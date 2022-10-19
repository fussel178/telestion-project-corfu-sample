package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuDeserializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuSerializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.*;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.InputStreamFunction;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.PrimitiveInputStream;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.PrimitiveOutputStream;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.OutputStreamFunction;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.InputStreamEmptyException;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.OutputStreamFullException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.Objects;

/**
 * Represents a field in a {@link CorfuPayload}.
 * On creation, it extracts the annotated information on the payload field.
 * It serializes and deserializes the associated field on a Corfu message
 * from their binary format into a {@link de.wuespace.telestion.api.message.JsonMessage JsonMessage} format and back.
 *
 * @author Ludwig Richter (@fussel178)
 */
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

	/**
	 * Returns the associated record component that represents the field in a {@link CorfuPayload}.
	 */
	public RecordComponent component() {
		return component;
	}

	/**
	 * Returns the payload mapper that are used to serialize and deserialize nested {@link CorfuPayload CorfuPayloads}.
	 */
	public CorfuPayloadMapper payloadSerializer() {
		return payloadMapper;
	}

	/**
	 * Returns the extracted parameters from the {@link CorfuProperty} annotation if the payload field is annotated.
	 */
	public CorfuProperty parameters() {
		return parameters;
	}

	/**
	 * Returns the actual java type of the payload field.
	 */
	public Class<?> componentType() {
		return component.getType();
	}

	/**
	 * Returns <code>true</code>, if the payload field is an array.
	 */
	public boolean isArray() {
		return componentType().isArray();
	}

	/**
	 * Returns <code>true</code>, if the payload field is a nested {@link CorfuPayload}.
	 */
	public boolean isPayload() {
		return CorfuPayload.class.isAssignableFrom(component.getType());
	}

	/**
	 * Returns <code>true</code>, if the payload field is annotated with the {@link CorfuProperty} annotation
	 * and can provide additional information via {@link #parameters()}.
	 */
	public boolean isAnnotated() {
		return Objects.nonNull(parameters);
	}

	/**
	 * Validates the payload field. It checks, if the java type is suitable for the selected Corfu type
	 * and has a valid count.
	 */
	public void validate() {
		if (!isAnnotated()) return;

		var type = parameters.value();

		// suitable type check
		if (!type.hasSuitableType(component.getType())) {
			throw new IllegalArgumentException(("The field %s in payload %s has an incompatible type. " +
					"The specified corfu type %s only allows a type %s or a specialization of it. " +
					"Please update the field type and try again").formatted(component.getName(),
					Object.class.getSimpleName(), type, type.suitableType().getSimpleName()));
		}

		// array-count check
		if (!isArray() && parameters.count() > 1) {
			throw new IllegalArgumentException(("The annotation on field %s specifies more than one count of this " +
					"type but the field type %s is not an array. Please convert the type to an array or remove the " +
					"count information from the annotation and try again.")
					.formatted(component.getName(), componentType().getName()));
		}

		// valid count check
		if (!type.hasValidCount(this.parameters.count())) {
			throw new IllegalArgumentException(("The corfu type %s on field %s in payload %s has an invalid count. " +
					"Only a maximum of %d elements are allowed for this corfu type. " +
					"Please update the count parameter in the corfu type annotation and try again")
					.formatted(type, component.getName(), Object.class.getSimpleName(), type.maxCount()));
		}
	}

	/**
	 * Returns the size of the payload field in bytes.
	 */
	public int size() {
		if (!isAnnotated()) return 0;

		if (parameters.value() != CorfuProperty.Type.OBJECT) {
			return parameters.value().size(parameters.count());
		}

		try {
			return payloadMapper.size(componentType().asSubclass(CorfuPayload.class)) * parameters.count();
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(("The class type %s on field %s in payload %s is not a " +
					"specialization of a %s. Please remove the %s annotation from the payload field or implement " +
					"the %s interface in the %s class and try again.")
					.formatted(
							componentType().getName(),
							component.getName(),
							component.getDeclaringRecord().getName(),
							CorfuPayload.class.getName(),
							CorfuProperty.class.getName(),
							CorfuPayload.class.getName(),
							componentType().getName()
					)
			);
		}
	}

	/**
	 * Serializes a Corfu payload field to its binary format.
	 *
	 * @param stream  the stream that accepts the encoded binary data
	 * @param payload the payload object that contains the actual value that should be encoded
	 * @throws CorfuSerializationException if errors happen during serialization
	 */
	public void serialize(PrimitiveOutputStream stream, AppTelecommandPayload payload) throws CorfuSerializationException {
		if (!isAnnotated()) return;
		try {
			var value = component.getAccessor().invoke(payload);

			switch (parameters.value()) {
				case BOOLEAN -> serializeAsPrimitive(stream::writeBoolean, value, Boolean.class);
				case FLOAT -> serializeAsPrimitive(stream::writeFloat, value, Float.class);
				case DOUBLE -> serializeAsPrimitive(stream::writeDouble, value, Double.class);
				case INT8 -> serializeAsPrimitive(stream::writeSignedByte, value, Byte.class);
				case INT16 -> serializeAsPrimitive(stream::writeSignedShort, value, Short.class);
				case INT32 -> serializeAsPrimitive(stream::writeSignedInteger, value, Integer.class);
				case INT64 -> serializeAsPrimitive(stream::writeSignedLong, value, Long.class);
				case UINT8 -> serializeAsPrimitive(stream::writeUnsignedByte, value, Short.class);
				case UINT16 -> serializeAsPrimitive(stream::writeUnsignedShort, value, Integer.class);
				case UINT32 -> serializeAsPrimitive(stream::writeUnsignedInteger, value, Long.class);
				case UINT64 -> serializeAsPrimitive(stream::writeUnsignedLong, value, BigInteger.class);
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

	/**
	 * Deserializes a Corfu payload field from its binary format to a Java object.
	 *
	 * @param stream the stream that provides the encoded binary format
	 * @return the instantiated java object that holds the decoded value
	 * @throws CorfuDeserializationException if errors happen during deserialization
	 */
	public Object deserialize(PrimitiveInputStream stream) throws CorfuDeserializationException {
		// fill up non-annotated components with default values or "null"
		if (!isAnnotated()) {
			return DefaultValues.getDefaultValue(componentType());
		}

		try {
			return switch (parameters.value()) {
				case BOOLEAN -> deserializeAsPrimitive(stream::readBoolean);
				case FLOAT -> deserializeAsPrimitive(stream::readFloat);
				case DOUBLE -> deserializeAsPrimitive(stream::readDouble);
				case INT8 -> deserializeAsPrimitive(stream::readSignedByte);
				case INT16 -> deserializeAsPrimitive(stream::readSignedShort);
				case INT32 -> deserializeAsPrimitive(stream::readSignedInteger);
				case INT64 -> deserializeAsPrimitive(stream::readSignedLong);
				case UINT8 -> deserializeAsPrimitive(stream::readUnsignedByte);
				case UINT16 -> deserializeAsPrimitive(stream::readUnsignedShort);
				case UINT32 -> deserializeAsPrimitive(stream::readUnsignedInteger);
				case UINT64 -> deserializeAsPrimitive(stream::readUnsignedLong);
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

	private <T> void serializeAsPrimitive(OutputStreamFunction<T> function, Object value, Class<T> type)
			throws OutputStreamFullException {

		if (isArray()) {
			var casted = (Object[]) type.arrayType().cast(value);
			for (var elem : casted) {
				function.invoke(type.cast(elem));
			}
		} else {
			function.invoke(type.cast(value));
		}
	}

	private void serializeAsObject(PrimitiveOutputStream stream, Object value) throws CorfuSerializationException {
		if (isArray()) {
			var array = (Object[]) value;
			for (var elem : array) {
				payloadMapper.serialize(stream, (AppTelecommandPayload) elem);
			}
		} else {
			payloadMapper.serialize(stream, (AppTelecommandPayload) value);
		}
	}

	private <T> Object deserializeAsPrimitive(InputStreamFunction<T> function)
			throws InputStreamEmptyException {

		if (isArray()) {
			// TODO: Find better way to instantiate primitive type array.
			//  This must be tested and verified that it's working as expected!
			Object[] array = new Object[parameters.count()];
			for (int i = 0; i < array.length; i++) {
				array[i] = function.invoke();
			}
			return array;
		} else {
			return function.invoke();
		}
	}

	private Object deserializeAsObject(PrimitiveInputStream stream) throws CorfuDeserializationException {
		if (isArray()) {
			Object[] array = new Object[parameters.count()];
			for (int i = 0; i < array.length; i++) {
				array[i] = payloadMapper.deserialize(stream, componentType().asSubclass(CorfuStruct.class));
			}
			return array;
		} else {
			return payloadMapper.deserialize(stream, componentType().asSubclass(CorfuStruct.class));
		}
	}
}
