package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

import java.util.Map;

public class ArrayMessageType implements MessageType {

	private ComponentName name;
	private final String type;
	private final int count;
	private TypeReference reference;

	public ArrayMessageType(String type, int count) {
		this.type = type;
		this.count = count;
	}

	@Override
	public void resolveType(Map<String, TypeReference> references) {
		if (!references.containsKey(type)) {
			throw new IllegalArgumentException(("The type %s does not exist." +
					"Please provide a struct specification with this name and try again.").formatted(type));
		}

		this.reference = references.get(type);
	}

	@Override
	public void setName(String raw) {
		this.name = new ComponentName(raw);
	}

	@Override
	public ComponentName getName() {
		return name;
	}

	@Override
	public int count() {
		return count;
	}

	@Override
	public String type() {
		return type;
	}

	@Override
	public TypeReference reference() {
		return reference;
	}

	@Override
	public String toString() {
		return "ArrayMessageType{" +
				"name=" + name +
				", type='" + type + '\'' +
				", count=" + count +
				", reference=" + reference +
				'}';
	}
}
