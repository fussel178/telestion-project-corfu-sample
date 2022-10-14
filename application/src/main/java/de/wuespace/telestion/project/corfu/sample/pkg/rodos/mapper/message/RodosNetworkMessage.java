package de.wuespace.telestion.project.corfu.sample.pkg.rodos.mapper.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.api.message.JsonMessage;

/**
 * Represents a network message that is transmitted and received by the RODOS gateway.
 *
 * @param senderNode        the sending node in the gateway network
 * @param sentTime          time of the sender node on which this network message was sent
 * @param senderThreadId    the thread in the sending node which emits this network message
 * @param topicId           the topic id which emits the user data in this network message
 * @param maxStepsToForward maximum hops the message should take before it should be discarded by the routing participant
 * @param userData          the actual user data from the forwarding topic
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record RodosNetworkMessage(
		@JsonProperty int senderNode,
		@JsonProperty long sentTime,
		@JsonProperty long senderThreadId,
		@JsonProperty long topicId,
		@JsonProperty short maxStepsToForward,
		@JsonProperty byte[] userData
) implements JsonMessage {
}
