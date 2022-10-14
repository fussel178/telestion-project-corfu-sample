package de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.exception;

public class UserDataTooSmallException extends RodosDeserializationException {
	public UserDataTooSmallException() {
		super();
	}

	public UserDataTooSmallException(String message) {
		super(message);
	}

	public UserDataTooSmallException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserDataTooSmallException(Throwable cause) {
		super(cause);
	}
}
