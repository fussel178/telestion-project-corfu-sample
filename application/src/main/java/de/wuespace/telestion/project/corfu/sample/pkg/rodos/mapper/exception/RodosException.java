package de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.exception;

public abstract class RodosException extends Exception {
	protected RodosException() {
	}

	protected RodosException(String message) {
		super(message);
	}

	protected RodosException(String message, Throwable cause) {
		super(message, cause);
	}

	protected RodosException(Throwable cause) {
		super(cause);
	}
}
