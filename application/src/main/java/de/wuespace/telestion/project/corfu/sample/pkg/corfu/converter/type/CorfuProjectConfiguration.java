package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

import java.util.Collections;
import java.util.List;

/**
 * Represents an entire Corfu project and its configurations for apps and nodes.
 *
 * @author Ludwig Richter (@fussel178)
 */
@SuppressWarnings("unused")
public class CorfuProjectConfiguration {
	public CorfuProjectConfiguration() {
		// default values
		this(null, Collections.emptyList(), Collections.emptyList());
	}

	public CorfuProjectConfiguration(
			ProjectConfiguration project,
			List<AppConfiguration> apps,
			List<NodeConfiguration> nodes
	) {
		this.project = project;
		this.apps = apps;
		this.nodes = nodes;
	}

	/**
	 * The general project configuration.
	 */
	public ProjectConfiguration project;

	/**
	 * A list of apps that the Corfu project provides.
	 */
	public List<AppConfiguration> apps;

	/**
	 * A list of nodes that the Corfu project provides.
	 */
	public List<NodeConfiguration> nodes;

	public AppConfiguration getStandardTelemetryApp() {
		return apps.stream().filter(AppConfiguration::generatesStandardTelemetry).findFirst().orElseThrow();
	}

	@Override
	public String toString() {
		return "CorfuProjectConfiguration{" +
				"project=" + project +
				", apps=" + apps +
				", nodes=" + nodes +
				'}';
	}
}
