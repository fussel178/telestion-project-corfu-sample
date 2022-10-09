package de.wuespace.telestion.project.corfu.sample.verticle.converter;

import de.wuespace.telestion.project.corfu.sample.converter.serializer.MessageSerializer;

public class GlobalSerializer {
	private static final MessageSerializer serializer = new MessageSerializer(GlobalStore.store());

	public static MessageSerializer serializer() {
		return serializer;
	}

	private GlobalSerializer() {
	}
}
