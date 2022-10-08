package de.wuespace.telestion.project.corfu.sample.converter.serializer;

import de.wuespace.telestion.project.corfu.sample.converter.exception.SerializationException;
import de.wuespace.telestion.project.corfu.sample.converter.store.MessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.converter.util.DataOutputStream;
import de.wuespace.telestion.project.corfu.sample.converter.message.Hardware;
import de.wuespace.telestion.project.corfu.sample.converter.message.Node;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class NodeSerializer {
	private final MessageTypeStore store;
	private final Map<Class<? extends Node>, Constructor<? extends Node>> cachedConstructors;

	public NodeSerializer(MessageTypeStore store) {
		this.store = store;
		this.cachedConstructors = new HashMap<>();
	}

	public <T extends Node> Constructor<T> getConstructor(Class<T> nodeType, Hardware hardware) {
		// cached result
		if (cachedConstructors.containsKey(nodeType)) {
			// This cast is safe because we only put the right type of constructor in the map later in this method.
			//noinspection unchecked
			return (Constructor<T>) cachedConstructors.get(nodeType);
		}

		// We have more information than Java
		//noinspection unchecked
		var constructors = (Constructor<T>[]) nodeType.getConstructors();

		for (Constructor<T> constructor : constructors) {
			if (constructor.getParameterCount() != 1) {
				continue;
			}

			if (hardware.getClass().isAssignableFrom(constructor.getParameterTypes()[0])) {
				cachedConstructors.put(nodeType, constructor);
				return constructor;
			}
		}

		throw new IllegalArgumentException(("The node %s does not provide a constructor that has exactly one " +
				"argument and this argument is a %s. Please provide this type of constructor and try again.")
				.formatted(nodeType.getName(), hardware.getClass().getName()));
	}

	public void serialize(DataOutputStream stream, Node node) throws SerializationException {
		stream.writeUnsignedByte(node.id());
		stream.writeUnsignedByte(node.hardware().id());
	}

	public Node deserialize(short nodeId, short hardwareId) {
		var nodeType = store.getNodeType(nodeId);
		var hardware = store.getHardware(nodeId, hardwareId);
		return deserialize(nodeType, hardware);
	}

	public Node deserialize(Class<? extends Node> nodeType, Hardware hardware) {
		var constructor = getConstructor(nodeType, hardware);
		try {
			return constructor.newInstance(hardware);
		} catch (InstantiationException e) {
			// TODO: Better exception
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO: Better exception
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			// TODO: Better exception
			throw new RuntimeException(e);
		}
	}
}
