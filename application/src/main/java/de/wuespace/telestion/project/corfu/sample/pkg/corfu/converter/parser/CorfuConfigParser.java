package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.exception.ParsingException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.AppConfiguration;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.CorfuProjectConfiguration;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.NodeConfiguration;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.ProjectConfiguration;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.util.PathUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Convenience class to read Corfu configuration files and map them to their POJO counterparts via Jackson.
 * It understands:
 * <ul>
 *     <li>{@link CorfuProjectConfiguration}</li>
 *     <li>{@link AppConfiguration}</li>
 *     <li>{@link NodeConfiguration}</li>
 * </ul>
 *
 * @author Ludwig Richter (@fussel178)
 */
public class CorfuConfigParser {

	/**
	 * Name of the directory that holds all apps in the Corfu project.
	 */
	public static final String APPS_DIR_NAME = "apps";

	/**
	 * Name of the directory that holds all nodes in the Corfu project.
	 */
	public static final String NODES_DIR_NAME = "nodes";

	/**
	 * Name of the project configuration file inside the project root.
	 */
	public static final String PROJECT_CONFIG_NAME = "project.yml";

	/**
	 * Name of the configuration file for an app inside an app folder.
	 */
	public static final String APP_CONFIG_NAME = "app.yml";

	/**
	 * Name of the configuration file for a node inside a node folder.
	 */
	public static final String NODE_CONFIG_NAME = "node.yml";

	private final ObjectMapper mapper;
	private final ParserOptions options;

	/**
	 * Creates a new instance of the Corfu configuration parser.
	 * @param mapper the Jackson mapper instance that can convert Corfu configuration files
	 */
	public CorfuConfigParser(ObjectMapper mapper) {
		this(mapper, new ParserOptions());
	}

	/**
	 * Creates a new instance of the Corfu configuration parser.
	 * @param mapper the Jackson mapper instance that can convert Corfu configuration files
	 * @param options custom configuration options to configure the behaviour of the parser
	 */
	public CorfuConfigParser(ObjectMapper mapper, ParserOptions options) {
		this.mapper = mapper;
		this.options = options;
	}

	/**
	 * Returns a Corfu project configuration in POJO format from the root directory of the Corfu project.
	 * @param projectRootDir path to the root directory of the Corfu project
	 * @return the entire project configuration in POJO format
	 * @throws ParsingException throws when errors during parsing of the configuration files occur
	 */
	public CorfuProjectConfiguration getProjectConfig(Path projectRootDir) throws ParsingException {
		if (!projectRootDir.isAbsolute()) {
			throw new IllegalArgumentException(("The project root directory %s is not absolute. Please provide an " +
					"absolute path to the Corfu project root directory and try again.")
					.formatted(projectRootDir.toString()));
		}

		var projectPath = projectRootDir.resolve(PROJECT_CONFIG_NAME);
		var appsDir = projectRootDir.resolve(APPS_DIR_NAME);
		var nodesDir = projectRootDir.resolve(NODES_DIR_NAME);


		var projectConfig = getProjectConfiguration(projectPath);
		var appsConfig = getAppConfigurations(appsDir);
		var nodesConfig = getNodeConfigurations(nodesDir);

		// tag app that emits standard telemetry as defined in the project config
		var standardTelemetryApp = projectConfig.ground.standardTelemetry;
		try {
			var found = appsConfig.stream()
					.filter(app -> app.getName().raw().equals(standardTelemetryApp.app))
					.findFirst().orElseThrow();
			found.setStandardTelemetryPayloadId(standardTelemetryApp.payloadId);
		} catch (NoSuchElementException e) {
			throw new ParsingException(("The standard telemetry app defined in the project configuration does not " +
					"exist yet. Please create an app with the name %s or rewrite the project configuration and try " +
					"again.").formatted(projectConfig.ground.standardTelemetry.app));
		}

		return new CorfuProjectConfiguration(projectConfig, appsConfig, nodesConfig);
	}

	public ProjectConfiguration getProjectConfiguration(Path configPath) throws ParsingException {
		if (!Files.isRegularFile(configPath)) {
			throw new IllegalArgumentException(("The project config %s is not a regular file. Please provide " +
					"a valid project configuration file and try again.")
					.formatted(configPath));
		}

		try {
			var fileContent = Files.readString(configPath);
			return mapper.readValue(fileContent, ProjectConfiguration.class);
		} catch (StreamReadException e) {
			throw new ParsingException(("Cannot map raw file content to project configuration. Error in stream " +
					"processing. Configuration file: %s").formatted(configPath.toString()), e);
		} catch (DatabindException e) {
			throw new ParsingException(("Cannot databind generic representation to objects. Error in databind " +
					"processing. Configuration file: %s").formatted(configPath.toString()), e);
		} catch (JsonProcessingException e) {
			throw new ParsingException("Cannot process configuration file. Configuration file: %s"
					.formatted(configPath.toString()), e);
		} catch (IOException e) {
			throw new ParsingException("IO error during filesystem operations. Configuration file: %s"
					.formatted(configPath.toString()), e);
		}
	}

