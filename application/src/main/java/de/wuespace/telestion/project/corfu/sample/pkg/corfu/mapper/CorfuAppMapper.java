package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuDeserializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuApp;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuAppTelecommand;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuAppTelemetry;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuPayload;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements a Corfu app transformer that can map {@link CorfuAppTelemetry} and {@link CorfuAppTelecommand}
 * from their binary format into a {@link de.wuespace.telestion.api.message.JsonMessage JsonMessage} format and back.
 *
 * @author Ludwig Richter (@fussel178)
 */
public class CorfuAppMapper {
	private final MessageTypeStore store;
	private final Map<Class<? extends CorfuApp>, Constructor<? extends CorfuApp>> cachedConstructors;

	public CorfuAppMapper(MessageTypeStore store) {
		this.store = store;
		this.cachedConstructors = new HashMap<>();
	}

	/**
	 * Returns the constructor for a {@link CorfuApp} that accepts parameters to fill all record fields.
	 * (aka the invisible predefined constructor of a {@link Record})
	 *
	 * @param appType     the class type of the {@link CorfuApp}
	 * @param payloadType the class type of the {@link CorfuPayload} that the constructor of the app accepts
	 * @return the constructor that can create new objects of type {@link CorfuApp}
	 * @throws NoSuchMethodException gets thrown if no constructor can be found
	 *                               that accepts the payload as parameter
	 */
	public <T extends CorfuApp, U extends CorfuPayload> Constructor<T> getConstructor(Class<T> appType, Class<U> payloadType)
			throws NoSuchMethodException {
		// cached result
		if (cachedConstructors.containsKey(appType)) {
			// This cast is safe because we only put the right type of constructor in the map later in this method.
			//noinspection unchecked
			return (Constructor<T>) cachedConstructors.get(appType);
		}

		// We have more information than Java
		//noinspection unchecked
		var constructors = (Constructor<T>[]) appType.getConstructors();

		for (Constructor<T> constructor : constructors) {
			if (constructor.getParameterCount() != 1) {
				continue;
			}

			var parameterType = constructor.getParameterTypes()[0];
			if (parameterType.isAssignableFrom(payloadType)) {
				cachedConstructors.put(appType, constructor);
				return constructor;
			}
		}

		throw new NoSuchMethodException(("The app %s does not provide a constructor that has exactly one argument " +
				"and this argument is a %s. Please provide this type of constructor and try again.")
				.formatted(appType.getName(), payloadType.getName()));
	}

	/**
	 * Deserializes a {@link CorfuApp} from its binary format to an actual Java object.
	 *
	 * @param appId   the id of the telemetry app extracted from the corfu message header as unsigned byte
	 * @param payload the already deserialized payload as Java object
	 * @return an instantiated Java object that holds the decoded values
	 * @throws CorfuDeserializationException if errors happen during deserialization
	 */
	public CorfuAppTelemetry deserialize(short appId, CorfuPayload payload) throws CorfuDeserializationException {
		return deserialize(store.getAppTelemetryType(appId), payload);
	}

	/**
	 * Deserializes a {@link CorfuApp} from its binary format to an actual Java object.
	 *
	 * @param appType the class type of the telemetry app
	 * @param payload the already deserialized payload as Java object
	 * @return an instantiated Java object that holds the decoded values
	 * @throws CorfuDeserializationException if errors happen during deserialization
	 */
	public CorfuAppTelemetry deserialize(Class<? extends CorfuAppTelemetry> appType, CorfuPayload payload)
			throws CorfuDeserializationException {

		try {
			var constructor = getConstructor(appType, payload.getClass());
			return constructor.newInstance(payload);
		} catch (InvocationTargetException e) {
			throw new CorfuDeserializationException("During instantiation the constructor threw an exception. " +
					"Please fix the following error and try again:", e);
		} catch (InstantiationException e) {
			throw new CorfuDeserializationException(("The app %s is abstract. This is not supported. Please " +
					"convert the app to a real class or convert the app to a record and try again.")
					.formatted(appType.getName()));
		} catch (IllegalAccessException e) {
			throw new CorfuDeserializationException(("The constructor of app %s is not accessible. Please make " +
					"the constructor public and accessible or convert the app to a record and try again.")
					.formatted(appType.getName()));
		} catch (NoSuchMethodException e) {
			throw new CorfuDeserializationException(("The app %s does not provide a constructor that accepts " +
					"the payload %s as constructor parameter. Please provide this type of constructor or " +
					"convert the app to a record and try again. Needed constructor parameters: %s")
					.formatted(appType.getName(), payload.getClass().getName(), "[" + payload.getClass().getName() + "]"));
		}
	}
}
