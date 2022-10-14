package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception;

public class PacketTooLargeException extends CorfuDeserializationException {
	public PacketTooLargeException() {
		super();
	}

	public PacketTooLargeException(String message) {
		super(message);
	}

	public PacketTooLargeException(String message, Throwable cause) {
		super(message, cause);
	}

	public PacketTooLargeException(Throwable cause) {
		super(cause);
	}
}
