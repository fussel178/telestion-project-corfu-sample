package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeConfiguration extends Identifiable {

	public NodeConfiguration() {
		// default values
		this.hardwareTargets = new HashMap<>();
		this.apps = new HashMap<>();
	}

	@JsonProperty("hardware_ids")
	public Map<String, Short> hardwareTargets;

	@JsonProperty
	public Map<String, JsonNode> apps;

	public void finalizeConfig() {
		// add implicit broadcast hardware
		hardwareTargets.put("broadcast", (short) 0xFF);
	}

	@Override
	public String toString() {
		return "NodeConfiguration{" +
				"hardwareTargets=" + hardwareTargets +
				", apps=" + apps +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", id=" + id +
				'}';
	}
}
