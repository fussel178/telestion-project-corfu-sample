package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

import java.util.Map;

public class BitArrayMessageType implements MessageType {

	private ComponentName name;
	private final int count;

	public BitArrayMessageType(int count) {
		this.count = count;
	}

	@Override
	public void resolveType(Map<String, TypeReference> references) {
		// Bit arrays don't need to resolve the type
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
		return "bitset";
	}

	@Override
	public TypeReference reference() {
		return TypeReference.BITARRAY;
	}

	@Override
	public String toString() {
		return "BitArrayMessageType{" +
				"name=" + name +
				", count=" + count +
				'}';
	}
}
