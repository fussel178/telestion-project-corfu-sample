package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception;

public class PacketTooSmallException extends CorfuDeserializationException {
	public PacketTooSmallException() {
		super();
	}

	public PacketTooSmallException(String message) {
		super(message);
	}

	public PacketTooSmallException(String message, Throwable cause) {
		super(message, cause);
	}

	public PacketTooSmallException(Throwable cause) {
		super(cause);
	}
}
