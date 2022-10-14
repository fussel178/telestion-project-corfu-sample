package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuDeserializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.exception.CorfuSerializationException;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.PrimitiveOutputStream;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuApp;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuAppTelecommand;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuAppTelemetry;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuPayload;
import de.wuespace.telestion.project.corfu.sample.pkg.util.stream.exception.OutputStreamFullException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CorfuAppMapper {
	private final MessageTypeStore store;
	private final Map<Class<? extends CorfuApp>, Constructor<? extends CorfuApp>> cachedConstructors;

	public CorfuAppMapper(MessageTypeStore store) {
		this.store = store;
		this.cachedConstructors = new HashMap<>();
	}

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

	public void serialize(PrimitiveOutputStream stream, CorfuAppTelecommand app) throws CorfuSerializationException {
		try {
			stream.writeUnsignedByte(app.id());
			stream.writeUnsignedByte(app.payload().id());
		} catch (OutputStreamFullException e) {
			throw new CorfuSerializationException("The output stream is full. Is the Corfu message too large?", e);
		}
	}

	public CorfuAppTelemetry deserialize(short appId, CorfuPayload payload) throws CorfuDeserializationException {
		return deserialize(store.getAppTelemetryType(appId), payload);
	}

	public CorfuAppTelemetry deserialize(Class<? extends CorfuAppTelemetry> appType, CorfuPayload payload)
			throws CorfuDeserializationException {

		try {
			var constructor = getConstructor(appType, payload.getClass());
			return constructor.newInstance(payload);
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
