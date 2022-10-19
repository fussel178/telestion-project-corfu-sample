package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Implements the {@link TemplateProvider} interface and fetches the templates from the Java resources.
 *
 * @author Ludwig Richter (@fussel178)
 */
public class ResourceTemplateProvider implements TemplateProvider {

	public static final Path TEMPLATE_DIR = Path.of("templates");

	public static final Path APP_STRUCT_RECORD_TEMPLATE =
			TEMPLATE_DIR.resolve("appStructRecord.java.jinja");
	public static final Path APP_STANDARD_TELEMETRY_RECORD_TEMPLATE =
			TEMPLATE_DIR.resolve("appStandardTelemetryRecord.java.jinja");
	public static final Path TELECOMMAND_PAYLOAD_RECORD_TEMPLATE =
			TEMPLATE_DIR.resolve("telecommandPayloadRecord.java.jinja");
	public static final Path TELEMETRY_PAYLOAD_RECORD_TEMPLATE =
			TEMPLATE_DIR.resolve("telemetryPayloadRecord.java.jinja");
	public static final Path APP_TELECOMMAND_PAYLOAD_INTERFACE_TEMPLATE =
			TEMPLATE_DIR.resolve("appTelecommandPayloadInterface.java.jinja");
	public static final Path APP_TELEMETRY_PAYLOAD_INTERFACE_TEMPLATE =
			TEMPLATE_DIR.resolve("appTelemetryPayloadInterface.java.jinja");
	public static final Path APP_TELECOMMAND_RECORD_TEMPLATE =
			TEMPLATE_DIR.resolve("appTelecommandRecord.java.jinja");
	public static final Path APP_TELEMETRY_RECORD_TEMPLATE =
			TEMPLATE_DIR.resolve("appTelemetryRecord.java.jinja");
	public static final Path NODE_RECORD_TEMPLATE =
			TEMPLATE_DIR.resolve("nodeRecord.java.jinja");
	public static final Path NODE_STANDARD_TELEMETRY_RECORD_TEMPLATE =
			TEMPLATE_DIR.resolve("nodeStandardTelemetryRecord.java.jinja");
	public static final Path REGISTRAR_CLASS_TEMPLATE =
			TEMPLATE_DIR.resolve("registrarClass.java.jinja");

	private String appStructRecordTemplate;
	private String appStandardTelemetryRecordTemplate;
	private String telecommandPayloadRecordTemplate;
	private String telemetryPayloadRecordTemplate;
	private String appTelecommandPayloadInterfaceTemplate;
	private String appTelemetryPayloadInterfaceTemplate;
	private String appTelecommandRecordTemplate;
	private String appTelemetryRecordTemplate;
	private String nodeRecordTemplate;
	private String nodeStandardTelemetryRecordTemplate;
	private String registrarClassTemplate;

	/**
	 * Fetches the templates from the Java resources.
	 */
	public void fetchTemplates() {
		appStructRecordTemplate = loadResource(APP_STRUCT_RECORD_TEMPLATE);
		appStandardTelemetryRecordTemplate = loadResource(APP_STANDARD_TELEMETRY_RECORD_TEMPLATE);
		telecommandPayloadRecordTemplate = loadResource(TELECOMMAND_PAYLOAD_RECORD_TEMPLATE);
		telemetryPayloadRecordTemplate = loadResource(TELEMETRY_PAYLOAD_RECORD_TEMPLATE);
		appTelecommandPayloadInterfaceTemplate = loadResource(APP_TELECOMMAND_PAYLOAD_INTERFACE_TEMPLATE);
		appTelemetryPayloadInterfaceTemplate = loadResource(APP_TELEMETRY_PAYLOAD_INTERFACE_TEMPLATE);
		appTelecommandRecordTemplate = loadResource(APP_TELECOMMAND_RECORD_TEMPLATE);
		appTelemetryRecordTemplate = loadResource(APP_TELEMETRY_RECORD_TEMPLATE);
		nodeRecordTemplate = loadResource(NODE_RECORD_TEMPLATE);
		nodeStandardTelemetryRecordTemplate = loadResource(NODE_STANDARD_TELEMETRY_RECORD_TEMPLATE);
		registrarClassTemplate = loadResource(REGISTRAR_CLASS_TEMPLATE);
	}

	@Override
	public String getAppStructRecordTemplate() {
		if (Objects.isNull(appStructRecordTemplate)) {
			fetchTemplates();
		}

		return appStructRecordTemplate;
	}

	@Override
	public String getAppStandardTelemetryRecordTemplate() {
		if (Objects.isNull(appStandardTelemetryRecordTemplate)) {
			fetchTemplates();
		}

		return appStandardTelemetryRecordTemplate;
	}

	@Override
	public String getTelecommandPayloadRecordTemplate() {
		if (Objects.isNull(telecommandPayloadRecordTemplate)) {
			fetchTemplates();
		}
		return telecommandPayloadRecordTemplate;
	}

	@Override
	public String getTelemetryPayloadRecordTemplate() {
		if (Objects.isNull(telemetryPayloadRecordTemplate)) {
			fetchTemplates();
		}

		return telemetryPayloadRecordTemplate;
	}

	@Override
	public String getAppTelecommandPayloadInterfaceTemplate() {
		if (Objects.isNull(appTelecommandPayloadInterfaceTemplate)) {
			fetchTemplates();
		}

		return appTelecommandPayloadInterfaceTemplate;
	}

	@Override
	public String getAppTelemetryPayloadInterfaceTemplate() {
		if (Objects.isNull(appTelemetryPayloadInterfaceTemplate)) {
			fetchTemplates();
		}

		return appTelemetryPayloadInterfaceTemplate;
	}

	@Override
	public String getAppTelecommandRecordTemplate() {
		if (Objects.isNull(appTelecommandRecordTemplate)) {
			fetchTemplates();
		}

		return appTelecommandRecordTemplate;
	}

	@Override
	public String getAppTelemetryRecordTemplate() {
		if (Objects.isNull(appTelemetryRecordTemplate)) {
			fetchTemplates();
		}

		return appTelemetryRecordTemplate;
	}

	@Override
	public String getNodeRecordTemplate() {
		if (Objects.isNull(nodeRecordTemplate)) {
			fetchTemplates();
		}

		return nodeRecordTemplate;
	}

	@Override
	public String getNodeStandardTelemetryRecordTemplate() {
		if (Objects.isNull(nodeStandardTelemetryRecordTemplate)) {
			fetchTemplates();
		}

		return nodeStandardTelemetryRecordTemplate;
	}

	@Override
	public String getRegistrarClassTemplate() {
		if (Objects.isNull(registrarClassTemplate)) {
			fetchTemplates();
		}

		return registrarClassTemplate;
	}

	private String loadResource(Path relativePath) {
		ClassLoader loader = ResourceTemplateProvider.class.getClassLoader();

		try (var stream = loader.getResourceAsStream(relativePath.toString())) {
			assert stream != null;
			return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new IllegalArgumentException("The template %s cannot be found in the module resources."
					.formatted(relativePath), e);
		}
	}
}
