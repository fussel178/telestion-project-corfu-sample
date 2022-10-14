package de.wuespace.telestion.project.corfu.sample.pkg.util.stream;

import de.wuespace.telestion.project.corfu.sample.pkg.util.ByteArrayUtils;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.InputStreamEmptyException;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.OutputStreamFullException;

import java.math.BigInteger;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.BitSet;

/**
 * A stream-like implementation that allows serialization and deserialization of primitive types
 * in a stream-like fashion.
 * Internally it uses a byte buffer with a fixed capacity.
 *
 * @author Ludwig Richter (@fussel178)
 */
public class ByteBufferStream implements PrimitiveInputStream, PrimitiveOutputStream {
	private final ByteBuffer buffer;

	public ByteBufferStream(int capacity) {
		this(capacity, ByteOrder.BIG_ENDIAN);
	}

	public ByteBufferStream(int capacity, ByteOrder order) {
		// set mark to start of buffer
		this.buffer = ByteBuffer.allocate(capacity).order(order).mark();
	}

	/**
	 * Returns the current capacity of the underlying buffer.
	 *
	 * @return the capacity of the stream
	 */
	public int capacity() {
		return buffer.capacity();
	}

	/**
	 * Returns the current position of the underlying buffer.
	 *
	 * @return the current position of the stream
	 */
	public int position() {
		return buffer.position();
	}

	/**
	 * Returns the current byte order of the underlying buffer.
	 *
	 * @return the current byte order of the stream
	 */
	public ByteOrder order() {
		return buffer.order();
	}

	/**
	 * Changes the byte order of the stream to further operations on the stream.
	 *
	 * @param order the new byte order
	 */
	public void setOrder(ByteOrder order) {
		this.buffer.order(order);
	}

	/**
	 * Sets the new position of the underlying buffer.
	 *
	 * @param newPosition the new position of the stream
	 */
	public void setPosition(int newPosition) {
		buffer.position(newPosition);
	}

	/**
	 * Resets the stream to the initial position.
	 */
	public void reset() {
		buffer.reset();
	}

	/**
	 * Resets and clears the entire stream.
	 */
	public void clear() {
		// set mark to start of buffer
		buffer.clear().mark();
	}

	@Override
	public void readSignedBytes(byte[] destination, int offset, int length) throws InputStreamEmptyException {
		try {
			buffer.get(destination, offset, length);
		} catch (BufferUnderflowException e) {
			throw new InputStreamEmptyException("The input stream is empty.", e);
		}
	}

	@Override
	public void readSignedBytes(byte[] destination) throws InputStreamEmptyException {
		try {
			buffer.get(destination);
		} catch (BufferUnderflowException e) {
			throw new InputStreamEmptyException("The input stream is empty.", e);
		}
	}

	@Override
	public byte readSignedByte() throws InputStreamEmptyException {
		try {
			return buffer.get();
		} catch (BufferUnderflowException e) {
			throw new InputStreamEmptyException("The input stream is empty.", e);
		}
	}

	@Override
	public short readUnsignedByte() throws InputStreamEmptyException {
		try {
			return (short) (buffer.get() & 0xFF);
		} catch (BufferUnderflowException e) {
			throw new InputStreamEmptyException("The input stream is empty.", e);
		}
	}

	@Override
	public short readSignedShort() throws InputStreamEmptyException {
		try {
			return buffer.getShort();
		} catch (BufferUnderflowException e) {
			throw new InputStreamEmptyException("The input stream is empty.", e);
		}
	}

	@Override
	public int readUnsignedShort() throws InputStreamEmptyException {
		try {
			return buffer.getShort() & 0xFFFF;
		} catch (BufferUnderflowException e) {
			throw new InputStreamEmptyException("The input stream is empty.", e);
		}
	}

	@Override
	public int readSignedInteger() throws InputStreamEmptyException {
		try {
			return buffer.getInt();
		} catch (BufferUnderflowException e) {
			throw new InputStreamEmptyException("The input stream is empty.", e);
		}
	}

	@Override
	public long readUnsignedInteger() throws InputStreamEmptyException {
		try {
			return buffer.getInt() & 0xFFFFFFFFL;
		} catch (BufferUnderflowException e) {
			throw new InputStreamEmptyException("The input stream is empty.", e);
		}
	}

	@Override
	public long readSignedLong() throws InputStreamEmptyException {
		try {
			return buffer.getLong();
		} catch (BufferUnderflowException e) {
			throw new InputStreamEmptyException("The input stream is empty.", e);
		}
	}

	@Override
	public BigInteger readUnsignedLong() throws InputStreamEmptyException {
		try {
			var rawValue = BigInteger.valueOf(buffer.getLong());
			// revert negation in valueOf method
			return rawValue.compareTo(BigInteger.ZERO) < 0 ? rawValue.multiply(BigInteger.valueOf(-1)) : rawValue;
		} catch (BufferUnderflowException e) {
			throw new InputStreamEmptyException("The input stream is empty.", e);
		}
	}

	@Override
	public float readFloat() throws InputStreamEmptyException {
		try {
			// prepare
			var initialOrder = buffer.order();
			buffer.order(ByteOrder.BIG_ENDIAN);
			// read
			float data = buffer.getFloat();
			// restore
			buffer.order(initialOrder);
			return data;
		} catch (BufferUnderflowException e) {
			throw new InputStreamEmptyException("The input stream is empty.", e);
		}
	}

	@Override
	public double readDouble() throws InputStreamEmptyException {
		try {
			// prepare
			var initialOrder = buffer.order();
			buffer.order(ByteOrder.BIG_ENDIAN);
			// read
			double data = buffer.getDouble();
			// restore
			buffer.order(initialOrder);
			return data;
		} catch (BufferUnderflowException e) {
			throw new InputStreamEmptyException("The input stream is empty.", e);
		}
	}

