package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuDeserializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuHardware;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements a Corfu node transformer that can map {@link CorfuNode} and {@link CorfuHardware}
 * from their binary format into a {@link de.wuespace.telestion.api.message.JsonMessage JsonMessage} format and back.
 *
 * @author Ludwig Richter (@fussel178)
 */
public class CorfuNodeMapper {
	private final MessageTypeStore store;
	private final Map<Class<? extends CorfuNode>, Constructor<? extends CorfuNode>> cachedConstructors;

	public CorfuNodeMapper(MessageTypeStore store) {
		this.store = store;
		this.cachedConstructors = new HashMap<>();
	}

	/**
	 * Returns the constructor for a {@link CorfuNode} that accepts parameters to fill the hardware field.
	 * (when using a record, the invisible predefined constructor of a {@link Record})
	 *
	 * @param nodeType the class type of the {@link CorfuNode}
	 * @param hardware the hardware of the {@link CorfuNode} that the constructor of the node accepts
	 * @return the constructor that can create new objects of type {@link CorfuNode}
	 * @throws NoSuchMethodException gets thrown if no constructor can be found
	 *                               that accepts the hardware as parameter
	 */
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

	/**
	 * Deserializes a {@link CorfuNode} from its binary format to an actual Java object.
	 *
	 * @param nodeId     the id of the node extracted from the corfu message header as unsigned byte
	 * @param hardwareId the id of the hardware that sent the message extracted from the corfu message header as unsigned byte
	 * @return an instantiated Java object that holds the decoded values
	 * @throws CorfuDeserializationException if errors happen during deserialization
	 */
	public CorfuNode deserialize(short nodeId, short hardwareId) throws CorfuDeserializationException {
		return deserialize(store.getNodeType(nodeId), store.getHardware(nodeId, hardwareId));
	}

	/**
	 * Deserializes a {@link CorfuNode} from its binary format to an actual Java object.
	 *
	 * @param nodeType the class type of the Corfu node
	 * @param hardware the actual hardware object
	 * @return an instantiated Java object that holds the decoded values
	 * @throws CorfuDeserializationException if errors happen during deserialization
	 */
	public CorfuNode deserialize(Class<? extends CorfuNode> nodeType, CorfuHardware hardware)
			throws CorfuDeserializationException {

		try {
			var constructor = getConstructor(nodeType, hardware);
			return constructor.newInstance(hardware);
		} catch (InvocationTargetException e) {
			throw new CorfuDeserializationException("During instantiation the constructor threw an exception. " +
					"Please fix the following error and try again:", e);
		} catch (InstantiationException e) {
			throw new CorfuDeserializationException(("The node %s is abstract. This is not supported. Please " +
					"convert the node to a real class or convert the node to a record and try again.")
					.formatted(nodeType.getName()));
		} catch (IllegalAccessException e) {
			throw new CorfuDeserializationException(("The constructor of node %s is not accessible. Please make " +
					"the constructor public and accessible or convert the node to a record and try again.")
					.formatted(nodeType.getName()));
		} catch (NoSuchMethodException e) {
			throw new CorfuDeserializationException(("The node %s does not provide a constructor that accepts " +
					"the hardware %s as constructor parameter. Please provide this type of constructor or " +
					"convert the node to a record and try again. Needed constructor parameters: %s")
					.formatted(nodeType.getName(), hardware.getClass().getName(), "[" + hardware.getClass().getName() + "]"));
		}
	}
}
