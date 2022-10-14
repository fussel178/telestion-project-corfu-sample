package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser.type;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class MessageFields {

	public MessageFields() {
		// default values
		this.content = new HashMap<>();
	}

	public Map<String, MessageType> content;

	@JsonAnySetter
	public void addField(String key, MessageType value) {
		content.put(key, value);
	}

	public boolean isEmpty() {
		return content.isEmpty();
	}

	@Override
	public String toString() {
		return content.toString();
	}
}
