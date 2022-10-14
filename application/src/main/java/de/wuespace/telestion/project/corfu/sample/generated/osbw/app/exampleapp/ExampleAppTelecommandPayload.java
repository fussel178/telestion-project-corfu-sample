package de.wuespace.telestion.project.corfu.sample.generated.osbw.app.exampleapp;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.AppTelecommandPayload;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = NopTelecommandPayload.class, name = NopTelecommandPayload.TELECOMMAND_PAYLOAD_NAME),
		@JsonSubTypes.Type(value = GenerateAnomalyTelecommandPayload.class, name = GenerateAnomalyTelecommandPayload.TELECOMMAND_PAYLOAD_NAME),
		@JsonSubTypes.Type(value = IncrementCounterTelecommandPayload.class, name = IncrementCounterTelecommandPayload.TELECOMMAND_PAYLOAD_NAME),
		@JsonSubTypes.Type(value = TwoParametersTelecommandPayload.class, name = TwoParametersTelecommandPayload.TELECOMMAND_PAYLOAD_NAME)
})
public interface ExampleAppTelecommandPayload extends AppTelecommandPayload {
}
