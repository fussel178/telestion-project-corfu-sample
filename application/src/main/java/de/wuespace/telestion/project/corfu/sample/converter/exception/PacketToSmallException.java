package de.wuespace.telestion.project.corfu.sample.converter.exception;

public class PacketToSmallException extends Exception {
	public PacketToSmallException() {
		super();
	}

	public PacketToSmallException(String message) {
		super(message);
	}

	public PacketToSmallException(String message, Throwable cause) {
		super(message, cause);
	}

	public PacketToSmallException(Throwable cause) {
		super(cause);
	}
}
