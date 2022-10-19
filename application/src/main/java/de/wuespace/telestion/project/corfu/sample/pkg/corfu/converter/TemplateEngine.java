package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter;

import com.hubspot.jinjava.Jinjava;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Rendering;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.*;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateEngine {

	public static final Path TEMPLATE_DIR = Path.of("templates");

	public static final Path APP_TELECOMMAND_RECORD_TEMPLATE =
			TEMPLATE_DIR.resolve("appTelecommandRecord.java.jinja");

	public static final Path APP_TELECOMMAND_PAYLOAD_INTERFACE_TEMPLATE =
			TEMPLATE_DIR.resolve("appTelecommandPayloadInterface.java.jinja");

	public static final Path APP_TELEMETRY_RECORD_TEMPLATE =
			TEMPLATE_DIR.resolve("appTelemetryRecord.java.jinja");

	public static final Path APP_TELEMETRY_PAYLOAD_INTERFACE_TEMPLATE =
			TEMPLATE_DIR.resolve("appTelemetryPayloadInterface.java.jinja");

	public static final Path NODE_RECORD_TEMPLATE = TEMPLATE_DIR.resolve("nodeRecord.java.jinja");

	public static final Path TELECOMMAND_PAYLOAD_RECORD_TEMPLATE =
			TEMPLATE_DIR.resolve("telecommandPayloadRecord.java.jinja");

	public static final Path TELEMETRY_PAYLOAD_RECORD_TEMPLATE =
			TEMPLATE_DIR.resolve("telemetryPayloadRecord.java.jinja");

	public static final Path APP_STRUCT_RECORD_TEMPLATE = TEMPLATE_DIR.resolve("appStructRecord.java.jinja");

	public static final Path APP_STANDARD_TELEMETRY_RECORD_TEMPLATE =
			TEMPLATE_DIR.resolve("appStandardTelemetryRecord.java.jinja");

	private final Jinjava engine;

	private String appTelecommandRecordTemplate;

	private String appTelecommandPayloadInterfaceTemplate;

	private String appTelemetryRecordTemplate;

	private String appTelemetryPayloadInterfaceTemplate;

	private String nodeRecordTemplate;

	private String telecommandPayloadRecordTemplate;

	private String telemetryPayloadRecordTemplate;

	private String appStructRecordTemplate;

	private String appStandardTelemetryRecordTemplate;

	private boolean areTemplatesLoaded;

	private final Map<String, Object> globalContext;

	public TemplateEngine() {
		this.engine = new Jinjava();
		this.areTemplatesLoaded = false;
		this.globalContext = new HashMap<>();

		globalContext.put("app_telecommand_payload_binary_name", AppTelecommandPayload.class.getName());
		globalContext.put("app_telemetry_payload_binary_name", AppTelemetryPayload.class.getName());
		globalContext.put("corfu_app_telecommand_binary_name", CorfuAppTelecommand.class.getName());
		globalContext.put("corfu_app_telemetry_binary_name", CorfuAppTelemetry.class.getName());
		globalContext.put("corfu_hardware_binary_name", CorfuHardware.class.getName());
		globalContext.put("corfu_node_binary_name", CorfuNode.class.getName());
		globalContext.put("corfu_property_binary_name", CorfuProperty.class.getName());
		globalContext.put("corfu_struct_binary_name", CorfuStruct.class.getName());
		globalContext.put("corfu_standard_telemetry_binary_name", AppStandardTelemetry.class.getName());
	}

	public boolean areTemplatesLoaded() {
		return areTemplatesLoaded;
	}

	public void loadTemplates() {
		loadTemplates(false);
	}

	public void loadTemplates(boolean force) {
		if (!force && areTemplatesLoaded) return;

		appTelecommandRecordTemplate = loadResource(APP_TELECOMMAND_RECORD_TEMPLATE);
		appTelecommandPayloadInterfaceTemplate = loadResource(APP_TELECOMMAND_PAYLOAD_INTERFACE_TEMPLATE);
		appTelemetryRecordTemplate = loadResource(APP_TELEMETRY_RECORD_TEMPLATE);
		appTelemetryPayloadInterfaceTemplate = loadResource(APP_TELEMETRY_PAYLOAD_INTERFACE_TEMPLATE);
		nodeRecordTemplate = loadResource(NODE_RECORD_TEMPLATE);
		telecommandPayloadRecordTemplate = loadResource(TELECOMMAND_PAYLOAD_RECORD_TEMPLATE);
		telemetryPayloadRecordTemplate = loadResource(TELEMETRY_PAYLOAD_RECORD_TEMPLATE);
		appStructRecordTemplate = loadResource(APP_STRUCT_RECORD_TEMPLATE);
		appStandardTelemetryRecordTemplate = loadResource(APP_STANDARD_TELEMETRY_RECORD_TEMPLATE);

		areTemplatesLoaded = true;
	}

	public Rendering renderNodeRecord(Package pkg, NodeConfiguration config) {
		loadTemplates();

		// create jinja context
		Map<String, Object> context = new HashMap<>(globalContext);

		// fill node specific values
		context.put("package", pkg.binaryName());
		context.put("raw_name", config.getName().raw());
		context.put("uppercamelcase_name", config.getName().upperCamelCase());
		context.put("description", config.getDescription());
		context.put("id", config.getId());

		// create hardware list
		List<?> hardwareTargets = config.hardwareTargets.entrySet().stream()
				.map(TemplateEngine::hardwareToMap)
				.toList();

		context.put("hardware_targets", hardwareTargets);

		return new Rendering(
				engine.render(nodeRecordTemplate, context),
				"%sNode".formatted(config.getName().upperCamelCase()),
				pkg
		);
	}

	public Rendering renderAppTelecommandRecord(Package pkg, AppConfiguration config) {
		loadTemplates();

		// create jinja context
		Map<String, Object> context = new HashMap<>(globalContext);

		// fill app specific values
		context.put("package", pkg.binaryName());
		context.put("raw_name", config.getName().raw());
		context.put("uppercamelcase_name", config.getName().upperCamelCase());
		context.put("description", config.getDescription());
		context.put("id", config.getId());

		return new Rendering(
				engine.render(appTelecommandRecordTemplate, context),
				"%sTelecommand".formatted(config.getName().upperCamelCase()),
				pkg
		);
	}

	public Rendering renderAppTelemetryRecord(Package pkg, AppConfiguration config) {
		loadTemplates();

		// create jinja context
		Map<String, Object> context = new HashMap<>(globalContext);

		// fill app specific values
		context.put("package", pkg.binaryName());
		context.put("raw_name", config.getName().raw());
		context.put("uppercamelcase_name", config.getName().upperCamelCase());
		context.put("description", config.getDescription());
		context.put("id", config.getId());

		return new Rendering(
				engine.render(appTelemetryRecordTemplate, context),
				"%sTelemetry".formatted(config.getName().upperCamelCase()),
				pkg
		);
	}

	public Rendering renderAppStructRecord(Package pkg, MessageFields config) {
		loadTemplates();

		// create jinja context
		Map<String, Object> context = new HashMap<>(globalContext);

		// fill app specific values
		context.put("package", pkg.binaryName());
		context.put("raw_name", config.getName().raw());
		context.put("uppercamelcase_name", config.getName().upperCamelCase());
		context.put("app_raw_name", config.getAssociatedApp().getName().raw());

		// create record elements
		List<?> recordElements = config.content.stream().map(TemplateEngine::messageTypeToMap).toList();
		context.put("record_elements", recordElements);

		return new Rendering(
				engine.render(appStructRecordTemplate, context),
				"%sStruct".formatted(config.getName().upperCamelCase()),
				pkg
		);
	}

	public Rendering renderAppStandardTelemetryRecord(Package pkg, AppConfiguration config) {
		loadTemplates();

		// create jinja context
		Map<String, Object> context = new HashMap<>(globalContext);

		// fill app specific values
		context.put("package", pkg.binaryName());
		context.put("raw_name", config.getName().raw());
		context.put("uppercamelcase_name", config.getName().upperCamelCase());

		// create record elements
		List<?> recordElements = config.standardTelemetry.content.stream().map(TemplateEngine::messageTypeToMap).toList();
		context.put("record_elements", recordElements);

		return new Rendering(
				engine.render(appStandardTelemetryRecordTemplate, context),
				"%sStandardTelemetry".formatted(config.getName().upperCamelCase()),
				pkg
		);
	}

	public Rendering renderAppTelecommandPayloadInterface(Package pkg, AppConfiguration config) {
		loadTemplates();

		// create jinja context
		Map<String, Object> context = new HashMap<>(globalContext);

		// fill app specific values
		context.put("package", pkg.binaryName());
		context.put("raw_name", config.getName().raw());
		context.put("uppercamelcase_name", config.getName().upperCamelCase());

		// create telecommand payload list
		List<?> payloads = config.telecommands.values().stream().map(TemplateEngine::telecommandToMap).toList();
		context.put("payloads", payloads);

		return new Rendering(
				engine.render(appTelecommandPayloadInterfaceTemplate, context),
				"%sTelecommandPayload".formatted(config.getName().upperCamelCase()),
				pkg
		);
	}

	public Rendering renderAppTelemetryPayloadInterface(Package pkg, AppConfiguration config) {
		loadTemplates();

		// create jinja context
		Map<String, Object> context = new HashMap<>(globalContext);

		// fill app specific values
		context.put("package", pkg.binaryName());
		context.put("raw_name", config.getName().raw());
		context.put("uppercamelcase_name", config.getName().upperCamelCase());

		// create telecommand payload list
		List<?> payloads = config.extendedTelemetry.values().stream().map(TemplateEngine::telemetryToMap).toList();
		context.put("payloads", payloads);

		return new Rendering(
				engine.render(appTelemetryPayloadInterfaceTemplate, context),
				"%sTelemetryPayload".formatted(config.getName().upperCamelCase()),
				pkg
		);
	}

	public Rendering renderTelecommandPayloadRecord(Package pkg, Message config) {
		loadTemplates();

		// create jinja context
		Map<String, Object> context = new HashMap<>(globalContext);

		// fill telecommand specific values
		context.put("package", pkg.binaryName());
		context.put("raw_name", config.getName().raw());
		context.put("uppercamelcase_name", config.getName().upperCamelCase());
		context.put("description", config.getDescription());
		context.put("app_raw_name", config.getAssociatedApp().getName().raw());
		context.put("app_uppercamelcase_name", config.getAssociatedApp().getName().upperCamelCase());
		context.put("id", config.getId());

		// create record elements
		List<?> recordElements = config.fields.content.stream().map(TemplateEngine::messageTypeToMap).toList();
		context.put("record_elements", recordElements);

		return new Rendering(
				engine.render(telecommandPayloadRecordTemplate, context),
				"%sTelecommandPayload".formatted(config.getName().upperCamelCase()),
				pkg
		);
	}

	public Rendering renderTelemetryPayloadRecord(Package pkg, Message config) {
		loadTemplates();

		// create jinja context
		Map<String, Object> context = new HashMap<>(globalContext);

		// fill telecommand specific values
		context.put("package", pkg.binaryName());
		context.put("raw_name", config.getName().raw());
		context.put("uppercamelcase_name", config.getName().upperCamelCase());
		context.put("description", config.getDescription());
		context.put("app_raw_name", config.getAssociatedApp().getName().raw());
		context.put("app_uppercamelcase_name", config.getAssociatedApp().getName().upperCamelCase());
		context.put("id", config.getId());

		// create record elements
		List<?> recordElements = config.fields.content.stream().map(TemplateEngine::messageTypeToMap).toList();
		context.put("record_elements", recordElements);

		return new Rendering(
				engine.render(telemetryPayloadRecordTemplate, context),
				"%sTelemetryPayload".formatted(config.getName().upperCamelCase()),
				pkg
		);
	}

	private String loadResource(Path relativePath) {
		ClassLoader loader = TemplateEngine.class.getClassLoader();

		try (var stream = loader.getResourceAsStream(relativePath.toString())) {
			assert stream != null;
			return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new IllegalArgumentException("The template %s cannot be found in the module resources."
					.formatted(relativePath), e);
		}
	}

	private static Map<String, Object> hardwareToMap(Map.Entry<String, Short> entry) {
		return Map.of("name", new ComponentName(entry.getKey()).screamingSnakeCase(), "id", entry.getValue());
	}

	private static Map<String, Object> telecommandToMap(Message telecommand) {
		return Map.of("class_name", "%sTelecommandPayload".formatted(telecommand.getName().upperCamelCase()));
	}

	private static Map<String, Object> telemetryToMap(Message telecommand) {
		return Map.of("class_name", "%sTelemetryPayload".formatted(telecommand.getName().upperCamelCase()));
	}

	private static Map<String, Object> messageTypeToMap(MessageType type) {
		return Map.of(
				"corfu_type",
				type.reference().corfuType().name(),
				"count",
				type.count(),
				"java_type",
				type.reference().simpleName(),
				"is_array",
				type instanceof ArrayMessageType,
				"lowercamelcase_name",
				type.getName().lowerCamelCase()
		);
	}
}
