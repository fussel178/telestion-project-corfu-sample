package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.generator.type;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.mapper.message.CorfuProperty;

public record PayloadEntry(
		CorfuProperty.Type type,
		int count,
		String name,
		EntryValueType valueType
) {
}
