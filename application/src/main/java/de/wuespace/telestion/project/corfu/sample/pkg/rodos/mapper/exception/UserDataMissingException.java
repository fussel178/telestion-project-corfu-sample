package de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.exception;

/**
 * Gets thrown when the user data in a network message is incomplete.
 */
public class UserDataMissingException extends RodosDeserializationException {
	public UserDataMissingException() {
		super();
	}

	public UserDataMissingException(String message) {
		super(message);
	}

	public UserDataMissingException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserDataMissingException(Throwable cause) {
		super(cause);
	}
}
