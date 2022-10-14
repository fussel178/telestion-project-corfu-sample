package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuDeserializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuSerializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.PrimitiveOutputStream;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuHardware;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuNode;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.OutputStreamFullException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CorfuNodeMapper {
	private final MessageTypeStore store;
	private final Map<Class<? extends CorfuNode>, Constructor<? extends CorfuNode>> cachedConstructors;

	public CorfuNodeMapper(MessageTypeStore store) {
		this.store = store;
		this.cachedConstructors = new HashMap<>();
	}

	public <T extends CorfuNode> Constructor<T> getConstructor(Class<T> nodeType, CorfuHardware hardware)
			throws NoSuchMethodException {

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

		throw new NoSuchMethodException(("The node %s does not provide a constructor that has exactly one " +
				"argument and this argument is a %s. Please provide this type of constructor and try again.")
				.formatted(nodeType.getName(), hardware.getClass().getName()));
	}

	public void serialize(PrimitiveOutputStream stream, CorfuNode node) throws CorfuSerializationException {
		try {
			stream.writeUnsignedByte(node.id());
			stream.writeUnsignedByte(node.hardware().id());
		} catch (OutputStreamFullException e) {
			throw new CorfuSerializationException("The output stream is full. Is the Corfu message too large?", e);
		}
	}

	public CorfuNode deserialize(short nodeId, short hardwareId) throws CorfuDeserializationException {
		var nodeType = store.getNodeType(nodeId);
		var hardware = store.getHardware(nodeId, hardwareId);
		return deserialize(nodeType, hardware);
	}

	public CorfuNode deserialize(Class<? extends CorfuNode> nodeType, CorfuHardware hardware)
			throws CorfuDeserializationException {

		try {
			var constructor = getConstructor(nodeType, hardware);
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
		} catch (NoSuchMethodException e) {
			// TODO: Better exception
			throw new RuntimeException(e);
		}
	}
}
