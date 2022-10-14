package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper;

import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.ByteBufferStream;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.InputStreamEmptyException;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.OutputStreamFullException;
import de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.RodosCheckSum;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.*;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuTelecommand;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuTelemetry;

import java.nio.ByteOrder;
import java.util.Objects;

/**
 * This class implements a message transformer that can map {@link CorfuTelemetry} and {@link CorfuTelecommand}
 * from their binary format into a {@link de.wuespace.telestion.api.message.JsonMessage JsonMessage} format and back.
 * <p>
 * Note: This class is <b>not</b> thread safe! Do <b>not</b> use the object in more than one thread.
 *
 * @author Ludwig Richter (@fussel178)
 */
public class CorfuMessageMapper {

	/**
	 * The size of a Corfu message checksum in bytes.
	 */
	public static final int CHECKSUM_SIZE = 2;

	/**
	 * The maximum size of a telemetry payload in bytes.
	 */
	public static final int MAX_TELEMETRY_PAYLOAD_SIZE = 128;

	/**
	 * The maximum size of a telecommand payload in bytes.
	 */
	public static final int MAX_TELECOMMAND_PAYLOAD_SIZE = 50;

	/**
	 * The size of a telemetry header <b>without</b> checksum in bytes.
	 */
	public static final int TELEMETRY_HEADER_SIZE = 25;

	/**
	 * The size of a telecommand header <b>without</b> checksum in bytes.
	 */
	public static final int TELECOMMAND_HEADER_SIZE = 15;

	/**
	 * The maximum size of an entire telemetry message in bytes.
	 */
	public static final int MAX_TELEMETRY_SIZE = CHECKSUM_SIZE + TELEMETRY_HEADER_SIZE + MAX_TELEMETRY_PAYLOAD_SIZE;

	/**
	 * The maximum size of an entire telecommand message in bytes.
	 */
	public static final int MAX_TELECOMMAND_SIZE = CHECKSUM_SIZE + TELECOMMAND_HEADER_SIZE + MAX_TELECOMMAND_PAYLOAD_SIZE;

	/**
	 * The maximum size of an entire corfu message in bytes.
	 */
	public static final int MAX_MESSAGE_SIZE = Math.max(MAX_TELEMETRY_SIZE, MAX_TELECOMMAND_SIZE);

	private final CorfuNodeMapper nodeMapper;
	private final CorfuAppMapper appMapper;
	private final CorfuPayloadMapper payloadMapper;
	private final ByteBufferStream stream;

	public CorfuMessageMapper(MessageTypeStore store) {
		this(store, new ByteBufferStream(MAX_MESSAGE_SIZE));
	}

	public CorfuMessageMapper(MessageTypeStore store, ByteOrder order) {
		this(store, new ByteBufferStream(MAX_MESSAGE_SIZE, order));
	}

	private CorfuMessageMapper(MessageTypeStore store, ByteBufferStream stream) {
		this.stream = stream;

		// component mappers
		this.nodeMapper = new CorfuNodeMapper(store);
		this.appMapper = new CorfuAppMapper(store);
		this.payloadMapper = new CorfuPayloadMapper(store);
	}

	public CorfuNodeMapper getNodeMapper() {
		return nodeMapper;
	}

	public CorfuAppMapper getAppMapper() {
		return appMapper;
	}

	public CorfuPayloadMapper getPayloadMapper() {
		return payloadMapper;
	}

