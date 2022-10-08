package de.wuespace.telestion.project.corfu.sample.converter.util;

import java.util.Objects;

public class ByteArrayUtils {

	/**
	 * <h2>Description</h2>
	 * Calculates the amount of bytes to represent the number of bits in a "bit-by-bit" format.
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

	/**
	 * <h2>Description</h2>
	 * Inserts an unsigned short in front of a byte array.
	 *
	 * <h2>Example</h2>
	 * <pre>
	 *     New data:  01 23              (needs 2 bytes)
	 *     data:      45 67 89 10        (length: 4)
	 *     Result:    01 23 45 67 89 10  (length: 6)
	 * </pre>
	 * @param prefix the unsigned short that should be inserted in front of the data
	 * @param data the remaining data that should be inserted behind the unsigned short
	 * @return the combined data as byte array
	 */
	public static byte[] packFrontUnsignedShort(short prefix, byte[] data) {
		byte[] buffer = new byte[data.length + 2]; // short needs 2 bytes in size

		// split new data in insert first
		buffer[0] = (byte) (prefix >>> 8);
		buffer[1] = (byte) prefix;

		// copy remaining data
		System.arraycopy(data, 0, buffer, 2, data.length);
		return buffer;
	}

	public static SplitData splitFrontUnsignedShort(byte[] data) {
		if (data.length < 2) {
			throw new IllegalArgumentException("The data byte array is to small to split of an unsigned short. " +
					"Please provide more data and try again");
		}

		byte[] buffer = new byte[data.length - 2];
		int part1 = data[0] & 0xFF; // unsigned conversion from byte to int
		int part2 = data[1] & 0xFF; // unsigned conversion from byte to int
		short prefix = (short) ((part1 << 8) + part2); // add them together with part1 being MSB

		System.arraycopy(data, 2, buffer, 0, data.length - 2);
		return new SplitData(prefix, buffer);
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
