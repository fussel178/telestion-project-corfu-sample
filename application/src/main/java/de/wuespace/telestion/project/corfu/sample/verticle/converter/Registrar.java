package de.wuespace.telestion.project.corfu.sample.verticle.converter;

import de.wuespace.telestion.project.corfu.sample.converter.store.MessageTypeStore;

public interface Registrar {
	/**
	 * Gets called, when the registrar should register the Corfu components on the message type store.
	 * @param store the message type store that should receive the Corfu components
	 */
	void onRegister(MessageTypeStore store);
}
