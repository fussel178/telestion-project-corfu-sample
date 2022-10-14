package de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper;

import java.util.Objects;

public class RodosCheckSum {
	/**
	 * <h2>Description</h2>
	 * Rotates the bit-representation of the input data one count to the right
	 * and move the fallen out bit (the right-most bit) back to the beginning (the left-most bit).
	 *
	 * <h2>Example</h2>
	 * <pre>
	 *     Input: 0111 1101 1111 0001
	 *
	 *     0111 1101 1011 0001 | shift 1 bit right
	 *     1011 1110 1101 1000 | fallen out bit: 1
	 *     └─────────────────────────────────────┘
	 * </pre>
	 *
	 * @param data the data that should be rotated
	 * @return the rotated data
	 */
	public static short rotateRight(short data) {
		if (((int) data & 0x0001) > 0) {
			// right-most bit is "1"
			// shift and make left-most bit "1" on shifted value
			return (short) ((short) ((int) data >>> 1) | (short) 0x8000);
		} else {
			// right-most bit is "0"
			// shift and make left-most bit "0" on shifted value
			return (short) ((short) ((int) data >>> 1) & (short) 0x7FFF);
		}
	}

	/**
	 * Calculates a checksum of a data packet by rotating the checksum and adding the data byte-wise
	 * to the rotated checksum for every byte in the data packet.
	 *
	 * @param data the data packet as a byte array
	 * @return the 16-bit (aka short) long checksum
	 */
	public static short calculateCheckSum(byte[] data) {
		return calculateCheckSum(data, 0, data.length);
	}

	/**
	 * Calculates a checksum of a data packet by rotating the checksum and adding the data byte-wise
	 * to the rotated checksum for every byte in the data packet.
	 *
	 * @param data     the data packet as a byte array
	 * @param offset the start position on the data packet
	 * @param length   the length of the part of data packet that should be calculated
	 * @return the 16-bit (aka short) long checksum
	 */
	public static short calculateCheckSum(byte[] data, int offset, int length) {
		Objects.checkFromIndexSize(offset, length, data.length);

		short checkSum = 0x0000; // initial value

		var readLength = offset + length;
		for (int i = offset; i < readLength; i++) {
			checkSum = rotateRight(checkSum);
			checkSum += (short) (data[i] & 0x00FF);
		}

		return checkSum;
	}
}
