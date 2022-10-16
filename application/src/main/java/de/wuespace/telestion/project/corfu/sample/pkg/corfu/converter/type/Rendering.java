package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

public record Rendering(
		String rendering,
		String className,
		String binaryName,
		Package pkg
) {
	public Rendering(String rendering, String className, Package pkg) {
		this(rendering, className, pkg.binaryName() + "." + className, pkg);
	}
}
