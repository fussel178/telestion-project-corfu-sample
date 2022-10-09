package de.wuespace.telestion.project.corfu.sample.verticle.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SerializerResetCommand() implements SerializerCommand {
}
