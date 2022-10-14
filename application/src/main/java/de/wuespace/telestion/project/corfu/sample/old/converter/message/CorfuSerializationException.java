package de.wuespace.telestion.project.corfu.sample.old.converter.message;

public class CorfuSerializationException extends RuntimeException {
	public CorfuSerializationException() {
		super();
	}

	public CorfuSerializationException(String message) {
		super(message);
	}

	public CorfuSerializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CorfuSerializationException(Throwable cause) {
		super(cause);
	}
}
