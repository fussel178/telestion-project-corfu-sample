package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Corfu telemetry message received from an onboard target.
 *
 * @param packetNumber the sequential number of the telemetry message
 * @param timeUTC      the UTC time of the telemetry creation in nanoseconds
 * @param uptime       the uptime of the node during telemetry creation in nanoseconds
 * @param isHistoric   indicates whether the telemetry packet contains historic data
 * @param node         the node that sent this telemetry message
 * @param app          the app on the node that sent this telemetry message
 *                     (including the actual payload information)
 * @author Ludwig Richter (@fussel178)
 */
public record CorfuTelemetry(
		@JsonProperty int packetNumber,
		@JsonProperty long timeUTC,
		@JsonProperty long uptime,
		@JsonProperty boolean isHistoric,
		@JsonProperty CorfuNode node,
		@JsonProperty CorfuAppTelemetry app
) implements CorfuMessage {
}
