package de.wuespace.telestion.project.corfu.sample;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TestEndian {
	public static void main(String[] args) {
		byte[] encodedData = new byte[]{
				(byte) 0x93, // checksum
				(byte) 0x9e, // checksum
				(byte) 0x00, // packet number
				(byte) 0x00, // packet number
				(byte) 0xe3, // time UTC
				(byte) 0x42, // time UTC
				(byte) 0x03, // time UTC
				(byte) 0x00, // time UTC
				(byte) 0x00, // time UTC
				(byte) 0x00, // time UTC
				(byte) 0x00, // time UTC
				(byte) 0x00, // time UTC
				(byte) 0xcf, // uptime
				(byte) 0x43, // uptime
				(byte) 0x03, // uptime
				(byte) 0x00, // uptime
				(byte) 0x00, // uptime
				(byte) 0x00, // uptime
				(byte) 0x00, // uptime
				(byte) 0x00, // uptime
				(byte) 0x50, // node ID
				(byte) 0x13, // hardware ID
				(byte) 0x89, // app ID
				(byte) 0x01, // payload ID
				(byte) 0x05, // payload length
				(byte) 0x00, // payload length
				(byte) 0x01, // payload 1: uint8_t
				(byte) 0x3f, // payload 2: float
				(byte) 0x80, // payload 2: float
				(byte) 0x00, // payload 2: float
				(byte) 0x00, // payload 2: float
//				(byte) 0x00,
//				(byte) 0x00,
//				(byte) 0x00,
//				(byte) 0x00,
//				(byte) 0x00,
//				(byte) 0x00,
//				(byte) 0x00,
//				(byte) 0x00,
//				(byte) 0x00,
//				(byte) 0x00,
//				(byte) 0x00,
//				(byte) 0x00,
//				(byte) 0x00,
		};

		// capture aus WireShark
		//0020                                      |e7 2a 00 01
		//0030   f7 62 00 00 00 00 3b 9b 07 d0 63 12 02 08 00 00
		//0040   04 11 00 0a 00 9b|93 9e|00 00|e3 42 03 00 00 00
		//0050   00 00|cf 43 03 00 00 00 00 00|50|13|89|01|05 00|
		//0060   00|01|3f 80 00 00|00 00 00 00 00 00 00 00 00 00
		//0070   00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
		//0080   00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
		//0090   00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
		//00a0   00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
		//00b0   00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
		//00c0   00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
		//00d0   00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
		//00e0   00


		var buffer = ByteBuffer.wrap(encodedData);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		int checkSum = (int) (buffer.getShort() & 0xFFFF);
		int packetNumber = (int) (buffer.getShort() & 0xFFFF);
		long timeUTC = buffer.getLong();
		long uptime = buffer.getLong();
		short nodeId = (short) (buffer.get() & 0xFF);
		short hardwareId = (short) (buffer.get() & 0xFF);
		short appId = (short) (buffer.get() & 0xFF);
		short payloadId = (short) (buffer.get() & 0xFF);
		int payloadLength = (int) (buffer.getShort() & 0xFFFF);
		short payload1 = (short) (buffer.get() & 0xFF);
		buffer.order(ByteOrder.BIG_ENDIAN);
		float payload2 = buffer.getFloat();

		System.out.printf("Checksum: 0x%04X%n", checkSum);
		System.out.printf("Packet number: 0x%04X%n", packetNumber);
		System.out.printf("Time UTC: 0x%16X%n", timeUTC);
		System.out.printf("Uptime: 0x%16X%n", uptime);
		System.out.printf("Node id: 0x%02X%n", nodeId);
		System.out.printf("Hardware id: 0x%02X%n", hardwareId);
		System.out.printf("App id: 0x%02X%n", appId);
		System.out.printf("Payload id: 0x%02X%n", payloadId);
		System.out.printf("Payload length: 0x%04X%n", payloadLength);
		System.out.printf("Payload 1: %d%n", payload1);
		System.out.printf("Payload 2: %f%n", payload2);
	}
}
