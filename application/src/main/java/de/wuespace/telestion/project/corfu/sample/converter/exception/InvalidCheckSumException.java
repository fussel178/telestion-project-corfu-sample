package de.wuespace.telestion.project.corfu.sample.converter.exception;

public class InvalidCheckSumException extends Exception {
	public InvalidCheckSumException() {
		super();
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
