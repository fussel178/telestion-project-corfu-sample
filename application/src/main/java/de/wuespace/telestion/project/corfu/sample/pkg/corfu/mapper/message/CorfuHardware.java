package de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message;

/**
 * Represents a hardware target associated with a {@link CorfuNode Corfu node}.
 * Every hardware target from a Corfu node should implement this interface.
 *
 * @author Ludwig Richter (@fussel178)
 */
public interface CorfuHardware {
	/**
	 * Returns the hardware target id as defined in the Corfu configuration as unsigned byte.
	 * <p>
	 * Note: Returning <code>0xFF</code> means "broadcast" message to all hardware targets
	 * that run the associated node.
	 */
	short id();
}
