package de.wuespace.telestion.project.corfu.sample.pkg.util.stream;

import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.OutputStreamFullException;

import java.math.BigInteger;
import java.util.BitSet;

/**
 * An output stream that allows accessors to write primitive types to it.
 * It supports all primitives that are required to process Corfu messages and payloads.
 *
 * @author Ludwig Richter (@fussel178)
 */
public interface PrimitiveOutputStream {
	/**
	 * Writes a sequence of bytes to the output stream provided by the data array.
	 * @param data the array that contains the sequence of bytes
	 * @param offset the offset within the data array to start the transfer
	 * @param length the maximum number of bytes that should be written to the output stream
	 */
	void writeSignedBytes(byte[] data, int offset, int length) throws OutputStreamFullException;

	/**
	 * Writes a sequence of bytes to the output stream provided by the data array.
	 * @param data the array that contains the sequence of bytes
	 */
	void writeSignedBytes(byte[] data) throws OutputStreamFullException;

	/**
	 * Writes a signed byte to the output stream.
	 * @param data the data in signed byte form
	 */
	void writeSignedByte(byte data) throws OutputStreamFullException;

	/**
	 * Writes an unsigned byte to the output stream.
	 * @param data the data in unsigned byte form
	 */
	void writeUnsignedByte(short data) throws OutputStreamFullException;

	/**
	 * Writes a signed short to the output stream.
	 * @param data the data in signed short form
	 */
	void writeSignedShort(short data) throws OutputStreamFullException;

	/**
	 * Writes an unsigned short to the output stream.
	 * @param data the data in unsigned short form
	 */
	void writeUnsignedShort(int data) throws OutputStreamFullException;

	/**
	 * Writes a signed integer to the output stream.
	 * @param data the data in signed integer form
	 */
	void writeSignedInteger(int data) throws OutputStreamFullException;

	/**
	 * Writes an unsigned integer to the output stream.
	 * @param data the data in unsigned integer form
	 */
	void writeUnsignedInteger(long data) throws OutputStreamFullException;

	/**
	 * Writes a signed long to the output stream.
	 * @param data the data in signed long form
	 */
	void writeSignedLong(long data) throws OutputStreamFullException;

	/**
	 * Writes an unsigned long to the output stream.
	 * @param data the data in unsigned long form
	 */
	void writeUnsignedLong(BigInteger data) throws OutputStreamFullException;

	/**
	 * Writes a float to the output stream.
	 * @param data the data in float form
	 */
	void writeFloat(float data) throws OutputStreamFullException;

	/**
	 * Writes a double to the output stream.
	 * @param data the data in double form
	 */
	void writeDouble(double data) throws OutputStreamFullException;

	/**
	 * Writes a boolean to the output stream.
	 * @param data the data in boolean form
	 */
	void writeBoolean(boolean data) throws OutputStreamFullException;

	/**
	 * Writes a bitset to the output stream.
	 * @param data the bitset that contains the different states
	 * @param count the number of bits that should be written to the output stream
	 */
	void writeBitSet(BitSet data, int count) throws OutputStreamFullException;
}
