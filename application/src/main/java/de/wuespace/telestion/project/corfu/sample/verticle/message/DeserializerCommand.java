package de.wuespace.telestion.project.corfu.sample.verticle.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.wuespace.telestion.api.message.JsonMessage;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = DeserializerResetCommand.class, name = "reset")
})
public interface DeserializerCommand extends JsonMessage {
}
