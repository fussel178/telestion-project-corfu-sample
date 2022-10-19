package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Map;

/**
 * Represents an app configuration file in a Corfu project.
 *
 * @author Ludwig Richter (@fussel178)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppConfiguration extends Identifiable {

	public AppConfiguration() {
		// default values
		this.structs = Collections.emptyMap();
		this.telecommands = Collections.emptyMap();
		this.extendedTelemetry = Collections.emptyMap();
		this.standardTelemetry = new MessageFields();
	}

	private short standardTelemetryPayloadId;

	public void setStandardTelemetryPayloadId(short id) {
		this.standardTelemetryPayloadId = id;
	}

	public boolean generatesStandardTelemetry() {
		return standardTelemetryPayloadId > 0;
	}

	public short getStandardTelemetryPayloadId() {
		return standardTelemetryPayloadId;
	}

	/**
	 * A map of message fields and their names that represent the structs of the app.
	 */
	@JsonProperty("structs")
	public Map<String, MessageFields> structs;

	/**
	 * A map of messages and their names that represent the telecommands of the app.
	 */
	@JsonProperty("telecommands")
	public Map<String, Message> telecommands;

	/**
	 * A map of messages and their names that represent the extended telemetry of the app.
	 */
	@JsonProperty("extended_telemetry")
	public Map<String, Message> extendedTelemetry;

	/**
	 * A collection of message fields that represent the standard telemetry of the app.
	 */
	@JsonProperty("standard_telemetry")
	public MessageFields standardTelemetry;

	public void finalizeConfig() {
		// give structures their name
		structs.forEach((key, value) -> {
			value.setName(key);
			value.setAssociatedApp(this);
		});
		telecommands.forEach((key, value) -> {
			value.setName(key);
			value.setAssociatedApp(this);
			value.finalizeConfig();
		});
		extendedTelemetry.forEach((key, value) -> {
			value.setName(key);
			value.setAssociatedApp(this);
			value.finalizeConfig();
		});
		standardTelemetry.setName(name.raw());
	}

	public void referenceTypes() {
		var references = TypeReference.getReferences(structs.values());
		structs.forEach((key, value) -> value.resolveType(references));
		telecommands.forEach((key, value) -> value.resolveType(references));
		extendedTelemetry.forEach((key, value) -> value.resolveType(references));
		standardTelemetry.resolveType(references);
	}

	@Override
	public String toString() {
		return "AppConfiguration{" +
				"standardTelemetryPayloadId=" + standardTelemetryPayloadId +
				", structs=" + structs +
				", telecommands=" + telecommands +
				", extendedTelemetry=" + extendedTelemetry +
				", standardTelemetry=" + standardTelemetry +
				", name=" + name +
				", description='" + description + '\'' +
				", id=" + id +
				'}';
	}
}
