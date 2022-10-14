package de.wuespace.telestion.project.corfu.sample.pkg.corfu.verticle.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeserializerResetCommand() implements DeserializerCommand {
}
