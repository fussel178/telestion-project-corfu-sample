package de.wuespace.telestion.project.corfu.sample.converter.serializer;

import de.wuespace.telestion.project.corfu.sample.converter.exception.SerializationException;
import de.wuespace.telestion.project.corfu.sample.converter.store.MessageTypeStore;
import de.wuespace.telestion.project.corfu.sample.converter.util.DataOutputStream;
import de.wuespace.telestion.project.corfu.sample.converter.message.App;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelecommand;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelemetry;
import de.wuespace.telestion.project.corfu.sample.converter.message.Payload;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class AppSerializer {
	private final MessageTypeStore store;
	private final Map<Class<? extends App>, Constructor<? extends App>> cachedConstructors;

	public AppSerializer(MessageTypeStore store) {
		this.store = store;
		this.cachedConstructors = new HashMap<>();
	}

	public <T extends App, U extends Payload> Constructor<T> getConstructor(Class<T> appType, Class<U> payloadType) {
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

		throw new IllegalArgumentException(("The app %s does not provide a constructor that has exactly one argument " +
				"and this argument is a %s. Please provide this type of constructor and try again.")
				.formatted(appType.getName(), payloadType.getName()));
	}

	public void serialize(DataOutputStream stream, AppTelecommand app) throws SerializationException {
		stream.writeUnsignedByte(app.id());
		stream.writeUnsignedByte(app.payload().id());
	}

	public AppTelemetry deserialize(short appId, Payload payload) {
		return deserialize(store.getAppTelemetryType(appId), payload);
	}

	public AppTelemetry deserialize(Class<? extends AppTelemetry> appType, Payload payload) {
		var constructor = getConstructor(appType, payload.getClass());

		try {
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
		}
	}
}
