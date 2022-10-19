package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.type;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.MessageFields;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;

public record StructRendering(
		String rendering,
		String className,
		String binaryName,
		Package pkg,
		MessageFields config
) implements Rendering {
	public StructRendering(String rendering, String className, Package pkg, MessageFields config) {
		this(rendering, className, pkg.resolve(className).binaryName(), pkg, config);
	}
}
