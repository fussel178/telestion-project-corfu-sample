package de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.exception;

public class NetworkMessageTooSmallException extends RodosException {
	public NetworkMessageTooSmallException() {
	}

	public NetworkMessageTooSmallException(String message) {
		super(message);
	}

	public NetworkMessageTooSmallException(String message, Throwable cause) {
		super(message, cause);
	}

	public NetworkMessageTooSmallException(Throwable cause) {
		super(cause);
	}
}
