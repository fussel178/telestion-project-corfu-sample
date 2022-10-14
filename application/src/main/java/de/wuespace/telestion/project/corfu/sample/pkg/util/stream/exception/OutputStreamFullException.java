package de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception;

public class OutputStreamFullException extends Exception {
	public OutputStreamFullException() {
	}

	public OutputStreamFullException(String message) {
		super(message);
	}

	public OutputStreamFullException(String message, Throwable cause) {
		super(message, cause);
	}

	public OutputStreamFullException(Throwable cause) {
		super(cause);
	}
}
