package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.exception;

/**
 * Gets thrown, when the file type is unknown.
 *
 * @author Ludwig Richter (@fussel178)
 */
public class UnknownFileTypeException extends RuntimeException {
	public UnknownFileTypeException() {
		super();
	}

	public UnknownFileTypeException(String message) {
		super(message);
	}

	public UnknownFileTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnknownFileTypeException(Throwable cause) {
		super(cause);
	}
}
