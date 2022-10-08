package de.wuespace.telestion.project.corfu.sample.converter.serializer;

import de.wuespace.telestion.project.corfu.sample.converter.exception.*;
import de.wuespace.telestion.project.corfu.sample.converter.util.ByteArrayUtils;
import de.wuespace.telestion.project.corfu.sample.converter.util.DataInputStream;
import de.wuespace.telestion.project.corfu.sample.converter.util.DataOutputStream;
import de.wuespace.telestion.project.corfu.sample.converter.util.RODOSCheckSum;
import de.wuespace.telestion.project.corfu.sample.converter.store.MessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.converter.message.Telecommand;
import de.wuespace.telestion.project.corfu.sample.converter.message.Telemetry;

import java.util.Objects;

public class MessageSerializer {

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
	public static final int TELECOMMAND_HEADER_SIZE = 25;

	/**
	 * The maximum size of an entire telemetry message in bytes.
	 */
	public static final int MAX_TELEMETRY_SIZE = CHECKSUM_SIZE + TELEMETRY_HEADER_SIZE + MAX_TELEMETRY_PAYLOAD_SIZE;

	/**
	 * The maximum size of an entire telecommand message in bytes.
	 */
	public static final int MAX_TELECOMMAND_SIZE = CHECKSUM_SIZE + TELECOMMAND_HEADER_SIZE + MAX_TELECOMMAND_PAYLOAD_SIZE;

	private final NodeSerializer nodeSerializer;
	private final AppSerializer appSerializer;
	private final PayloadSerializer payloadSerializer;
	private final DataOutputStream outStream;
	private final DataInputStream inStream;
	private final MessageTypeStore store;

	public MessageSerializer(MessageTypeStore store) {
		this.store = store;

		// component serializers
		this.nodeSerializer = new NodeSerializer(store);
		this.appSerializer = new AppSerializer(store);
		this.payloadSerializer = new PayloadSerializer(store);

		// reusable byte streams
		this.outStream = new DataOutputStream(TELECOMMAND_HEADER_SIZE + MAX_TELECOMMAND_PAYLOAD_SIZE);
		this.inStream = new DataInputStream();
	}

	public NodeSerializer getNodeSerializer() {
		return nodeSerializer;
	}

	public AppSerializer getAppSerializer() {
		return appSerializer;
	}

	public PayloadSerializer getPayloadSerializer() {
		return payloadSerializer;
	}

	public byte[] serialize(Telecommand telecommand) throws SerializationException {
		if (Objects.isNull(telecommand)) {
			throw new IllegalArgumentException("Telecommand cannot be null. " +
					"Please provide a valid telecommand message and try again");
		}

		outStream.reset();

		// global begin
		outStream.writeUnsignedShort(telecommand.commandIndex());
		outStream.writeUnsignedByte(telecommand.sequenceCounter());
		// node
		nodeSerializer.serialize(outStream, telecommand.node());
		// app
		appSerializer.serialize(outStream, telecommand.app());
		// global end
		outStream.writeSignedLong(telecommand.timeToExecute());
		// payload
		payloadSerializer.serialize(outStream, telecommand.app().payload());
		// checksum
		var serializedData = outStream.toByteArray();
		var checkSum = RODOSCheckSum.calculateCheckSum(serializedData);
		return ByteArrayUtils.packFrontUnsignedShort(checkSum, serializedData);
	}

	public Telemetry deserialize(byte[] packet)
			throws PacketToSmallException, PacketToLargeException, InvalidCheckSumException, DeserializationException,
			InvalidPayloadLengthException {
		if (Objects.isNull(packet)) {
			throw new IllegalArgumentException("Byte packet cannot be null. " +
					"Please provide a valid data packet in byte array format and try again");
		}

		if (packet.length < TELEMETRY_HEADER_SIZE + CHECKSUM_SIZE) {
			throw new PacketToSmallException("The received packet is %d bytes to small"
					.formatted(TELEMETRY_HEADER_SIZE + CHECKSUM_SIZE - packet.length));
		}

		if (packet.length > MAX_TELEMETRY_SIZE) {
			throw new PacketToLargeException("The received packet is %d bytes to large"
					.formatted(packet.length - MAX_TELEMETRY_SIZE));
		}

		var splitData = ByteArrayUtils.splitFrontUnsignedShort(packet);
		var checkSum = RODOSCheckSum.calculateCheckSum(splitData.data());

		if (splitData.prefix() != checkSum) {
			throw new InvalidCheckSumException(("The sent checksum does not match with the calculated one. " +
					"Sent: 0x%04X, Calculated: 0x%04X").formatted(splitData.prefix(), checkSum));
		}

		inStream.reset(splitData.data());

		// unpack header
		int packetNumber = inStream.readUnsignedShort();
		long timeUTC = inStream.readSignedLong();
		long uptime = inStream.readSignedLong();
		short nodeId = inStream.readUnsignedByte();
		short hardwareId = inStream.readUnsignedByte();
		short appId = inStream.readUnsignedByte();
		short payloadId = inStream.readUnsignedByte();
		int payloadLength = inStream.readUnsignedShort();
		boolean isHistoric = inStream.readBoolean();

		// check payload length
		if (splitData.data().length != payloadLength + TELEMETRY_HEADER_SIZE) {
			throw new InvalidPayloadLengthException(("The sent payload length does not match with the size of the " +
					"received payload bytes. Sent: %d, Actual: %d").formatted(payloadLength,
					splitData.data().length - TELEMETRY_HEADER_SIZE));
		}

		var payload = payloadSerializer.deserialize(inStream, appId, payloadId);
		var app = appSerializer.deserialize(appId, payload);
		var node = nodeSerializer.deserialize(nodeId, hardwareId);

		return new Telemetry(packetNumber, timeUTC, uptime, isHistoric, node, app);
	}
}
