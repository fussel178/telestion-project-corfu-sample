package de.wuespace.telestion.project.corfu.sample.old.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.project.corfu.sample.old.converter.message.CorfuMessage;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuProperty;

public record CorfuListMessage(
		@JsonProperty @CorfuProperty(CorfuProperty.Type.INT32) int item
) implements CorfuMessage {
}
