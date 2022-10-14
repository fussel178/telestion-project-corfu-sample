package de.wuespace.telestion.project.corfu.sample.verticle;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.verticle.TelestionConfiguration;
import de.wuespace.telestion.api.verticle.TelestionVerticle;
import de.wuespace.telestion.api.verticle.trait.WithEventBus;
import de.wuespace.telestion.project.corfu.sample.pkg.util.ByteArrayUtils;
import de.wuespace.telestion.services.connection.rework.RawMessage;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramPacket;
import io.vertx.core.datagram.DatagramSocket;

import java.util.Objects;

@SuppressWarnings("unused")
public class UDPTransmitter extends TelestionVerticle<UDPTransmitter.Configuration> implements WithEventBus {

	public record Configuration(
			@JsonProperty String inAddress,
			@JsonProperty String outAddress,
			@JsonProperty String bindHost,
			@JsonProperty String dstHost,
			@JsonProperty int uplinkPort,
			@JsonProperty int downlinkPort
	) implements TelestionConfiguration {
		public Configuration() {
			this(null, null, "127.0.0.1", "127.0.0.1", 0, 0);
		}
	}

	private DatagramSocket socket;

	@Override
	public void onStart(Promise<Void> startPromise) {
		socket = vertx.createDatagramSocket();
		socket.handler(this::handleReceive);
		socket.listen(getConfig().downlinkPort, getConfig().bindHost)
				.onSuccess(socket -> logger.info("UDP Socket open. Listening on {}:{} for incoming packets",
						getConfig().bindHost,
						getConfig().downlinkPort))
				.onSuccess(socket -> register(getConfig().inAddress, this::handleSend, RawMessage.class))
				.onFailure(cause -> logger.error("Cannot on UDP socket on {}:{}, cause: {}",
						getConfig().bindHost,
						getConfig().downlinkPort,
						cause))
				.<Void>mapEmpty().onComplete(startPromise);
	}

	@Override
	public void onStop(Promise<Void> stopPromise) {
		if (Objects.isNull(socket)) {
			stopPromise.complete();
			return;
		}

		logger.info("Closing socket now on {}:{}", getConfig().bindHost, getConfig().downlinkPort);
		socket.close().onComplete(stopPromise);
	}

	private void handleReceive(DatagramPacket packet) {
		logger.debug("Received new packet: {}", ByteArrayUtils.toString(packet.data().getBytes()));
		publish(getConfig().outAddress, new RawMessage(packet.data().getBytes()));
	}

	private void handleSend(RawMessage message) {
		logger.debug("Send new packet to host {}:{}: {}",
				getConfig().dstHost,
				getConfig().uplinkPort,
				ByteArrayUtils.toString(message.data())
		);
		socket.send(Buffer.buffer(message.data()), getConfig().uplinkPort, getConfig().dstHost);
	}
}
