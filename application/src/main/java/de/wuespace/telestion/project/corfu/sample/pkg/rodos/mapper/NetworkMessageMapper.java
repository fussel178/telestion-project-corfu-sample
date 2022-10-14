package de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper;

import de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.exception.*;
import de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.message.RodosNetworkMessage;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.ByteBufferStream;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.InputStreamEmptyException;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.OutputStreamFullException;

import java.nio.ByteOrder;

/**
 * This class implements a message transformer that can map RODOS {@link RodosNetworkMessage network messages}
 * from the binary format into a {@link de.wuespace.telestion.api.message.JsonMessage JsonMessage} format and back.
 * <p>
 * Note: This class is <b>not</b> thread safe! Do <b>not</b> use the object in more than one thread.
 *
 * @author Ludwig Richter (@fussel178)
 */
public class NetworkMessageMapper {
	/**
	 * The size of the checksum in a RODOS network message header in bytes.
	 */
	public static final int CHECKSUM_SIZE = 2;

	/**
	 * The size of a RODOS network message header in bytes.
	 */
	public static final int HEADER_SIZE = 26;

	/**
	 * The maximum size of the user data in a RODOS network message in bytes.
	 */
	public static final int MAX_USER_DATA_SIZE = 1300;

	/**
	 * The minimum size of a RODOS network message in bytes.
	 */
	public static final int MIN_NETWORK_MESSAGE_SIZE = HEADER_SIZE;

	/**
	 * The maximum size of a RODOS network message in bytes.
	 */
	public static final int MAX_NETWORK_MESSAGE_SIZE = HEADER_SIZE + MAX_USER_DATA_SIZE;

	private final ByteBufferStream stream;

	public NetworkMessageMapper() {
		this.stream = new ByteBufferStream(MAX_NETWORK_MESSAGE_SIZE);
	}

	public NetworkMessageMapper(ByteOrder order) {
		this.stream = new ByteBufferStream(MAX_NETWORK_MESSAGE_SIZE, order);
	}

	/**
	 * Serializes a RODOS gateway message into its binary representation ready for transmission to a RODOS node.
	 *
	 * @param message the RODOS network message
	 * @return the binary representation of the network message
	 * @throws UserDataTooLargeException throws when the user data inside the RODOS network message is too large
	 *                                   to fit inside one network message
	 */
	public byte[] serialize(RodosNetworkMessage message) throws RodosSerializationException, UserDataTooLargeException {
		var userDataLength = message.userData().length;
		if (userDataLength > MAX_USER_DATA_SIZE) {
			throw new UserDataTooLargeException(("The provided user data exceeds the maximum user data size in the " +
					"network message. Please shrink or split the user data and try again. Got: %d, Maximum: %d")
					.formatted(userDataLength, MAX_USER_DATA_SIZE));
		}

		try {
			// write network message header
			stream.clear();
			// keep space for checksum
			stream.writeSignedShort((short) 0);
			stream.writeSignedInteger(message.senderNode());
			stream.writeSignedLong(message.sentTime());
			stream.writeUnsignedInteger(message.senderThreadId());
			stream.writeUnsignedInteger(message.topicId());
			stream.writeSignedShort(message.maxStepsToForward());
			stream.writeUnsignedShort(userDataLength);

			// write entire user data
			stream.writeSignedBytes(message.userData());

			// extract all data with no checksum
			var size = stream.position();
			stream.reset();
			byte[] serialized = new byte[size];
			stream.readSignedBytes(serialized);

			// calculate RODOS like checksum and insert it in stream to keep byte order
			var checkSum = RodosCheckSum.calculateCheckSum(serialized, 2, serialized.length - 2);
			stream.reset();
			stream.writeSignedShort(checkSum);

			// extract all data again with correct checksum
			stream.reset();
			stream.readSignedBytes(serialized, 0, 2);

			return serialized;
		} catch (OutputStreamFullException e) {
			throw new RodosSerializationException("The output stream is full. Is the network message too large?", e);
		} catch (InputStreamEmptyException e) {
			throw new RodosSerializationException("Sorry for the inconvenience. This should not happen. Please send " +
					"this error to the maintainers.");
		}
	}

