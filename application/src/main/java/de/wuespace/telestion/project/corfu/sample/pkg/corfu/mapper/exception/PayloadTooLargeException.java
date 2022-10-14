package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception;

public class PayloadTooLargeException extends CorfuDeserializationException {
	public PayloadTooLargeException() {
	}

	public PayloadTooLargeException(String message) {
		super(message);
	}

	public PayloadTooLargeException(String message, Throwable cause) {
		super(message, cause);
	}

	public PayloadTooLargeException(Throwable cause) {
		super(cause);
	}
}
