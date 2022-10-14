package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.generator.type;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Objects;

public record EntryValueType(String name, boolean isArray, String importClassName) {

	public static final EntryValueType BYTE = new EntryValueType(byte.class.getSimpleName(), false, null);

	public static final EntryValueType SHORT = new EntryValueType(short.class.getSimpleName(), false, null);

	public static final EntryValueType INT = new EntryValueType(int.class.getSimpleName(), false, null);

	public static final EntryValueType LONG = new EntryValueType(long.class.getSimpleName(), false, null);

	public static final EntryValueType FLOAT = new EntryValueType(float.class.getSimpleName(), false, null);

	public static final EntryValueType DOUBLE = new EntryValueType(double.class.getSimpleName(), false, null);

	public static final EntryValueType BIG_INTEGER = new EntryValueType(BigInteger.class.getSimpleName(),
			false,
			BigInteger.class.getName()
	);

	public static final EntryValueType BITSET = new EntryValueType(BitSet.class.getSimpleName(),
			false,
			BitSet.class.getName()
	);

	public boolean needsImport() {
		return Objects.nonNull(importClassName);
	}

	public EntryValueType toArray() {
		return new EntryValueType(this.name, true, this.importClassName);
	}
}
