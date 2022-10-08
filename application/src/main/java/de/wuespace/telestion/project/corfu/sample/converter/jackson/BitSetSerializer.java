package de.wuespace.telestion.project.corfu.sample.converter.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.BitSet;

public class BitSetSerializer extends StdSerializer<BitSet> {

	public BitSetSerializer() {
		this(null);
	}

	public BitSetSerializer(Class<BitSet> targetType) {
		super(targetType);
	}

	@Override
	public void serialize(BitSet bitSet, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartArray();
		for (var word : bitSet.toLongArray()) {
			gen.writeNumber(word);
		}
		gen.writeEndArray();
	}
}
