package de.wuespace.telestion.project.corfu.sample;

import de.wuespace.telestion.project.corfu.sample.converter.util.RODOSCheckSum;

public class TestCheckSum {
	public static void main(String[] args) {
		byte[] data = new byte[]{
				(byte) 0b10110010,
				(byte) 0b01100011,
				(byte) 0b11101111,
				(byte) 0b00000010
		};

		short checkSum = RODOSCheckSum.calculateCheckSum(data);

		System.out.printf("Checksum: %04X%n", checkSum);
	}
}
