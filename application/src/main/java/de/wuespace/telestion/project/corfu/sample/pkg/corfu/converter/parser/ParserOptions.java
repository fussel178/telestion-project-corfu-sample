package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ParserOptions {

	private final List<String> ignoredNodes;
	private final List<String> ignoredApps;

	public ParserOptions() {
		this.ignoredNodes = new ArrayList<>();
		this.ignoredApps = new ArrayList<>();
	}

	public List<String> getIgnoredNodes() {
		return ignoredNodes;
	}

	public List<String> getIgnoredApps() {
		return ignoredApps;
	}

	public ParserOptions addIgnoredNode(String nodeName) {
		ignoredNodes.add(nodeName);
		return this;
	}

	public ParserOptions addIgnoredApp(String appName) {
		ignoredApps.add(appName);
		return this;
	}

	public ParserOptions addIgnoredNodes(Collection<String> nodeNames) {
		ignoredNodes.addAll(nodeNames);
		return this;
	}

	public ParserOptions addIgnoredApps(Collection<String> appNames) {
		ignoredApps.addAll(appNames);
		return this;
	}

	public ParserOptions setIgnoredNodes(Collection<String> nodeNames) {
		ignoredNodes.clear();
		ignoredNodes.addAll(nodeNames);
		return this;
	}

	public ParserOptions setIgnoredApps(Collection<String> appNames) {
		ignoredApps.clear();
		ignoredApps.addAll(appNames);
		return this;
	}
}
