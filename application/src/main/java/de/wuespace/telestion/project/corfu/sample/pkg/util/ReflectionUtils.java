package de.wuespace.telestion.project.corfu.sample.pkg.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.util.List;

public class ReflectionUtils {
	public static Class<?> getListElementType(RecordComponent component) {
		if (!List.class.isAssignableFrom(component.getType())) {
			throw new IllegalArgumentException(("The type %s in the given record component %s is not a list. Please " +
					"provide a list type and try again").formatted(component.getType().getName(), component.getName()));
		}

		var listType = (ParameterizedType) component.getGenericType();
		return (Class<?>) listType.getActualTypeArguments()[0];
	}

	public static Class<?> getListElementType(Field field) {
		if (!List.class.isAssignableFrom(field.getType())) {
			throw new IllegalArgumentException(("The type %s in the given field %s is not a list. Please provide " +
					"a list type and try again").formatted(field.getType().getName(), field.getName()));
		}

		var listType = (ParameterizedType) field.getGenericType();
		return (Class<?>) listType.getActualTypeArguments()[0];
	}
}
