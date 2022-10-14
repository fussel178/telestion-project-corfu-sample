package de.wuespace.telestion.project.corfu.sample.pkg.util;

import java.util.Objects;

public class ByteArrayUtils {

	/**
	 * <h2>Description</h2>
	 * Calculates the amount of bytes that are needed to represent the number of bits in a "bit-by-bit" format.
	 *
	 * <h2>Example</h2>
	 *
	 * <pre>
	 *                              <--- 1 byte ---> <--- 1 byte --->
	 *     Needed by application:   1 1 1 0 0 1 0 1 : 1 1              (10 bits)
	 *     Storage representation:  1 1 1 0 0 1 0 1 : 1 1 0 0 0 0 0 0  (16 bits or 2 bytes)
	 * </pre>
	 *
	 * @param bits the number of bits
	 * @return the required amount of bytes to represent the number of bits
	 */
	public static int bytesPerBits(int bits) {
		return bits > 0 ? (bits - 1) / 8 + 1 : 0;
	}

	/**
	 * <h2>Description</h2>
	 * Stringifies the given binary array in a colon of words in hexadecimal representation.
	 *
	 * <h2>Example</h2>
	 *
	 * <pre>0E 13 29 A4 BC BF FF 07 35 69</pre>
	 *
	 * @param data the binary array that should be stringified
	 * @return the stringified version of the binary data
	 */
	public static String toHexString(byte[] data) {
		int iMax = data.length - 1;
		if (iMax < 0) {
			return "";
		}

		var builder = new StringBuilder();
		for (int i = 0; ; i++) {
			builder.append(String.format("%02X", data[i]));
			if (i >= iMax) {
				return builder.toString();
			}

			builder.append(" ");
		}
	}

	public static String toString(byte[] data) {
		if (Objects.isNull(data)) return "null";

		int iMax = data.length - 1;
		if (iMax < 0) {
			return "[]";
		}

		var sb = new StringBuilder().append("[");
		for (int i = 0; ; i++) {
			sb.append(String.format("%02X", data[i]));
			if (i >= iMax) {
				return sb.append("] (size: ").append(data.length).append(" bytes)").toString();
			}

			sb.append(" ");
		}
	}
}
