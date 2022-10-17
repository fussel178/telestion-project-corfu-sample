package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Corfu telecommand message ready for transmission to an onboard target.
 *
 * @param commandIndex    the index of the command message
 * @param sequenceCounter the sequential number of the telecommand message
 * @param timeToExecute   the time on which the telecommand should be executed
 *                        (<code><0</code> defines a relative time,
 *                        <code>>0</code> defines an absolute time,
 *                        <code>0</code> means immediate execution)
 * @param node            the node that should receive this telecommand message
 * @param app             the app on the node that should receive this telecommand message
 *                        (including the actual payload information)
 * @author Ludwig Richter (@fussel178)
 */
public record CorfuTelecommand(
		@JsonProperty short commandIndex,
		@JsonProperty short sequenceCounter,
		@JsonProperty long timeToExecute,
		@JsonProperty CorfuNode node,
		@JsonProperty CorfuAppTelecommand app
) implements CorfuMessage {
}
