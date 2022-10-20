package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store;

/**
 * A message registrar puts new message types into a {@link MessageTypeStore} when called.
 *
 * @author Ludwig Richter (@fussel178)
 */
public interface MessageTypeRegistrar {
	/**
	 * Gets called, when the registrar should register the Corfu components on the message type store.
	 * @param store the message type store that should receive the Corfu components
	 */
	void onRegister(MessageTypeStore store);
}
