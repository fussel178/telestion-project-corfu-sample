package de.wuespace.telestion.project.corfu.sample.old.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.wuespace.telestion.project.corfu.sample.old.converter.message.CorfuMessage;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuProperty;

import java.util.List;

public record MyCorfuMessage(
		@JsonProperty @CorfuProperty(CorfuProperty.Type.INT32) int id,
		@JsonProperty @CorfuProperty(CorfuProperty.Type.FLOAT) float temp,
		@JsonProperty @CorfuProperty(CorfuProperty.Type.BOOLEAN) boolean enabled,
		@JsonProperty @CorfuProperty(CorfuProperty.Type.DOUBLE) double precision,
		@JsonProperty @CorfuProperty(CorfuProperty.Type.UINT32) long unsigned,
		@JsonProperty @CorfuProperty CorfuSubMessage subMessage,
		@JsonProperty @CorfuProperty(count = 10) List<CorfuListMessage> list
) implements CorfuMessage {
}
