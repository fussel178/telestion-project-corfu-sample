package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception;

public class PayloadMissingException extends CorfuDeserializationException {
	public PayloadMissingException() {
	}

	public PayloadMissingException(String message) {
		super(message);
	}

	public PayloadMissingException(String message, Throwable cause) {
		super(message, cause);
	}

	public PayloadMissingException(Throwable cause) {
		super(cause);
	}
}
