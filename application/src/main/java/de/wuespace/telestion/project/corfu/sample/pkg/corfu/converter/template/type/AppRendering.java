package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.type;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.AppConfiguration;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;

public record AppRendering(
		String rendering,
		String className,
		String binaryName,
		Package pkg,
		AppConfiguration config
) implements Rendering {
	public AppRendering(String rendering, String className, Package pkg, AppConfiguration config) {
		this(rendering, className, pkg.resolve(className).binaryName(), pkg, config);
	}
}
