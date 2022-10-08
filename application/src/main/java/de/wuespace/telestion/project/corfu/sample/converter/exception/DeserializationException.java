package de.wuespace.telestion.project.corfu.sample.converter.exception;

import java.io.IOException;

public class DeserializationException extends IOException {
	public DeserializationException() {
		super();
	}

	public DeserializationException(String message) {
		super(message);
	}

	public DeserializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeserializationException(Throwable cause) {
		super(cause);
	}
}
