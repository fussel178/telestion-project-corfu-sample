package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

@JsonDeserialize(using = MessageTypeDeserializer.class)
public interface MessageType {
	void resolveType(Map<String, TypeReference> references);

	void setName(String raw);

	ComponentName getName();

	int count();

	String type();

	TypeReference reference();
}
