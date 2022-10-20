package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template;

import com.hubspot.jinjava.Jinjava;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.type.*;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.*;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.*;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeRegistrar;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeStore;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JinjaTemplateEngine implements TemplateEngine {

	private final Jinjava engine;
	private final TemplateProvider provider;
	private final Map<String, Object> globalContext;

	public JinjaTemplateEngine(TemplateProvider provider) {
		this.engine = new Jinjava();
		this.provider = provider;
		this.globalContext = new HashMap<>();

		globalContext.put("corfu_struct_binary_name", CorfuStruct.class.getName());
		globalContext.put("app_standard_telemetry_binary_name", AppStandardTelemetry.class.getName());
		globalContext.put("app_telecommand_payload_binary_name", AppTelecommandPayload.class.getName());
		globalContext.put("app_telemetry_payload_binary_name", AppTelemetryPayload.class.getName());
		globalContext.put("corfu_app_telecommand_binary_name", CorfuAppTelecommand.class.getName());
		globalContext.put("corfu_app_telemetry_binary_name", CorfuAppTelemetry.class.getName());
		globalContext.put("corfu_hardware_binary_name", CorfuHardware.class.getName());
		globalContext.put("corfu_node_binary_name", CorfuNode.class.getName());
		globalContext.put("corfu_property_binary_name", CorfuProperty.class.getName());
		globalContext.put("message_type_registrar_binary_name", MessageTypeRegistrar.class.getName());
		globalContext.put("message_type_store_binary_name", MessageTypeStore.class.getName());
	}

	@Override
	public StructRendering renderAppStructRecord(Package pkg, MessageFields config) {
		var context = createContext(pkg, config);

		// create record elements
		List<?> recordElements = config.content.stream().map(JinjaTemplateEngine::messageTypeToMap).toList();
		context.put("record_elements", recordElements);

		return new StructRendering(
				engine.render(provider.getAppStructRecordTemplate(), context),
				"%sStruct".formatted(config.getName().upperCamelCase()),
				pkg,
				config
		);
	}

	@Override
	public AppRendering renderAppStandardTelemetryRecord(Package pkg, AppConfiguration config) {
		var context = createContext(pkg, config);

		// create record elements
		List<?> recordElements = config.standardTelemetry.content.stream().map(JinjaTemplateEngine::messageTypeToMap).toList();
		context.put("record_elements", recordElements);

		return new AppRendering(
				engine.render(provider.getAppStandardTelemetryRecordTemplate(), context),
				"%sStandardTelemetry".formatted(config.getName().upperCamelCase()),
				pkg,
				config
		);
	}

	@Override
	public PayloadRendering renderTelecommandPayloadRecord(Package pkg, Message config) {
		var context = createContext(pkg, config);

		// create record elements
		List<?> recordElements = config.fields.content.stream().map(JinjaTemplateEngine::messageTypeToMap).toList();
		context.put("record_elements", recordElements);

		return new PayloadRendering(
				engine.render(provider.getTelecommandPayloadRecordTemplate(), context),
				"%sTelecommandPayload".formatted(config.getName().upperCamelCase()),
				pkg,
				config
		);
	}

	@Override
	public PayloadRendering renderTelemetryPayloadRecord(Package pkg, Message config) {
		var context = createContext(pkg, config);

		// create record elements
		List<?> recordElements = config.fields.content.stream().map(JinjaTemplateEngine::messageTypeToMap).toList();
		context.put("record_elements", recordElements);

		return new PayloadRendering(
				engine.render(provider.getTelemetryPayloadRecordTemplate(), context),
				"%sTelemetryPayload".formatted(config.getName().upperCamelCase()),
				pkg,
				config
		);
	}

	@Override
	public AppRendering renderAppTelecommandPayloadInterface(Package pkg, AppConfiguration config) {
		var context = createContext(pkg, config);

		// create telecommand payload list
		List<?> payloads = config.telecommands.values().stream().map(JinjaTemplateEngine::telecommandToMap).toList();
		context.put("payloads", payloads);

		return new AppRendering(
				engine.render(provider.getAppTelecommandPayloadInterfaceTemplate(), context),
				"%sTelecommandPayload".formatted(config.getName().upperCamelCase()),
				pkg,
				config
		);
	}

	@Override
	public AppRendering renderAppTelemetryPayloadInterface(Package pkg, AppConfiguration config) {
		return renderAppTelemetryPayloadInterface(pkg, config, Collections.emptyList());
	}

	@Override
	public AppRendering renderAppTelemetryPayloadInterface(Package pkg, AppConfiguration config, List<NodeRendering> nodePayloads) {
		var context = createContext(pkg, config);

		// create telecommand payload list
		List<?> payloads = config.extendedTelemetry.values().stream().map(JinjaTemplateEngine::telemetryToMap).toList();
		context.put("payloads", payloads);

		List<?> mapped = nodePayloads.stream().map(JinjaTemplateEngine::renderedNodeToMap).toList();
		context.put("node_payloads", mapped);

		return new AppRendering(
				engine.render(provider.getAppTelemetryPayloadInterfaceTemplate(), context),
				"%sTelemetryPayload".formatted(config.getName().upperCamelCase()),
				pkg,
				config
		);
	}

	@Override
	public AppRendering renderAppTelecommandRecord(Package pkg, AppConfiguration config) {
		var context = createContext(pkg, config);

		return new AppRendering(
				engine.render(provider.getAppTelecommandRecordTemplate(), context),
				"%sTelecommand".formatted(config.getName().upperCamelCase()),
				pkg,
				config
		);
	}

	@Override
	public AppRendering renderAppTelemetryRecord(Package pkg, AppConfiguration config) {
		var context = createContext(pkg, config);

		return new AppRendering(
				engine.render(provider.getAppTelemetryRecordTemplate(), context),
				"%sTelemetry".formatted(config.getName().upperCamelCase()),
				pkg,
				config
		);
	}

	@Override
	public NodeRendering renderNodeRecord(Package pkg, NodeConfiguration config) {
		var context = createContext(pkg, config);

		// create hardware list
		List<?> hardwareTargets = config.hardwareTargets.entrySet().stream()
				.map(JinjaTemplateEngine::hardwareToMap)
				.toList();

		context.put("hardware_targets", hardwareTargets);

		return new NodeRendering(
				engine.render(provider.getNodeRecordTemplate(), context),
				"%sNode".formatted(config.getName().upperCamelCase()),
				pkg,
				config
		);
	}

	@Override
	public NodeRendering renderNodeStandardTelemetryRecord(
			Package pkg,
			NodeConfiguration config,
			List<AppRendering> appStandardTelemetryRenderings,
			AppRendering payloadInterface
	) {
		var context = createContext(pkg, config);

		List<?> standardTelemetries = appStandardTelemetryRenderings.stream()
				.map(JinjaTemplateEngine::standardTelemetryToMap)
				.toList();

		context.put("standard_telemetries", standardTelemetries);

		context.put("standard_telemetry_app", Map.of(
						"payload_interface_binary_name",
						payloadInterface.binaryName(),
						"payload_id",
						payloadInterface.config().getStandardTelemetryPayloadId(),
						"uppercamelcase_name",
						payloadInterface.config().getName().upperCamelCase()
				)
		);

		return new NodeRendering(
				engine.render(provider.getNodeStandardTelemetryRecordTemplate(), context),
				"%sStandardTelemetryPayload".formatted(config.getName().upperCamelCase()),
				pkg,
				config
		);
	}

	@Override
	public PlainRendering renderRegistrarClass(
			Package pkg,
			List<NodeRendering> renderedNodes,
			List<AppRendering> renderedAppTelecommands,
			List<AppRendering> renderedAppTelemetries,
			List<PayloadRendering> renderedTelecommandPayloads,
			List<PayloadRendering> renderedTelemetryPayloads
	) {
		var context = createContext(pkg);

		List<?> nodes = renderedNodes.stream().map(JinjaTemplateEngine::renderedNodeToMap).toList();
		List<?> appTelecommands = renderedAppTelecommands.stream().map(JinjaTemplateEngine::renderedAppTelecommandToMap).toList();
		List<?> appTelemetries = renderedAppTelemetries.stream().map(JinjaTemplateEngine::renderedAppTelemetryToMap).toList();
		List<?> telecommandPayloads = renderedTelecommandPayloads.stream().map(JinjaTemplateEngine::renderedTelecommandPayloadToMap).toList();
		List<?> telemetryPayloads = renderedTelemetryPayloads.stream().map(JinjaTemplateEngine::renderedTelemetryPayloadToMap).toList();

		context.put("nodes", nodes);
		context.put("app_telecommands", appTelecommands);
		context.put("app_telemetries", appTelemetries);
		context.put("telecommand_payloads", telecommandPayloads);
		context.put("telemetry_payloads", telemetryPayloads);

		return new PlainRendering(
				engine.render(provider.getRegistrarClassTemplate(), context),
				"GeneratedRegistrar",
				pkg
		);
	}

	private Map<String, Object> createContext(Package pkg) {
		Map<String, Object> context = new HashMap<>(globalContext);
		context.put("package", pkg.binaryName());
		return context;
	}

	private Map<String, Object> createContext(Package pkg, Identifiable config) {
		Map<String, Object> context = new HashMap<>(globalContext);

		context.put("package", pkg.binaryName());
		context.put("raw_name", config.getName().raw());
		context.put("uppercamelcase_name", config.getName().upperCamelCase());
		context.put("lowercamelcase_name", config.getName().lowerCamelCase());
		context.put("description", config.getDescription());
		context.put("id", config.getId());

		return context;
	}

	private Map<String, Object> createContext(Package pkg, MessageFields config) {
		Map<String, Object> context = new HashMap<>(globalContext);

		context.put("package", pkg.binaryName());
		context.put("raw_name", config.getName().raw());
		context.put("uppercamelcase_name", config.getName().upperCamelCase());
		context.put("lowercamelcase_name", config.getName().lowerCamelCase());
		context.put("app_raw_name", config.getAssociatedApp().getName().raw());
		context.put("app_uppercamelcase_name", config.getAssociatedApp().getName().upperCamelCase());
		context.put("app_lowercamelcase_name", config.getAssociatedApp().getName().lowerCamelCase());

		return context;
	}

	private Map<String, Object> createContext(Package pkg, Message config) {
		var context = createContext(pkg, (Identifiable) config);

		context.put("app_raw_name", config.getAssociatedApp().getName().raw());
		context.put("app_uppercamelcase_name", config.getAssociatedApp().getName().upperCamelCase());
		context.put("app_lowercamelcase_name", config.getAssociatedApp().getName().lowerCamelCase());

		return context;
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

	private static Map<String, Object> standardTelemetryToMap(AppRendering rendering) {
		return Map.of(
				"binary_name",
				rendering.binaryName(),
				"class_name",
				rendering.className(),
				"name",
				rendering.config().getName().lowerCamelCase()
		);
	}

	private static Map<String, Object> renderedNodeToMap(NodeRendering rendering) {
		return Map.of(
				"binary_name",
				rendering.binaryName(),
				"class_name",
				rendering.className(),
				"uppercamelcase_name",
				rendering.config().getName().upperCamelCase()
		);
	}

	private static Map<String, Object> renderedAppTelecommandToMap(AppRendering rendering) {
		return Map.of(
				"binary_name",
				rendering.binaryName(),
				"class_name",
				rendering.className()
		);
	}

	private static Map<String, Object> renderedAppTelemetryToMap(AppRendering rendering) {
		return Map.of(
				"binary_name",
				rendering.binaryName(),
				"class_name",
				rendering.className()
		);
	}

	private static Map<String, Object> renderedTelecommandPayloadToMap(PayloadRendering rendering) {
		return Map.of(
				"binary_name",
				rendering.binaryName(),
				"class_name",
				rendering.className(),
				"app_class_name",
				"%sTelecommand".formatted(rendering.config().getAssociatedApp().getName().upperCamelCase())
		);
	}

	private static Map<String, Object> renderedTelemetryPayloadToMap(PayloadRendering rendering) {
		return Map.of(
				"binary_name",
				rendering.binaryName(),
				"class_name",
				rendering.className(),
				"app_class_name",
				"%sTelemetry".formatted(rendering.config().getAssociatedApp().getName().upperCamelCase())
		);
	}
}
