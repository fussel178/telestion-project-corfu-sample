package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

import java.util.Map;

public class SingleMessageType implements MessageType {

	private ComponentName name;
	private final String type;
	private TypeReference reference;

	public SingleMessageType(String type) {
		this.type = type;
		this.reference = null;
	}

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
		return 1;
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
		return "SingleMessageType{" +
				"name=" + name +
				", type='" + type + '\'' +
				", reference=" + reference +
				'}';
	}
}
