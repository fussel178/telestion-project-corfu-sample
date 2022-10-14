package de.wuespace.telestion.project.corfu.sample.generated.osbw.node;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuHardware;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ObcNode(ObcHardware hardware) implements CorfuNode {

	public static final String NODE_NAME = "obc";
	public static final short NODE_ID = 0x50;

	public enum ObcHardware implements CorfuHardware {
		MT0_ID0((short) 0x13),
		BROADCAST((short) 0xFF);

		private final short id;

		ObcHardware(short id) {
			this.id = id;
		}

		@Override
		public short id() {
			return id;
		}
	}

	@Override
	public short id() {
		return NODE_ID;
	}

	@Override
	public String name() {
		return NODE_NAME;
	}
}