	@Override
	public boolean readBoolean() throws InputStreamEmptyException {
		try {
			return buffer.get() != 0;
		} catch (BufferUnderflowException e) {
			throw new InputStreamEmptyException("The input stream is empty.", e);
		}
	}

	@Override
	public BitSet readBitSet(int count) throws InputStreamEmptyException {
		var numberOfBytes = ByteArrayUtils.bytesPerBits(count);

		// fill buffer
		byte[] bitSetBuffer = new byte[numberOfBytes];
		readSignedBytes(bitSetBuffer);

		// create bitset from buffer
		var bitSet = BitSet.valueOf(bitSetBuffer);

		// clear bits beyond count but within bytes per bits range
		// (because the user doesn't care about these states)
		bitSet.clear(count - 1, numberOfBytes * 8);

		return bitSet;
	}

	@Override
	public void writeSignedBytes(byte[] data, int offset, int length) throws OutputStreamFullException {
		try {
			buffer.put(data, offset, length);
		} catch (BufferOverflowException e) {
			throw new OutputStreamFullException("The output stream is full.", e);
		}
	}

	@Override
	public void writeSignedBytes(byte[] data) throws OutputStreamFullException {
		try {
			buffer.put(data);
		} catch (BufferOverflowException e) {
			throw new OutputStreamFullException("The output stream is full.", e);
		}
	}

	@Override
	public void writeSignedByte(byte data) throws OutputStreamFullException {
		try {
			buffer.put(data);
		} catch (BufferOverflowException e) {
			throw new OutputStreamFullException("The output stream is full.", e);
		}
	}

	@Override
	public void writeUnsignedByte(short data) throws OutputStreamFullException {
		if (data < 0) {
			throw new IllegalArgumentException("Unsigned byte %d cannot be negative".formatted(data));
		}

		try {
			buffer.put((byte) data);
		} catch (BufferOverflowException e) {
			throw new OutputStreamFullException("The output stream is full.", e);
		}
	}

	@Override
	public void writeSignedShort(short data) throws OutputStreamFullException {
		try {
			buffer.putShort(data);
		} catch (BufferOverflowException e) {
			throw new OutputStreamFullException("The output stream is full.", e);
		}
	}

	@Override
	public void writeUnsignedShort(int data) throws OutputStreamFullException {
		if (data < 0) {
			throw new IllegalArgumentException("Unsigned short %d cannot be negative".formatted(data));
		}

		try {
			buffer.putShort((short) data);
		} catch (BufferOverflowException e) {
			throw new OutputStreamFullException("The output stream is full.", e);
		}
	}

	@Override
	public void writeSignedInteger(int data) throws OutputStreamFullException {
		try {
			buffer.putInt(data);
		} catch (BufferOverflowException e) {
			throw new OutputStreamFullException("The output stream is full.", e);
		}
	}

	@Override
	public void writeUnsignedInteger(long data) throws OutputStreamFullException {
		if (data < 0) {
			throw new IllegalArgumentException("Unsigned integer %d cannot be negative".formatted(data));
		}

		try {
			buffer.putInt((int) data);
		} catch (BufferOverflowException e) {
			throw new OutputStreamFullException("The output stream is full.", e);
		}
	}

	@Override
	public void writeSignedLong(long data) throws OutputStreamFullException {
		try {
			buffer.putLong(data);
		} catch (BufferOverflowException e) {
			throw new OutputStreamFullException("The output stream is full.", e);
		}
	}

	@Override
	public void writeUnsignedLong(BigInteger data) throws OutputStreamFullException {
		if (data.compareTo(BigInteger.ZERO) < 0) {
			throw new IllegalArgumentException("Unsigned long %d cannot be negative".formatted(data));
		}

		try {
			buffer.putLong(data.longValue());
		} catch (BufferOverflowException e) {
			throw new OutputStreamFullException("The output stream is full.", e);
		}
	}

	@Override
	public void writeFloat(float data) throws OutputStreamFullException {
		try {
			// prepare
			var initialOrder = buffer.order();
			buffer.order(ByteOrder.BIG_ENDIAN);
			// write
			buffer.putFloat(data);
			// restore
			buffer.order(initialOrder);
		} catch (BufferOverflowException e) {
			throw new OutputStreamFullException("The output stream is full.", e);
		}
	}

	@Override
	public void writeDouble(double data) throws OutputStreamFullException {
		try {
			// prepare
			var initialOrder = buffer.order();
			buffer.order(ByteOrder.BIG_ENDIAN);
			// write
			buffer.putDouble(data);
			// restore
			buffer.order(initialOrder);
		} catch (BufferOverflowException e) {
			throw new OutputStreamFullException("The output stream is full.", e);
		}
	}

	@Override
	public void writeBoolean(boolean data) throws OutputStreamFullException {
		try {
			buffer.put(data ? (byte) 0x01 : (byte) 0x00);
		} catch (BufferOverflowException e) {
			throw new OutputStreamFullException("The output stream is full.", e);
		}
	}

	@Override
	public void writeBitSet(BitSet data, int count) throws OutputStreamFullException {
		if (data.length() > count) {
			throw new IllegalArgumentException(("The bitset %s has bits set outside the specified range %d. Please " +
					"unset the check that the bitset is in the annotated range and try again.").formatted(data, count));
		}

		var neededByteCount = ByteArrayUtils.bytesPerBits(count);
		var bitSetBuffer = new byte[neededByteCount];
		var bitSetBytes = data.toByteArray();
		System.arraycopy(bitSetBytes, 0, bitSetBuffer, 0, bitSetBytes.length);

		writeSignedBytes(bitSetBuffer);
	}
}
