package de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception;

public class InputStreamEmptyException extends Exception {
	public InputStreamEmptyException() {
	}

	public InputStreamEmptyException(String message) {
		super(message);
	}

	public InputStreamEmptyException(String message, Throwable cause) {
		super(message, cause);
	}

	public InputStreamEmptyException(Throwable cause) {
		super(cause);
	}
}
