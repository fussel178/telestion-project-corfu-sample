package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;

public record ProjectGeneratorFilesystem(Path basePath) implements GeneratorFilesystem {
	public ProjectGeneratorFilesystem {
		if (!basePath.isAbsolute()) {
			throw new IllegalArgumentException(("The base path %s is not absolute. Give an absolute path as base " +
					"path and try again.").formatted(basePath.toString()));
		}
	}

	@Override
	public void writeFile(Path filePath, String content) throws IOException {
		if (filePath.isAbsolute()) {
			throw new IllegalArgumentException(("File path %s cannot be absolute. Please provide a relative file " +
					"path and try again.").formatted(filePath.toString()));
		}

		var fullPath = basePath.resolve(filePath);
		createDirectory(fullPath.getParent());
		// write string to file, create it, when it not exists and overwrite existing content
		Files.deleteIfExists(fullPath);
		Files.writeString(fullPath, content, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
	}

	@Override
	public void delete(Path path) throws IOException {
		if (path.isAbsolute()) {
			throw new IllegalArgumentException(("Path %s cannot be absolute. Please provide a relative file " +
					"path and try again.").formatted(path.toString()));
		}

		var fullPath = basePath.resolve(path);
		if (!Files.exists(fullPath)) {
			return;
		}

		try (var stream = Files.walk(fullPath)) {
			//noinspection ResultOfMethodCallIgnored
			stream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
		}
	}

	private void createDirectory(Path path) throws IOException {
		if (Files.isDirectory(path)) {
			return;
		}

		if (Files.exists(path) && !Files.isDirectory(path)) {
			Files.delete(path);
		}

		Files.createDirectories(path);
	}
}
