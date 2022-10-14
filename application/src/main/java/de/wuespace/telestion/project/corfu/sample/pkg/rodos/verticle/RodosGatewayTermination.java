package de.wuespace.telestion.project.corfu.sample.pkg.rodos.verticle;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.verticle.TelestionConfiguration;
import de.wuespace.telestion.api.verticle.TelestionVerticle;
import de.wuespace.telestion.api.verticle.trait.WithEventBus;
import de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.message.RodosNetworkMessage;
import de.wuespace.telestion.project.corfu.sample.pkg.util.TimeUtils;
import de.wuespace.telestion.services.connection.rework.RawMessage;

import java.time.Instant;

@SuppressWarnings("unused")
public class RodosGatewayTermination extends TelestionVerticle<RodosGatewayTermination.Configuration>
		implements WithEventBus {

	public static final short DEFAULT_MAX_STEPS_TO_FORWARD = 10;

	public record Configuration(
			@JsonProperty String gatewayInAddress,
			@JsonProperty String gatewayOutAddress,
			@JsonProperty String dataInAddress,
			@JsonProperty String dataOutAddress,
			@JsonProperty int senderNode,
			@JsonProperty long senderThreadId,
			@JsonProperty long topicId
	) implements TelestionConfiguration {
	}

	@Override
	public void onStart() throws Exception {
		logger.info("New Rodos gateway member: SenderNode={}, SenderThreadId={}, TopicId={}",
				getConfig().senderNode(),
				getConfig().senderThreadId(),
				getConfig().topicId()
		);

		register(getConfig().gatewayInAddress(), this::handleGatewayMessage, RodosNetworkMessage.class);
		register(getConfig().dataInAddress(), this::handleDataMessage, RawMessage.class);

		logger.info("Ready for gateway termination");
	}

	public void handleGatewayMessage(RodosNetworkMessage message) {
		logger.debug("Terminate new network message");
		publish(getConfig().dataOutAddress(), new RawMessage(message.userData()));
	}

	public void handleDataMessage(RawMessage message) {
		logger.debug("Generate new network message");
		var sentTime = Instant.now();
		var RodosNetworkMessage = new RodosNetworkMessage(getConfig().senderNode(),
				TimeUtils.toNanoSeconds(sentTime),
				getConfig().senderThreadId(),
				getConfig().topicId(),
				DEFAULT_MAX_STEPS_TO_FORWARD,
				message.data()
		);
		publish(getConfig().gatewayOutAddress(), RodosNetworkMessage);
	}
}
