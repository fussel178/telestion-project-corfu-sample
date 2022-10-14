package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception;

public class InvalidPayloadLengthException extends CorfuDeserializationException {
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