	/**
	 * Returns a list of app configuration in POJO format from the apps directory of a Corfu project.
	 * @param appsDir path to the apps directory that contains the apps with their configurations
	 * @return a list of app configurations in POJO format
	 * @throws ParsingException throws when errors during parsing of the configuration files occur
	 */
	public List<AppConfiguration> getAppConfigurations(Path appsDir) throws ParsingException {
		// no apps dir, no apps config
		if (!Files.isDirectory(appsDir)) {
			return Collections.emptyList();
		}

		List<AppConfiguration> apps = new ArrayList<>();
		try (var stream = Files.list(appsDir)) {
			var list = stream.toList();
			for (Path appDir : list) {
				var appName = appDir.getFileName().toString();
				if (options.getIgnoredApps().contains(appName)) {
					System.err.printf("Ignoring application %s%n", appName);
					continue;
				}

				if (!Files.isDirectory(appDir)) {
					throw new IllegalArgumentException(("The app %s is not a directory. Please remove this file " +
							"or create a directory with this name and try again.").formatted(appDir.toString()));
				}

				var configPath = appDir.resolve(APP_CONFIG_NAME);
				apps.add(getAppConfiguration(configPath));
			}
		} catch (IOException e) {
			throw new ParsingException("Cannot read list of apps in app directory %s".formatted(appsDir.toString()), e);
		}

		return apps;
	}

	/**
	 * Returns a list of node configurations in POJO format from the nodes directory of a Corfu project.
	 * @param nodesDir path to the nodes directory that contains the nodes with their configurations
	 * @return a list of node configurations in POJO format
	 * @throws ParsingException throws when errors during parsing of the configuration files occur
	 */
	public List<NodeConfiguration> getNodeConfigurations(Path nodesDir) throws ParsingException {
		// no nodes dir, no nodes config
		if (!Files.isDirectory(nodesDir)) {
			return Collections.emptyList();
		}

		List<NodeConfiguration> nodes = new ArrayList<>();
		try (var stream = Files.list(nodesDir)) {
			var list = stream.toList();
			for (Path node : list) {
				var nodeName = node.getFileName().toString();
				if (options.getIgnoredNodes().contains(nodeName)) {
					System.err.printf("Ignoring node %s%n", nodeName);
					continue;
				}

				if (Files.isRegularFile(node)) {
					// try to read file
					nodes.add(getNodeConfiguration(node, PathUtils.getFileName(node, true)));
				} else if (Files.isDirectory(node)) {
					var configPath = node.resolve(NODE_CONFIG_NAME);
					nodes.add(getNodeConfiguration(configPath, PathUtils.getFileName(node, true)));
				} else {
					throw new IllegalArgumentException(("The node %s has an incompatible file type. Please put the " +
							"node configuration inside the nodes directory or in a directory with the name " +
							"\"node.yml\" and try again. Compatible: %s").formatted(node, "regular file, directory"));
				}
			}
		} catch (IOException e) {
			throw new ParsingException("Cannot read list of nodes in node directory %s"
					.formatted(nodesDir.toString()), e);
		}

		return nodes;
	}

	/**
	 * Returns an app configuration in POJO format from an app directory of a Corfu project.
	 * @param configPath path to the root directory of an app in a Corfu project
	 * @return an app configuration in POJO format
	 * @throws ParsingException throws when errors during parsing of the configuration files occur
	 */
	public AppConfiguration getAppConfiguration(Path configPath) throws ParsingException {
		if (!Files.isRegularFile(configPath)) {
			throw new IllegalArgumentException(("The app config %s is not a regular file. Please provide " +
					"a valid app configuration file or remove the entire app directory and try again.")
					.formatted(configPath));
		}

		try {
			var fileContent = Files.readString(configPath);
			var config = mapper.readValue(fileContent, AppConfiguration.class);

			// we want the name of the parent folder of the configuration
			config.setName(PathUtils.getFileName(configPath.getParent(), true));

			config.finalizeConfig();
			config.referenceTypes();

			return config;
		} catch (StreamReadException e) {
			throw new ParsingException(("Cannot map raw file content to app configuration. Error in stream " +
					"processing. Configuration file: %s").formatted(configPath.toString()), e);
		} catch (DatabindException e) {
			throw new ParsingException(("Cannot databind generic representation to objects. Error in databind " +
					"processing. Configuration file: %s").formatted(configPath.toString()), e);
		} catch (JsonProcessingException e) {
			throw new ParsingException("Cannot process configuration file. Configuration file: %s"
					.formatted(configPath.toString()), e);
		} catch (IOException e) {
			throw new ParsingException("IO error during filesystem operations. Configuration file: %s"
					.formatted(configPath.toString()), e);
		}
	}

	/**
	 * Returns a node configuration in POJO format from a node directory of a Corfu project.
	 * @param configPath path to the root directory of a node in a Corfu project
	 * @return a node configuration in POJO format
	 * @throws ParsingException throws when errors during parsing of the configuration files occur
	 */
	public NodeConfiguration getNodeConfiguration(Path configPath, String name) throws ParsingException {
		if (!Files.isRegularFile(configPath)) {
			throw new IllegalArgumentException(("The node config %s is not a regular file. Please provide " +
					"a valid node configuration file or remove the entire app directory and try again.")
					.formatted(configPath));
		}

		try {
			var fileContent = Files.readString(configPath);
			var config = mapper.readValue(fileContent, NodeConfiguration.class);

			// we want the name of the parent folder of the configuration
			config.setName(name);

			config.finalizeConfig();

			return config;
		} catch (StreamReadException e) {
			throw new ParsingException(("Cannot map raw file content to node configuration. Error in stream " +
					"processing. Configuration file: %s").formatted(configPath.toString()), e);
		} catch (DatabindException e) {
			throw new ParsingException(("Cannot databind generic representation to objects. Error in databind " +
					"processing. Configuration file: %s").formatted(configPath.toString()), e);
		} catch (JsonProcessingException e) {
			throw new ParsingException("Cannot process configuration file. Configuration file: %s"
					.formatted(configPath.toString()), e);
		} catch (IOException e) {
			throw new ParsingException("IO error during filesystem operations. Configuration file: %s"
					.formatted(configPath.toString()), e);
		}
	}
}
