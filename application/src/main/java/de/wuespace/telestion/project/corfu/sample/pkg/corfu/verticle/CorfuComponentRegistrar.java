package de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.verticle.TelestionConfiguration;
import de.wuespace.telestion.api.verticle.TelestionVerticle;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle.global.GlobalCorfuModule;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle.global.GlobalStore;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle.global.Registrar;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unused")
public class CorfuComponentRegistrar extends TelestionVerticle<CorfuComponentRegistrar.Configuration> {

	public record Configuration(
			@JsonProperty String[] registrarClassNames
	) implements TelestionConfiguration {
		public Configuration() {
			this(new String[]{});
		}
	}

	@Override
	public void onStart() {
		for (var className : getConfig().registrarClassNames) {
			try {
				logger.info("Load registrar {}", className);
				var registrar = load(className);
				logger.info("Import Corfu components from registrar {}", className);
				registrar.onRegister(GlobalStore.store());
			} catch (ClassNotFoundException e) {
				logger.error("Cannot find registrar {}. Please check if the given class reference is correct and try again.", className);
			} catch (ClassCastException e) {
				logger.error("Registrar {} does not implement the registrar interface {}. Please implement the interface and try again.", className, Registrar.class.getName());
			} catch (NoSuchMethodException e) {
				logger.error("Registrar {} does not provide a default constructor. Please create a constructor without any arguments and try again.", className);
			} catch (IllegalAccessException e) {
				logger.error("Constructor of registrar {} is private. Please create a default public constructor and try again.", className);
			} catch (InstantiationException e) {
				logger.error("Registrar {} is abstract. Please provide a instantiable registrar class and try again.", className);
			} catch (InvocationTargetException e) {
				logger.error("The registrar {} threw an exception during creation. Please fix the following exception and try again: {}", className, e);
			}
		}

		logger.info("Load Corfu Jackson Serializers");
		GlobalCorfuModule.register();

		logger.info("Successfully initialized global message type store");
	}

	private Registrar load(String className) throws ClassNotFoundException, ClassCastException, NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		var type = Class.forName(className).asSubclass(Registrar.class);
		return type.getConstructor().newInstance();
	}
}
