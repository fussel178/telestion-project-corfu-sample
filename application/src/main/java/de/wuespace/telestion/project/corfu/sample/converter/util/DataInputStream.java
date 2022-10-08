package de.wuespace.telestion.project.corfu.sample.converter.util;

import de.wuespace.telestion.project.corfu.sample.converter.exception.DeserializationException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.BitSet;

public class DataInputStream {
	private final RewritableByteArrayInputStream byteStream;
	private final java.io.DataInputStream stream;

	public DataInputStream(byte[] data) {
		byteStream = new RewritableByteArrayInputStream(data);
		stream = new java.io.DataInputStream(byteStream);
	}

	public DataInputStream() {
		this(new byte[]{});
	}

	public byte readSignedByte() throws DeserializationException {
		try {
			return stream.readByte();
		} catch (IOException e) {
			throw new DeserializationException("Cannot read signed byte from byte stream %s at position %d"
					.formatted(ByteArrayUtils.toString(byteStream.getBuffer()), byteStream.getCurrentPos()));
		}
	}

	public short readUnsignedByte() throws DeserializationException {
		try {
			return (short) stream.readUnsignedByte();
		} catch (IOException e) {
			throw new DeserializationException("Cannot read unsigned byte from byte stream %s at position %d"
					.formatted(ByteArrayUtils.toString(byteStream.getBuffer()), byteStream.getCurrentPos()));
		}
	}

	public short readSignedShort() throws DeserializationException {
		try {
			return stream.readShort();
		} catch (IOException e) {
			throw new DeserializationException("Cannot read signed short from byte stream %s at position %d"
					.formatted(ByteArrayUtils.toString(byteStream.getBuffer()), byteStream.getCurrentPos()));
		}
	}

	public int readUnsignedShort() throws DeserializationException {
		try {
			return stream.readUnsignedShort();
		} catch (IOException e) {
			throw new DeserializationException("Cannot read unsigned short from byte stream %s at position %d"
					.formatted(ByteArrayUtils.toString(byteStream.getBuffer()), byteStream.getCurrentPos()));
		}
	}

	public int readSignedInt() throws DeserializationException {
		try {
			return stream.readInt();
		} catch (IOException e) {
			throw new DeserializationException("Cannot read signed integer from byte stream %s at position %d"
					.formatted(ByteArrayUtils.toString(byteStream.getBuffer()), byteStream.getCurrentPos()));
		}
	}

	public long readUnsignedInt() throws DeserializationException {
		try {
			// cast to long (which keeps the negative value of int)
			// throw away bits greater than integer to make the result positive
			return ((long) stream.readInt() & 0xFFFFFFFFL);
		} catch (IOException e) {
			throw new DeserializationException("Cannot read unsigned integer from byte stream %s at position %d"
					.formatted(ByteArrayUtils.toString(byteStream.getBuffer()), byteStream.getCurrentPos()));
		}
	}

	public long readSignedLong() throws DeserializationException {
		try {
			return stream.readLong();
		} catch (IOException e) {
			throw new DeserializationException("Cannot read signed long from byte stream %s at position %d"
					.formatted(ByteArrayUtils.toString(byteStream.getBuffer()), byteStream.getCurrentPos()));
		}
	}

	public BigInteger readUnsignedLong() throws DeserializationException {
		try {
			var rawValue = BigInteger.valueOf(stream.readLong());
			// revert negation in valueOf method
			return rawValue.compareTo(BigInteger.ZERO) < 0 ? rawValue.multiply(BigInteger.valueOf(-1)) : rawValue;
		} catch (IOException e) {
			throw new DeserializationException("Cannot read unsigned long from byte stream %s at position %d"
					.formatted(ByteArrayUtils.toString(byteStream.getBuffer()), byteStream.getCurrentPos()));
		}
	}

	public float readFloat() throws DeserializationException {
		try {
			return stream.readFloat();
		} catch (IOException e) {
			throw new DeserializationException("Cannot read float from byte stream %s at position %d"
					.formatted(ByteArrayUtils.toString(byteStream.getBuffer()), byteStream.getCurrentPos()));
		}
	}

	public double readDouble() throws DeserializationException {
		try {
			return stream.readDouble();
		} catch (IOException e) {
			throw new DeserializationException("Cannot read double from byte stream %s at position %d"
					.formatted(ByteArrayUtils.toString(byteStream.getBuffer()), byteStream.getCurrentPos()));
		}
	}

	public boolean readBoolean() throws DeserializationException {
		try {
			return stream.readBoolean();
		} catch (IOException e) {
			throw new DeserializationException("Cannot read boolean from byte stream %s at position %d"
					.formatted(ByteArrayUtils.toString(byteStream.getBuffer()), byteStream.getCurrentPos()));
		}
	}

	public BitSet readBitSet(int count) throws DeserializationException {
		try {
			var numberOfBytes = ByteArrayUtils.bytesPerBits(count);

			// fill buffer
			byte[] buffer = new byte[numberOfBytes];
			for (int i = 0; i < buffer.length; i++) {
				buffer[i] = stream.readByte();
			}

			// create bitset from buffer
			var bitSet = BitSet.valueOf(buffer);

			// clear bits beyond count but within bytes per bits range
			// (because the user doesn't care about these states)
			bitSet.clear(count - 1, numberOfBytes * 8);

			return bitSet;
		} catch (IOException e) {
			throw new DeserializationException("Cannot read bitset from byte stream %s at position %d"
					.formatted(ByteArrayUtils.toString(byteStream.getBuffer()), byteStream.getCurrentPos()));
		}
	}

	public int size() {
		return byteStream.getBuffer().length;
	}

	public void reset() {
		byteStream.reset();
	}

	public void reset(byte[] data) {
		byteStream.reset(data);
	}
}
