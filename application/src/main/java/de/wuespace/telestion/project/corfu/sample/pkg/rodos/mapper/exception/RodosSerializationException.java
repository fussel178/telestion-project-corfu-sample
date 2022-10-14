package de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.exception;

public class RodosSerializationException extends RodosException {
	public RodosSerializationException() {
	}

	public RodosSerializationException(String message) {
		super(message);
	}

	public RodosSerializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public RodosSerializationException(Throwable cause) {
		super(cause);
	}
}
