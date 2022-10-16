package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.fs;

import java.io.IOException;
import java.nio.file.Path;

public interface GeneratorFilesystem {
	/**
	 * Writes a new file to the given file path with the given content.
	 * Non-existent folders on the file path are generated automatically.
	 * Any existing file or folder is overridden during this operation.
	 *
	 * @param filePath the path to the new file
	 * @param content the new content the file should have
	 * @throws IOException gets thrown when errors happen during filesystem operations
	 */
	void writeFile(Path filePath, String content) throws IOException;
}
