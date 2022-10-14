package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigInteger;
import java.util.BitSet;

@Target({ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorfuProperty {
	Type value() default Type.OBJECT;
	int count() default 1;

	enum Type {
		// atomic types
		BOOLEAN(boolean.class, 1, 1),
		FLOAT(float.class, 4, 1),
		DOUBLE(double.class, 8, 1),
		INT8(byte.class, 1, 1),
		INT16(short.class, 2, 1),
		INT32(int.class, 4, 1),
		INT64(long.class, 8, 1),
		UINT8(short.class, 1, 1),
		UINT16(int.class, 2, 1),
		UINT32(long.class, 4, 1),
		UINT64(BigInteger.class, 8, 1),
		BITARRAY(BitSet.class, 1, Integer.MAX_VALUE),
		OBJECT(Object.class, 1, Integer.MAX_VALUE);

		private final Class<?> suitableType;
		private final int size;
		private final int maxCount;

		Type(Class<?> suitableType, int size, int maxCount) {
			this.suitableType = suitableType;
			this.size = size;
			this.maxCount = maxCount;
		}

		public Class<?> suitableType() {
			return suitableType;
		}

		/**
		 * Returns the size of this corfu type in the packed c struct format in bytes.
		 * @param count the count annotated on the payload field
		 */
		public int size(int count) {
			return size * count;
		}

		public int maxCount() {
			return maxCount;
		}

		/**
		 * Verifies, that the given class type is suitable for this corfu type.
		 * @param other the class type that should check against the suitable type for this corfu type
		 * @return <code>true</code>, if the class type is suitable
		 */
		public boolean hasSuitableType(Class<?> other) {
			return suitableType.isAssignableFrom(other);
		}

		public boolean hasValidCount(int count) {
			return count <= this.maxCount;
		}
	}
}
