package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception;

public class CorfuDeserializationException extends CorfuException {
	public CorfuDeserializationException() {
	}

	public CorfuDeserializationException(String message) {
		super(message);
	}

	public CorfuDeserializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CorfuDeserializationException(Throwable cause) {
		super(cause);
	}
}
