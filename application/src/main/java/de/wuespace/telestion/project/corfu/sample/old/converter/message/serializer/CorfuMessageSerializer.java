package de.wuespace.telestion.project.corfu.sample.old.converter.message.serializer;

import de.wuespace.telestion.project.corfu.sample.old.converter.message.CorfuMessage;
import de.wuespace.telestion.project.corfu.sample.old.converter.message.CorfuSerializationException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class CorfuMessageSerializer {
	public static <T> T deserialize(DataInputStream stream) throws IOException {
		throw new RuntimeException("Not implemented");
	}

	public static void serialize(DataOutputStream stream, CorfuMessage message) throws IOException {
		if (Objects.isNull(message)) {
			throw new CorfuSerializationException("The corfu message is not defined");
		}

		var corfuFields = CorfuFieldUtils.getCorfuFields(message.getClass());

		for (CorfuField field : corfuFields) {
			switch (field.type()) {
				case BOOLEAN -> stream.writeBoolean(field.cast(message, Boolean.class));
				case FLOAT -> stream.writeFloat(field.cast(message, Float.class));
				case DOUBLE -> stream.writeDouble(field.cast(message, Double.class));
				case INT8 -> stream.writeByte(field.cast(message, Byte.class));
				case INT16 -> stream.writeShort(field.cast(message, Short.class));
				case INT32 -> stream.writeInt(field.cast(message, Integer.class));
				case INT64 -> stream.writeLong(field.cast(message, Long.class));
				case UINT8 -> stream.writeByte(field.cast(message, Short.class));
				case UINT16 -> stream.writeShort(field.cast(message, Integer.class));
				case UINT32 -> stream.writeInt(field.cast(message, Long.class).intValue());
				case UINT64 -> stream.writeLong(field.cast(message, BigInteger.class).longValue());
				case OBJECT -> {
					if (field.isCorfuMessage()) {
						serialize(stream, field.cast(message, CorfuMessage.class));
					} else if (field.isList()) {
						var list = field.cast(message, List.class);
						for (Object item : list) {
							serialize(stream, CorfuMessage.cast(item));
						}
					}
				}
				default -> throw new IllegalStateException("Unknown field type encountered. " +
						"Sorry for the inconvenience. Please provide a bug report to the developers.");
			}
		}
	}
}
