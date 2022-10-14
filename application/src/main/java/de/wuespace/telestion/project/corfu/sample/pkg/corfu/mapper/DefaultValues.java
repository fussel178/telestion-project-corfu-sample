package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class DefaultValues {
	private final static Map<Class<?>, Object> defaults = new HashMap<>();

	static {
		// primitive types
		defaults.put(boolean.class, false);
		defaults.put(byte.class, (byte) 0);
		defaults.put(short.class, (short) 0);
		defaults.put(int.class, 0);
		defaults.put(long.class, 0L);
		defaults.put(float.class, 0.0f);
		defaults.put(double.class, 0.0);
		// boxed primitive types
		defaults.put(Boolean.class, false);
		defaults.put(Byte.class, defaults.get(byte.class));
		defaults.put(Short.class, defaults.get(short.class));
		defaults.put(Integer.class, defaults.get(int.class));
		defaults.put(Long.class, defaults.get(long.class));
		defaults.put(Float.class, defaults.get(float.class));
		defaults.put(Double.class, defaults.get(double.class));
		// specials
		defaults.put(BitSet.class, new BitSet());
		defaults.put(BigInteger.class, BigInteger.ZERO);
	}

	public static <T> T getDefaultValue(Class<T> type) {
		// Because we decide in the static block above what values are allowed.
		//noinspection unchecked
		return defaults.containsKey(type) ? (T) defaults.get(type) : null;
	}
}
