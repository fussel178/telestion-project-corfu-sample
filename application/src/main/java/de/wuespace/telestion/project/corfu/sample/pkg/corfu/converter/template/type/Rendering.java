package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.type;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;

public interface Rendering {
	String rendering();

	String className();

	String binaryName();

	Package pkg();
}
