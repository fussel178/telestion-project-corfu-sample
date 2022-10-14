package de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.exception;

public class InvalidCheckSumException extends RodosDeserializationException {
	public InvalidCheckSumException() {
	}

	public InvalidCheckSumException(String message) {
		super(message);
	}

	public InvalidCheckSumException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCheckSumException(Throwable cause) {
		super(cause);
	}
}