	public byte[] serialize(CorfuTelecommand telecommand) throws CorfuSerializationException {
		if (Objects.isNull(telecommand)) {
			throw new IllegalArgumentException("Telecommand cannot be null. " +
					"Please provide a valid telecommand message and try again");
		}

		try {
			stream.clear();

			// write telecommand header
			// keep space for checksum
			stream.writeSignedShort((short) 0);
			// global begin
			stream.writeUnsignedShort(telecommand.commandIndex());
			stream.writeUnsignedByte(telecommand.sequenceCounter());
			// node
			nodeMapper.serialize(stream, telecommand.node());
			// app
			appMapper.serialize(stream, telecommand.app());
			// global end
			stream.writeSignedLong(telecommand.timeToExecute());
			// payload
			payloadMapper.serialize(stream, telecommand.app().payload());

			// extract all data with no checksum
			var size = stream.position();
			stream.reset();
			byte[] serialized = new byte[MAX_TELECOMMAND_SIZE];
			stream.readSignedBytes(serialized);

			// calculate RODOS like checksum and insert it in stream to keep byte order
			var checkSum = RodosCheckSum.calculateCheckSum(serialized, 2, serialized.length - 2);
			stream.reset();
			stream.writeSignedShort(checkSum);

			// extract all data again with correct checksum
			stream.reset();
			stream.readSignedBytes(serialized);

			return serialized;
		} catch (OutputStreamFullException | InputStreamEmptyException e) {
			throw new CorfuSerializationException("Sorry for the inconvenience. This should not happen. Please send " +
					"this error to the maintainers.");
		}
	}

	public CorfuTelemetry deserialize(byte[] data)
			throws PacketTooSmallException, PacketTooLargeException, InvalidCheckSumException, CorfuDeserializationException,
			InvalidPayloadLengthException, PayloadTooSmallException, PayloadTooLargeException, PayloadMissingException {
		if (Objects.isNull(data)) {
			throw new IllegalArgumentException("Byte packet cannot be null. " +
					"Please provide a valid data packet in byte array format and try again");
		}

		if (data.length < TELEMETRY_HEADER_SIZE + CHECKSUM_SIZE) {
			throw new PacketTooSmallException("The received packet is %d bytes to small"
					.formatted(TELEMETRY_HEADER_SIZE + CHECKSUM_SIZE - data.length));
		}

		if (data.length > MAX_TELEMETRY_SIZE) {
			throw new PacketTooLargeException("The received packet is %d bytes to large"
					.formatted(data.length - MAX_TELEMETRY_SIZE));
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
				throw new InvalidCheckSumException(("The sent checksum does not match with the calculated checksum. " +
						"Was the data manipulated during transmission? Got: 0x%04X, Calculated: 0x%04X")
						.formatted(sentCheckSum, calculatedCheckSum));
			}

			// unpack header
			int packetNumber = stream.readUnsignedShort();
			long timeUTC = stream.readSignedLong();
			long uptime = stream.readSignedLong();
			short nodeId = stream.readUnsignedByte();
			short hardwareId = stream.readUnsignedByte();
			short appId = stream.readUnsignedByte();
			short payloadId = stream.readUnsignedByte();
			int payloadLength = stream.readUnsignedShort();
			boolean isHistoric = stream.readBoolean();

			if (payloadLength < 0) {
				throw new PayloadTooSmallException("The payload is too small. Got: %d, Minimum: %d"
						.formatted(payloadLength, 0));
			}

			if (payloadLength > MAX_TELEMETRY_PAYLOAD_SIZE) {
				throw new PayloadTooLargeException(("The payload is too large. Please shrink of split the payload " +
						"and try again. Got: %d, Maximum: %d").formatted(payloadLength, MAX_TELEMETRY_PAYLOAD_SIZE));
			}

			if (payloadLength > data.length - TELEMETRY_HEADER_SIZE) {
				throw new PayloadMissingException(("The payload data is incomplete. Did you miss to send some parts? " +
						"Header provided size: %d, Actual size: %d")
						.formatted(payloadLength, data.length - TELEMETRY_HEADER_SIZE));
			}

			var payload = payloadMapper.deserialize(stream, appId, payloadId);
			var app = appMapper.deserialize(appId, payload);
			var node = nodeMapper.deserialize(nodeId, hardwareId);

			return new CorfuTelemetry(packetNumber, timeUTC, uptime, isHistoric, node, app);
		} catch (InputStreamEmptyException | OutputStreamFullException e) {
			throw new CorfuDeserializationException("Sorry for the inconvenience. This should not happen. Please " +
					"send this error to the maintainers.");
		}
	}
}
