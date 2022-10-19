package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuProperty.Type;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TypeReference {
	public static final TypeReference BITARRAY = new TypeReference(Type.BITARRAY);
	public static final TypeReference BOOLEAN = new TypeReference(Type.BOOLEAN);
	public static final TypeReference FLOAT = new TypeReference(Type.FLOAT);
	public static final TypeReference DOUBLE = new TypeReference(Type.DOUBLE);
	public static final TypeReference INT8 = new TypeReference(Type.INT8);
	public static final TypeReference INT16 = new TypeReference(Type.INT16);
	public static final TypeReference INT32 = new TypeReference(Type.INT32);
	public static final TypeReference INT64 = new TypeReference(Type.INT64);
	public static final TypeReference UINT8 = new TypeReference(Type.UINT8);
	public static final TypeReference UINT16 = new TypeReference(Type.UINT16);
	public static final TypeReference UINT32 = new TypeReference(Type.UINT32);
	public static final TypeReference UINT64 = new TypeReference(Type.UINT64);

	public static final Map<String, TypeReference> primitiveTypeReferences;

	static {
		// here are the mappings from C++ primitive types to the Java / CorfuProperty primitive types
		primitiveTypeReferences = Map.ofEntries(
				Map.entry("bool", BOOLEAN),
				Map.entry("std::bool", BOOLEAN),
				Map.entry("float", FLOAT),
				Map.entry("std::float", FLOAT),
				Map.entry("double", DOUBLE),
				Map.entry("std::double", DOUBLE),
				Map.entry("int8_t", INT8),
				Map.entry("std::int8_t", INT8),
				Map.entry("int16_t", INT16),
				Map.entry("std::int16_t", INT16),
				Map.entry("int32_t", INT32),
				Map.entry("std::int32_t", INT32),
				Map.entry("int", INT32),
				Map.entry("std::int", INT32),
				Map.entry("int64_t", INT64),
				Map.entry("std::int64_t", INT64),
				Map.entry("uint8_t", UINT8),
				Map.entry("std::uint8_t", UINT8),
				Map.entry("uint16_t", UINT16),
				Map.entry("std::uint16_t", UINT16),
				Map.entry("uint32_t", UINT32),
				Map.entry("std::uint32_t", UINT32),
				Map.entry("uint64_t", UINT64),
				Map.entry("std::uint64_t", UINT64),
				// custom Corfu types,
				Map.entry("corfu::Anomaly::ID", UINT16),
				Map.entry("Anomaly::ID", UINT16)
		);
	}

	/**
	 * Transforms the given struct definitions into references
	 * so telemetry and telecommand messages can reference them.
	 * Primitive type references from Corfu are added by default.
	 *
	 * @param structs the structs from an app configuration
	 * @return a reference map that can map types from the Corfu config to Java objects / type references
	 */
	public static Map<String, TypeReference> getReferences(Collection<MessageFields> structs) {
		Map<String, TypeReference> references = new HashMap<>(primitiveTypeReferences);
		structs.forEach(fields ->
				references.put(fields.getName().raw(), new TypeReference(fields.getName().upperCamelCase() + "Struct")));

		return references;
	}

	private final Type corfuType;
	private final String simpleName;

	protected TypeReference(String simpleName) {
		this.corfuType = Type.OBJECT;
		this.simpleName = simpleName;
	}

	protected TypeReference(Type corfuType) {
		this.corfuType = corfuType;
		this.simpleName = corfuType.suitableType().getSimpleName();
	}

	public Type corfuType() {
		return corfuType;
	}

	public String simpleName() {
		return simpleName;
	}

	@Override
	public String toString() {
		return "TypeReference{" +
				"corfuType=" + corfuType +
				", simpleName='" + simpleName + '\'' +
				'}';
	}
}
