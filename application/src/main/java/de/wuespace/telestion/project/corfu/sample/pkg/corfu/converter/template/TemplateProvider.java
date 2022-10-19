package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template;

/**
 * A template provider resolves and loads the template files for the {@link JinjaTemplateEngine}
 * and serves the template upon request.
 *
 * @author Ludwig Richter (@fussel178)
 */
public interface TemplateProvider {
	/**
	 * Returns the template that represents an application struct.
	 */
	String getAppStructRecordTemplate();

	/**
	 * Returns the template that represents an application standard telemetry record.
	 */
	String getAppStandardTelemetryRecordTemplate();

	/**
	 * Returns the template that represents a specific telecommand payload.
	 */
	String getTelecommandPayloadRecordTemplate();

	/**
	 * Returns the template that represents a specific telemetry payload.
	 */
	String getTelemetryPayloadRecordTemplate();

	/**
	 * Returns the template that represents a combining interface of all specific telecommand payloads.
	 */
	String getAppTelecommandPayloadInterfaceTemplate();

	/**
	 * Returns the template that represents a combining interface of all specific telemetry payloads.
	 */
	String getAppTelemetryPayloadInterfaceTemplate();

	/**
	 * Returns the template that represents an application telecommand.
	 */
	String getAppTelecommandRecordTemplate();

	/**
	 * Returns the template that represents an application telemetry.
	 */
	String getAppTelemetryRecordTemplate();

	/**
	 * Returns the template that represents a Corfu node.
	 */
	String getNodeRecordTemplate();

	/**
	 * Returns the template that represents a Corfu node standard telemetry.
	 */
	String getNodeStandardTelemetryRecordTemplate();

	/**
	 * Returns the template that represents the automatically generated registrar that loads the generated records
	 * and interfaces into a
	 * {@link de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.store.MessageTypeStore MessageTypeStore}.
	 */
	String getRegistrarClassTemplate();
}
