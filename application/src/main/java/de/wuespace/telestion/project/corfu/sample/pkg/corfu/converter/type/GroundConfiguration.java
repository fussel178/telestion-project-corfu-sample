package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroundConfiguration {

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class StandardTelemetry {
		@JsonProperty("app")
		public String app;

		@JsonProperty("subserviceId")
		public short payloadId = 0;

		@Override
		public String toString() {
			return "StandardTelemetry{" +
					"app='" + app + '\'' +
					", payloadId=" + payloadId +
					'}';
		}
	}

	@JsonProperty("eventTopic")
	public String eventTopic;

	@JsonProperty("telemetryTopic")
	public String telemetryTopic;

	@JsonProperty("telecommandTopic")
	public String telecommandTopic;

	@JsonProperty("downlinkPort")
	public int downlinkPort;

	@JsonProperty("uplinkPort")
	public int uplinkPort;

	@JsonProperty("standardTelemetry")
	public StandardTelemetry standardTelemetry;

	@Override
	public String toString() {
		return "GroundConfiguration{" +
				"eventTopic='" + eventTopic + '\'' +
				", telemetryTopic='" + telemetryTopic + '\'' +
				", telecommandTopic='" + telecommandTopic + '\'' +
				", downlinkPort=" + downlinkPort +
				", uplinkPort=" + uplinkPort +
				", standardTelemetry=" + standardTelemetry +
				'}';
	}
}
