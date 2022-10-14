package de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.exception;

public class NetworkMessageTooLargeException extends RodosException {
	public NetworkMessageTooLargeException() {
	}

	public NetworkMessageTooLargeException(String message) {
		super(message);
	}

	public NetworkMessageTooLargeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NetworkMessageTooLargeException(Throwable cause) {
		super(cause);
	}
}
