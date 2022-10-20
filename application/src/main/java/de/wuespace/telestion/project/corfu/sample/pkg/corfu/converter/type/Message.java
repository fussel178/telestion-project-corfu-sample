package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message extends Identifiable {

	private AppConfiguration associatedApp;

	public Message() {
		// default values
		this.fields = new MessageFields();
	}

	@JsonProperty("fields")
	public MessageFields fields;

	public boolean hasFields() {
		return Objects.nonNull(fields);
	}

	public void setAssociatedApp(AppConfiguration associatedApp) {
		this.associatedApp = associatedApp;
	}

	public AppConfiguration getAssociatedApp() {
		return associatedApp;
	}

	public void finalizeConfig() {
		if (Objects.isNull(fields)) {
			fields = new MessageFields();
		}

		fields.setName(name.raw());
		fields.setAssociatedApp(associatedApp);
	}

	public void resolveType(Map<String, TypeReference> references) {
		fields.resolveType(references);
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
