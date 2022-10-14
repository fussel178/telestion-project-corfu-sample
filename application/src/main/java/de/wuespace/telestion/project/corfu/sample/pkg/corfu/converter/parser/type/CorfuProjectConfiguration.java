package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser.type;

import java.util.Collections;
import java.util.List;

/**
 * Represents an entire Corfu project and its configurations for apps and nodes.
 *
 * @author Ludwig Richter (@fussel178)
 */
public class CorfuProjectConfiguration {
	public CorfuProjectConfiguration() {
		// default values
		this(Collections.emptyList(), Collections.emptyList());
	}

	public CorfuProjectConfiguration(List<AppConfiguration> apps, List<NodeConfiguration> nodes) {
		this.apps = apps;
		this.nodes = nodes;
	}

	/**
	 * A list of apps that the Corfu project provides.
	 */
	public List<AppConfiguration> apps;

	/**
	 * A list of nodes that the Corfu project provides.
	 */
	public List<NodeConfiguration> nodes;

	@Override
	public String toString() {
		return "CorfuProjectConfiguration{" + "apps=" + apps + ", nodes=" + nodes + '}';
	}
}
