package de.wuespace.telestion.project.corfu.sample.old.message.generated.node;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CorfuAdcsNodeNode(
		@JsonProperty Hardware hardware
) implements GeneratedCorfuNodeId {

	public enum Hardware {
		// the hardware that can run the parent node
		ID0, ID1, BROADCAST;

		/**
		 * Converts a AdcsNode Hardware type to its associated hardware byte id.
		 * @param hardware the hardware type
		 * @return the associated hardware byte id
		 */
		public static byte toId(Hardware hardware) {
			return hardware.getId();
		}

		/**
		 * Converts a AdcsNode Hardware byte id to its associated hardware type.
		 * @param id the hardware byte id
		 * @return the associated hardware type
		 */
		public static Hardware toType(byte id) {
			return switch (id) {
				case (byte) 0x01 -> ID0;
				case (byte) 0x02 -> ID1;
				case (byte) 0xFF -> BROADCAST;
				default -> throw new IllegalArgumentException(("Unknown hardware byte id 0x%02X. Please add the " +
						"missing hardware to the AdcsNode or reload the configuration, and try again.").formatted(id));
			};
		}

		/**
		 * Returns the associated byte id of this hardware type.
		 */
		public byte getId() {
			return switch (this) {
				case ID0 -> (byte) 0x01;
				case ID1 -> (byte) 0x02;
				case BROADCAST -> (byte) 0xFF;
			};
		}

		/**
		 * Returns <code>true</code>, when the hardware id is a broadcast.
		 */
		public boolean isBroadcast() {
			return this == BROADCAST;
		}
	}

	@Override
	public byte getId() {
		return 0x40;
	}

	@Override
	public boolean isHardwareBroadcast() {
		return hardware.isBroadcast();
	}
}
