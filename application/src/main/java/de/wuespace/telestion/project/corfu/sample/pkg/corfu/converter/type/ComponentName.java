package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents the name of a component in the Corfu configuration.
 *
 * @param raw the raw name as it's defined by the Corfu configuration (folder name, map key)
 * @param lowerCamelCase the lowerCamelCase version
 * @param upperCamelCase the UpperCamelCase version
 * @param screamingSnakeCase the SCREAMING_SNAKE_CASE version
 */
@SuppressWarnings("unused")
public record ComponentName(
		String raw,
		String lowerCamelCase,
		String upperCamelCase,
		String screamingSnakeCase
) {
	public ComponentName(String raw) {
		this(raw, toLowerCamelCase(raw), toUpperCamelCase(raw), toScreamingSnakeCase(raw));
	}

	public static String toPackageName(String name) {
		// remove any non-compatible package names
		return name.replaceAll("[^a-zA-Z]", "").toLowerCase();
	}

	public static String toUpperCamelCase(String name) {
		return splitName(name).map(ComponentName::capitalizeFirst).collect(Collectors.joining());
	}

	public static String toLowerCamelCase(String name) {
		var first = splitName(name).findFirst().orElseThrow();
		return first + splitName(name).skip(1).map(ComponentName::capitalizeFirst).collect(Collectors.joining());
	}

	public static String toScreamingSnakeCase(String name) {
		return splitName(name).collect(Collectors.joining("_")).toUpperCase();
	}

	public static Stream<String> splitName(String name) {
		return Stream.of(name)
				// split on delimiters
				.flatMap(part -> Arrays.stream(part.split("[-_]")))
				// split on upper case characters
				.flatMap(part -> Arrays.stream(part.split("(?=\\p{Upper})")))
				// make everything lower case
				.map(String::toLowerCase);
	}

	public static String capitalizeFirst(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}
}
