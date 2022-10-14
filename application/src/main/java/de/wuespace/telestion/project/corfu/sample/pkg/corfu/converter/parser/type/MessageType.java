package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser.type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = MessageTypeDeserializer.class)
public class MessageType {
	public enum Type {
		SIMPLE,
		BIT_ARRAY,
		ARRAY
	}

	public MessageType(Type type, String value, int count) {
		this.type = type;
		this.value = value;
		this.count = count;
	}

	public Type type;

	public String value;

	public int count;

	@Override
	public String toString() {
		return "MessageType{" + "type=" + type + ", value='" + value + '\'' + ", count=" + count + '}';
	}
}
