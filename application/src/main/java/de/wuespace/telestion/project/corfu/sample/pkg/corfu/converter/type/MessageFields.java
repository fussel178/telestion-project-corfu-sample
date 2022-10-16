package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class MessageFields {

	private ComponentName name;

	private AppConfiguration associatedApp;

	public MessageFields() {
		// default values
		this.content = new ArrayList<>();
	}

	public List<MessageType> content;

	public void setName(String raw) {
		this.name = new ComponentName(raw);
	}

	public ComponentName getName() {
		return name;
	}

	public void setAssociatedApp(AppConfiguration associatedApp) {
		this.associatedApp = associatedApp;
	}

	public AppConfiguration getAssociatedApp() {
		return associatedApp;
	}

	@JsonAnySetter
	public void addField(String key, MessageType value) {
		value.setName(key);
		content.add(value);
	}

	public void resolveType(Map<String, TypeReference> references) {
		content.forEach(type -> type.resolveType(references));
	}

	public boolean isEmpty() {
		return content.isEmpty();
	}

	@Override
	public String toString() {
		return content.toString();
	}
}
