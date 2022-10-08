package de.wuespace.telestion.project.corfu.sample.converter.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

public class BitSetDeserializer extends StdDeserializer<BitSet> {

	public BitSetDeserializer() {
		this(null);
	}

	public BitSetDeserializer(Class<BitSet> targetType) {
		super(targetType);
	}

	@Override
	public BitSet deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
		// structure check
		if (parser.getCurrentToken() != JsonToken.START_ARRAY) return null;

		var wordList = new ArrayList<Long>();
		var token = parser.nextToken();
		while(token != JsonToken.END_ARRAY) {
			wordList.add(parser.getLongValue());
			token = parser.nextToken();
		}

		// convert ArrayList of type "Long" to primitive "long" array
		var words = new long[wordList.size()];
		for (int i = 0; i < wordList.size(); i++) {
			words[i] = wordList.get(i);
		}

		return BitSet.valueOf(words);
	}
}
