package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.timemanager;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.wuespace.telestion.project.corfu.sample.converter.message.AppTelecommandPayload;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = SetUtcTelecommandPayload.class, name = SetUtcTelecommandPayload.TELECOMMAND_PAYLOAD_NAME),
		@JsonSubTypes.Type(value = SetClockDriftTelecommandPayload.class, name = SetClockDriftTelecommandPayload.TELECOMMAND_PAYLOAD_NAME)
})
public interface TimeManagerTelecommandPayload extends AppTelecommandPayload {
}
