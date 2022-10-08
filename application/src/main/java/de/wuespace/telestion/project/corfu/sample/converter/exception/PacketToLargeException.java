package de.wuespace.telestion.project.corfu.sample.converter.exception;

public class PacketToLargeException extends Exception {
	public PacketToLargeException() {
		super();
	}

	public PacketToLargeException(String message) {
		super(message);
	}

	public PacketToLargeException(String message, Throwable cause) {
		super(message, cause);
	}

	public PacketToLargeException(Throwable cause) {
		super(cause);
	}
}
