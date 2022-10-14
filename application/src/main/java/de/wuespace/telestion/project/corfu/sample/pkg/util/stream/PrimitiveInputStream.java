package de.wuespace.telestion.project.corfu.sample.pkg.util.stream;

import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.InputStreamEmptyException;

import java.math.BigInteger;
import java.util.BitSet;

/**
 * An input stream that allows accessors to read primitive types from it.
 * It supports all primitives that are required to process Corfu messages and payload.
 *
 * @author Ludwig Richter (@fussel178)
 */
public interface PrimitiveInputStream {
	/**
	 * Reads a sequence of bytes from the input stream and stores them in the given destination array.
	 * @param destination the array that contains the sequence of bytes after the operation.
	 * @param offset the offset within the destination array to start the transfer
	 * @param length the maximum number of bytes that should be written to the destination array
	 */
	void readSignedBytes(byte[] destination, int offset, int length) throws InputStreamEmptyException;

	/**
	 * Reads a sequence of bytes from the input stream and stores them in the given destination array.
	 * @param destination the array that contains the sequence of bytes after the operation.
	 */
	void readSignedBytes(byte[] destination) throws InputStreamEmptyException;

	/**
	 * Reads one signed byte from the input stream.
	 * @return one signed byte from the stream
	 */
	byte readSignedByte() throws InputStreamEmptyException;

	/**
	 * Reads one unsigned byte from the input stream.
	 * @return one unsigned byte from the stream
	 */
	short readUnsignedByte() throws InputStreamEmptyException;

	/**
	 * Reads one signed short from the input stream.
	 * @return one signed short from the stream
	 */
	short readSignedShort() throws InputStreamEmptyException;

	/**
	 * Reads one unsigned short from the input stream.
	 * @return one unsigned short from the stream
	 */
	int readUnsignedShort() throws InputStreamEmptyException;

	/**
	 * Reads one signed integer from the input stream.
	 * @return one signed integer from the stream
	 */
	int readSignedInteger() throws InputStreamEmptyException;

	/**
	 * Reads one unsigned integer from the input stream.
	 * @return one unsigned integer from the stream
	 */
	long readUnsignedInteger() throws InputStreamEmptyException;

	/**
	 * Reads one signed long from the input stream.
	 * @return one signed long from the stream
	 */
	long readSignedLong() throws InputStreamEmptyException;

	/**
	 * Reads one unsigned long from the input stream.
	 * @return one unsigned long from the stream
	 */
	BigInteger readUnsignedLong() throws InputStreamEmptyException;

	/**
	 * Reads one float from the input stream.
	 * @return one float from the stream
	 */
	float readFloat() throws InputStreamEmptyException;

	/**
	 * Reads one double from the input stream.
	 * @return one double from the stream
	 */
	double readDouble() throws InputStreamEmptyException;

	/**
	 * Reads one boolean from the input stream.
	 * @return one boolean from the stream
	 */
	boolean readBoolean() throws InputStreamEmptyException;

	/**
	 * Reads a bitset from the input stream.
	 * @param count the number of bits that should be read from the stream
	 * @return a bitset containing the bits
	 */
	BitSet readBitSet(int count) throws InputStreamEmptyException;
}
