package de.wuespace.telestion.project.corfu.sample;

import de.wuespace.telestion.project.corfu.sample.old.converter.message.serializer.CorfuMessageSerializer;
import de.wuespace.telestion.project.corfu.sample.old.message.CorfuListMessage;
import de.wuespace.telestion.project.corfu.sample.old.message.CorfuSubMessage;
import de.wuespace.telestion.project.corfu.sample.old.message.MyCorfuMessage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Test {
	public static void main(String[] args) {

		List<CorfuListMessage> listMessage = new ArrayList<>();

		listMessage.add(new CorfuListMessage(1));
		listMessage.add(new CorfuListMessage(2));
		listMessage.add(new CorfuListMessage(3));
		listMessage.add(new CorfuListMessage(4));
		listMessage.add(new CorfuListMessage(5));
		listMessage.add(new CorfuListMessage(6));
		listMessage.add(new CorfuListMessage(7));
		listMessage.add(new CorfuListMessage(8));
		listMessage.add(new CorfuListMessage(9));
		listMessage.add(new CorfuListMessage(10));

		var myMessage = new MyCorfuMessage(10, 18.5f, true, 3.141d, 235774573547934L,
				new CorfuSubMessage(5), listMessage);

		System.out.printf("My Corfu Message: %s%n", myMessage);

		ByteArrayOutputStream byteStream = null;
		DataOutputStream dataStream = null;
		byte[] byteArray = null;

		try {
			byteStream = new ByteArrayOutputStream();
			dataStream = new DataOutputStream(byteStream);

			CorfuMessageSerializer.serialize(dataStream, myMessage);

			byteArray = byteStream.toByteArray();
		} catch (IOException e) {
			System.err.println("IO exception during serialization");
		} finally {
			if (dataStream != null) {
				try {
					dataStream.close();
				} catch (IOException e) {
					System.err.println("Cannot close data stream");
				}
			}

			if (byteStream != null) {
				try {
					byteStream.close();
				} catch (IOException e) {
					System.err.println("Cannot close byte stream");
				}
			}
		}

		if (Objects.nonNull(byteArray)) {
			System.out.printf("Serialized data: %s%n", printByteArray(byteArray));
			System.out.printf("Message size: %d bytes, %d bits%n", byteArray.length, byteArray.length * 8);
		}
	}

	public static String printByteArray(byte[] data) {
		if (Objects.isNull(data)) return "null";

		int iMax = data.length - 1;
		if (iMax < 0) {
			return "[]";
		}

		var sb = new StringBuilder();
		for (int i = 0; ; i++) {
			sb.append(String.format("%02X", data[i]));
			if (i >= iMax) {
				return sb.toString();
			}

			sb.append(" ");
		}
	}
}
