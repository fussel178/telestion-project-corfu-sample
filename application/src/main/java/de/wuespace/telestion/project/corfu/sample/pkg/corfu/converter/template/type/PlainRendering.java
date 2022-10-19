package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.type;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;

public record PlainRendering(
		String rendering,
		String className,
		String binaryName,
		Package pkg
) implements Rendering {
	public PlainRendering(String rendering, String className, Package pkg) {
		this(rendering, className, pkg.resolve(className).binaryName(), pkg);
	}
}
