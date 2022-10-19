package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

import java.nio.file.Path;

public class Package {
	public static Package resolve(Package parentPackage, Package newPackage) {
		return new Package("%s.%s".formatted(parentPackage.binaryName(), newPackage.binaryName()));
	}

	private final String binaryName;

	private final Path path;

	public Package(String binaryName) {
		this.binaryName = binaryName;
		this.path = Path.of(binaryName.replaceAll("\\.", "/"));
	}

	public Package(Path path) {
		if (path.isAbsolute()) {
			throw new IllegalArgumentException(("The path %s is absolute and therefore not applicable for a " +
					"package path. Please provide a relative path and try again.").formatted(path));
		}

		this.path = path;
		this.binaryName = path.toString().replaceAll("/", ".");
	}

	public String binaryName() {
		return binaryName;
	}

	public Path path() {
		return path;
	}

	public Package resolve(Package newPackage) {
		return resolve(this, newPackage);
	}

	public Package resolve(String binaryName) {
		return resolve(new Package(binaryName));
	}

	@Override
	public String toString() {
		return "Package{" +
				"binaryName='" + binaryName + '\'' +
				", path=" + path +
				'}';
	}
}