	/**
	 * Deserializes a RODOS gateway message from its binary representation into its object representation.
	 *
	 * @param data the binary representation of the network message
	 * @return the object representation of the network message
	 * @throws NetworkMessageTooSmallException throws when the given data packet is too small for a network message
	 * @throws NetworkMessageTooLargeException throws when the given data packet is too large for a network message
	 * @throws InvalidCheckSumException        throws when the send checksum and the calculated one of the network
	 *                                         message do not match
	 * @throws UserDataTooSmallException       throws when the user data inside the network message is too small
	 * @throws UserDataTooLargeException       throws when the user data inside the network message is too large
	 * @throws UserDataMissingException        throws when the header announce more user data than are available
	 */
	public RodosNetworkMessage deserialize(byte[] data) throws NetworkMessageTooSmallException,
			NetworkMessageTooLargeException, RodosDeserializationException {

		// boundary checks
		if (data.length < MIN_NETWORK_MESSAGE_SIZE) {
			throw new NetworkMessageTooSmallException(("The provided data is to small for a network message. " +
					"Please manually check the provided data and try again. Got: %d, Minimum: %d")
					.formatted(data.length, MIN_NETWORK_MESSAGE_SIZE));
		}

		if (data.length > MAX_NETWORK_MESSAGE_SIZE) {
			throw new NetworkMessageTooLargeException(("The provided data is to large for a network message. " +
					"Please manually check the provided data and try again. Got: %d, Maximum: %d")
					.formatted(data.length, MAX_NETWORK_MESSAGE_SIZE));
		}

		try {
			stream.clear();
			stream.writeSignedBytes(data);
			stream.reset();

			// we don't use unsigned short here because the checksum calculation relies on the 16-bits of the short datatype
			short sentCheckSum = stream.readSignedShort();
			// skip 2 bytes because we don't want to calculate with the send checksum
			short calculatedCheckSum = RodosCheckSum.calculateCheckSum(data, 2, data.length - 2);

			if (sentCheckSum != calculatedCheckSum) {
				throw new InvalidCheckSumException(("The sent checksum does not match with the calculated checksum. Was " +
						"the data manipulated during transmission? Got: 0x%04X, Calculated: 0x%04X")
						.formatted(sentCheckSum, calculatedCheckSum));
			}

			int senderNode = stream.readSignedInteger();
			long sentTime = stream.readSignedLong();
			long senderThreadId = stream.readUnsignedInteger();
			long topicId = stream.readUnsignedInteger();
			short maxStepsToForward = stream.readSignedShort();
			int userDataLength = stream.readUnsignedShort();

			if (userDataLength < 0) {
				throw new UserDataTooSmallException("The user data is too small. Got: %d, Minimum: %d".formatted(
						userDataLength, 0
				));
			}

			if (userDataLength > MAX_USER_DATA_SIZE) {
				throw new UserDataTooLargeException(("The user data is too large. Please shrink or split the user data " +
						"and try again. Got: %d, Maximum: %d").formatted(userDataLength, MAX_USER_DATA_SIZE));
			}

			if (userDataLength > data.length - HEADER_SIZE) {
				throw new UserDataMissingException(("The user data are incomplete. Did you miss to send some parts? " +
						"Header provided size: %d, actual size: %d").formatted(userDataLength, data.length - HEADER_SIZE));
			}

			byte[] userData = new byte[userDataLength];
			stream.readSignedBytes(userData);

			return new RodosNetworkMessage(senderNode, sentTime, senderThreadId, topicId, maxStepsToForward, userData);
		} catch (InputStreamEmptyException | OutputStreamFullException e) {
			throw new RodosDeserializationException("Sorry for the inconvenience. This should not happen. Please " +
					"send this error to the maintainers.");
		}
	}
}
