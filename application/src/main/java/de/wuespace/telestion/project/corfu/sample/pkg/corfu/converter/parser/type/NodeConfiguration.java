package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeConfiguration extends Identifiable {

	public NodeConfiguration() {
		// default values
		this.hardwareTargets = Collections.emptyMap();
		this.apps = Collections.emptyMap();
	}

	@JsonProperty
	public Map<String, Short> hardwareTargets;

	@JsonProperty
	public Map<String, JsonNode> apps;

	@Override
	public String toString() {
		return "NodeConfiguration{" + "apps=" + apps +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", id=" + id +
				'}';
	}
}
