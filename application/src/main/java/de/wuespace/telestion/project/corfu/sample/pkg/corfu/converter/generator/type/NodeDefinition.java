package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.generator.type;

import java.util.List;

public record NodeDefinition(
		short nodeId,
		String nodeName,
		List<HardwareDefinition> hardwareTargets
) {
}
