package de.wuespace.telestion.project.corfu.sample.converter.exception;

public class InvalidPayloadLengthException extends Exception {
	public InvalidPayloadLengthException() {
		super();
	}

	public InvalidPayloadLengthException(String message) {
		super(message);
	}

	public InvalidPayloadLengthException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPayloadLengthException(Throwable cause) {
		super(cause);
	}
}
