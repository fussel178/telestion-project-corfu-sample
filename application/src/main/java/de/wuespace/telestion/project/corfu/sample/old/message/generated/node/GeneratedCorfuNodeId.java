package de.wuespace.telestion.project.corfu.sample.old.message.generated.node;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.wuespace.telestion.project.corfu.sample.old.message.CorfuNodeId;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "name")
@JsonSubTypes({
		@JsonSubTypes.Type(value = CorfuAdcsNodeNode.class, name = "adcs-node")
})
public interface GeneratedCorfuNodeId extends CorfuNodeId {
}
