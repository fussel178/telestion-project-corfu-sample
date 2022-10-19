package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.type;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.NodeConfiguration;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;

public record NodeRendering(
		String rendering,
		String className,
		String binaryName,
		Package pkg,
		NodeConfiguration config
) implements Rendering {
	public NodeRendering(String rendering, String className, Package pkg, NodeConfiguration config) {
		this(rendering, className, pkg.resolve(className).binaryName(), pkg, config);
	}
}
