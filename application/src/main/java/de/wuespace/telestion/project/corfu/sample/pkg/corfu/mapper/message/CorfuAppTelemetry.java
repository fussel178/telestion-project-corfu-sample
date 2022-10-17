package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

/**
 * Represents a Corfu application as telemetry specialization.
 * Every application that send telemetry messages in the Corfu configuration
 * should implement this interface.
 *
 * @author Ludwig Richter (@fussel178)
 */
public interface CorfuAppTelemetry extends CorfuApp {
	/**
	 * The name of the JSON object property that represents the application name.
	 */
	String IDENTIFIER_PROPERTY = "type";

	/**
	 * The name of the JSON object property that represents the stored payload.
	 */
	String PAYLOAD_PROPERTY = "payload";

	/**
	 * Returns the telemetry payload that this app sent from the onboard target.
	 */
	AppTelemetryPayload payload();
}
