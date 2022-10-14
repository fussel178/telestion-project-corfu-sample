package de.wuespace.telestion.project.corfu.sample.old.converter.message.serializer;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuProperty;
import de.wuespace.telestion.project.corfu.sample.old.converter.message.CorfuMessage;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class CorfuFieldUtils {
	public static List<CorfuField> getCorfuFields(Class<? extends CorfuMessage> messageClazz) {
		var fields = Arrays.stream(messageClazz.getDeclaredFields())
				// filter out non annotated fields
				.filter(CorfuFieldUtils::hasCorfuAnnotation)
				// wrap them in a corfu field
				.map(CorfuField::new)
				// sort them based on the position annotation
				.sorted().toList();

		// make them accessible
		fields.forEach(field -> field.field().setAccessible(true));

		return fields;
	}

	public static boolean hasCorfuAnnotation(Field field) {
		return field.isAnnotationPresent(CorfuProperty.class);
	}
}
