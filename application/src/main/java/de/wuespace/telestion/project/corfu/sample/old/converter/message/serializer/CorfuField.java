package de.wuespace.telestion.project.corfu.sample.old.converter.message.serializer;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuProperty;
import de.wuespace.telestion.project.corfu.sample.old.converter.message.CorfuMessage;
import de.wuespace.telestion.project.corfu.sample.old.converter.message.CorfuSerializationException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public final class CorfuField implements Comparable<CorfuField> {
	private final Field field;
	private final CorfuProperty.Type type;
	private final int position;

	public CorfuField(Field field) {
		this.field = field;
		var annotation = field.getAnnotation(CorfuProperty.class);
		this.type = annotation.value();
		this.position = -1;
	}

	public Field field() {
		return field;
	}

	public CorfuProperty.Type type() {
		return type;
	}

	public int position() {
		return position;
	}

	public <T> T cast(CorfuMessage instance, Class<T> type) throws CorfuSerializationException {
		try {
			return type.cast(field.get(instance));
		} catch (IllegalAccessException e) {
			throw new CorfuSerializationException(("Cannot access value of field %s " +
					"because it is prohibited by the security manager. " +
					"Please allow access to package and private properties and try again. " +
					"Current instance: %s").formatted(field.getName(), instance));
		} catch (ClassCastException e) {
			throw new CorfuSerializationException(("Cannot cast value of field %s to requested type. " +
					"Please check that the requested cast type matches with the field type definition and try again. " +
					"Current instance: %s").formatted(field.getName(), instance));
		}
	}

	public boolean isList() {
		return List.class.isAssignableFrom(field.getType());
	}

	public boolean isCorfuMessage() {
		return CorfuMessage.class.isAssignableFrom(field.getType());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (CorfuField) obj;
		return Objects.equals(this.field, that.field) &&
				Objects.equals(this.type, that.type) &&
				this.position == that.position;
	}

	@Override
	public int hashCode() {
		return Objects.hash(field, type, position);
	}

	@Override
	public String toString() {
		return "CorfuField[" +
				"field=" + field + ", " +
				"type=" + type + ", " +
				"position=" + position + ']';
	}

	@Override
	public int compareTo(CorfuField other) {
		return this.position - other.position;
	}
}
