package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message extends Identifiable {

	public Message() {
		// default values
		this.fields = new MessageFields();
	}

	@JsonProperty
	public MessageFields fields;

	public boolean hasFields() {
		return Objects.nonNull(fields);
	}

	@Override
	public String toString() {
		return "Message{" + "id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", fields=" + fields +
				'}';
	}
}
