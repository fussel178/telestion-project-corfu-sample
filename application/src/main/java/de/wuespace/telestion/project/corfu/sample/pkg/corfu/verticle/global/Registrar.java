package de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle.global;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeStore;

public interface Registrar {
	/**
	 * Gets called, when the registrar should register the Corfu components on the message type store.
	 * @param store the message type store that should receive the Corfu components
	 */
	void onRegister(MessageTypeStore store);
}