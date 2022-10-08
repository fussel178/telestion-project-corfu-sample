package de.wuespace.telestion.project.corfu.sample.converter.util;

import de.wuespace.telestion.project.corfu.sample.converter.exception.SerializationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.BitSet;

public class DataOutputStream {
	private final ByteArrayOutputStream byteStream;
	private final java.io.DataOutputStream stream;

	public DataOutputStream(int size) {
		byteStream = new ByteArrayOutputStream(size);
		stream = new java.io.DataOutputStream(byteStream);
	}

	public DataOutputStream() {
		byteStream = new ByteArrayOutputStream();
		stream = new java.io.DataOutputStream(byteStream);
	}

	public void writeSerializedData(byte[] data) throws SerializationException {
		try {
			byteStream.write(data);
		} catch (IOException e) {
			throw new SerializationException("Cannot write serialized data %s".formatted(ByteArrayUtils.toHexString(data)));
		}
	}

	public void writeSignedByte(byte data) throws SerializationException {
		try {
			stream.writeByte(data);
		} catch (IOException e) {
			throw new SerializationException("Cannot serialize signed byte 0x%02X / %d".formatted(data, data));
		}
	}

	public void writeUnsignedByte(short data) throws SerializationException {
		if (data < 0) {
			throw new IllegalArgumentException("Unsigned byte %d cannot be negative".formatted(data));
		}

		try {
			stream.writeByte(data);
		} catch (IOException e) {
			throw new SerializationException("Cannot serialize unsigned byte 0x%02X / %d".formatted(data, data));
		}
	}

	public void writeSignedShort(short data) throws SerializationException {
		try {
			stream.writeShort(data);
		} catch (IOException e) {
			throw new SerializationException("Cannot serialize signed short 0x%04X / %d".formatted(data, data));
		}
	}

	public void writeUnsignedShort(int data) throws SerializationException {
		if (data < 0) {
			throw new IllegalArgumentException("Unsigned short %d cannot be negative".formatted(data));
		}

		try {
			stream.writeShort(data);
		} catch (IOException e) {
			throw new SerializationException("Cannot serialize unsigned short 0x%04X / %d".formatted(data, data));
		}
	}

	public void writeSignedInt(int data) throws SerializationException {
		try {
			stream.writeInt(data);
		} catch (IOException e) {
			throw new SerializationException("Cannot serialize signed integer 0x%08X / %d".formatted(data, data));
		}
	}

	public void writeUnsignedInt(long data) throws SerializationException {
		if (data < 0) {
			throw new IllegalArgumentException("Unsigned integer %d cannot be negative".formatted(data));
		}

		try {
			stream.writeInt((int) data);
		} catch (IOException e) {
			throw new SerializationException("Cannot serialize unsigned integer 0x%08X / %d".formatted(data, data));
		}
	}

	public void writeSignedLong(long data) throws SerializationException {
		try {
			stream.writeLong(data);
		} catch (IOException e) {
			throw new SerializationException("Cannot serialize signed long 0x%16X / %d".formatted(data, data));
		}
	}

	public void writeUnsignedLong(BigInteger data) throws SerializationException {
		if (data.compareTo(BigInteger.ZERO) < 0) {
			throw new IllegalArgumentException("Unsigned long %d cannot be negative".formatted(data));
		}

		try {
			stream.writeLong(data.longValue());
		} catch (IOException e) {
			throw new SerializationException("Cannot serialize unsigned long 0x%16X / %d".formatted(data.longValue(), data));
		}
	}

	public void writeFloat(float data) throws SerializationException {
		try {
			stream.writeFloat(data);
		} catch (IOException e) {
			throw new SerializationException("Cannot serialize float %f".formatted(data));
		}
	}

	public void writeDouble(double data) throws SerializationException {
		try {
			stream.writeDouble(data);
		} catch (IOException e) {
			throw new SerializationException("Cannot serialize double %f".formatted(data));
		}
	}

	public void writeBoolean(boolean data) throws SerializationException {
		try {
			stream.writeBoolean(data);
		} catch (IOException e) {
			throw new SerializationException("Cannot serialize boolean %b".formatted(data));
		}
	}

	public void writeBitSet(BitSet bitSet, int count) throws SerializationException {
		if (bitSet.length() > count) {
			throw new IllegalArgumentException(("The bitset %s has bits set outside the specified range %d. Please " +
					"unset the check that the bitset is in the annotated range and try again.").formatted(bitSet, count));
		}

		try {
			var neededByteCount = ByteArrayUtils.bytesPerBits(count);
			var bytes = bitSet.toByteArray();
			for (int i = 0; i < neededByteCount; i++) {
				if (i < bytes.length) {
					// write what's stored in the bitset
					stream.write(bytes[i]);
				} else {
					// fill up remaining space with zeros
					stream.write(0x00);
				}
			}
		} catch (IOException e) {
			throw new SerializationException("Cannot serialize bitset %s".formatted(bitSet));
		}
	}

	public int size() {
		return stream.size();
	}

	public void reset() {
		byteStream.reset();
	}

	public byte[] toByteArray() {
		return byteStream.toByteArray();
	}
}
