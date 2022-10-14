package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.exception.ParsingException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser.CorfuConfigParser;

import java.nio.file.Path;

public class TestParser {
	public static void main(String[] args) throws ParsingException {
		var projectRootDir = Path.of("/home/ludwig/Coding/Private/telestion-corfu-ba/corfu-example-obsw");

		var parser = new CorfuConfigParser(YAML.getMapper());

		var config = parser.getProjectConfig(projectRootDir);

		System.out.println(config.apps.get(1).structs);
	}
}
