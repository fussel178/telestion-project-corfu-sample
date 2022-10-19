package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.type;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Message;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;

public record PayloadRendering(
		String rendering,
		String className,
		String binaryName,
		Package pkg,
		Message config
) implements Rendering {
	public PayloadRendering(String rendering, String className, Package pkg, Message config) {
		this(rendering, className, pkg.resolve(className).binaryName(), pkg, config);
	}
}
