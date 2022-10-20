package de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.verticle.TelestionConfiguration;
import de.wuespace.telestion.api.verticle.TelestionVerticle;
import de.wuespace.telestion.api.verticle.trait.WithEventBus;
import de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.message.RodosNetworkMessage;
import de.wuespace.telestion.project.corfu.sample.pkg.rodos.verticle.RodosGatewayTermination;
import de.wuespace.telestion.project.corfu.sample.pkg.util.TimeUtils;
import de.wuespace.telestion.services.connection.rework.RawMessage;

import java.time.Instant;

public class CorfuGatewayTermination extends TelestionVerticle<CorfuGatewayTermination.Configuration>
		implements WithEventBus {

	public record Configuration(
			@JsonProperty String gatewayInAddress,
			@JsonProperty String gatewayOutAddress,
			@JsonProperty String rawEventOutAddress,
			@JsonProperty String rawTelemetryOutAddress,
			@JsonProperty String rawTelecommandInAddress,
			@JsonProperty int senderNode,
			@JsonProperty long senderThreadId,
			@JsonProperty long eventTopicId,
			@JsonProperty long downlinkTelemetryTopicId,
			@JsonProperty long uplinkTelecommandTopicId
	) implements TelestionConfiguration {
		public Configuration() {
			this(null, null, null, null, null, 0, 0, 0x603, 0x411, 0x401);
		}
	}

	@Override
	public void onStart() throws Exception {
		logger.info("New Rodos gateway member: SenderNode={}, SenderThreadId={}",
				getConfig().senderNode(),
				getConfig().senderThreadId()
		);

		logger.info("Corfu events are expected on topic: {}", getConfig().eventTopicId());
		logger.info("Corfu telemetry are expected on topic: {}", getConfig().downlinkTelemetryTopicId());
		logger.info("Corfu telecommand will be send on topic: {}", getConfig().uplinkTelecommandTopicId());

		register(getConfig().gatewayInAddress(), this::handleGatewayMessage, RodosNetworkMessage.class);
		register(getConfig().rawTelecommandInAddress(), this::handleTelecommand, RawMessage.class);
	}

	public void handleGatewayMessage(RodosNetworkMessage message) {
		logger.debug("New gateway message on topic: {}", message.topicId());

		if (message.topicId() == getConfig().eventTopicId()) {
			logger.debug("Received new event message");
			publish(getConfig().rawEventOutAddress(), new RawMessage(message.userData()));
		} else if (message.topicId() == getConfig().downlinkTelemetryTopicId()) {
			logger.debug("Received new downlink telemetry message");
			publish(getConfig().rawTelemetryOutAddress(), new RawMessage(message.userData()));
		} else {
			logger.debug("Message not for us. Ignoring");
		}
	}

	public void handleTelecommand(RawMessage message) {
		logger.debug("Send new telecommand on topic: {}", getConfig().uplinkTelecommandTopicId());

		var sentTime = Instant.now();
		var networkMessage = new RodosNetworkMessage(
				getConfig().senderNode(),
				TimeUtils.toNanoSeconds(sentTime),
				getConfig().senderThreadId(),
				getConfig().uplinkTelecommandTopicId(),
				RodosGatewayTermination.DEFAULT_MAX_STEPS_TO_FORWARD,
				message.data()
		);
		publish(getConfig().gatewayOutAddress(), networkMessage);
	}
}
