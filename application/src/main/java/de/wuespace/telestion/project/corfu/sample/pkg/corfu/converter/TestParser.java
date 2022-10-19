package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.exception.ParsingException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.fs.ProjectGeneratorFilesystem;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.JinjaTemplateEngine;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.ResourceTemplateProvider;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Steps in which order to generate Corfu definitions:
 * for each app:
 *    generate structs (records)
 *    generate standard telemetry (record)
 *    generate all telecommand payloads (records)
 *    generate all telemetry payloads (records)
 *    generate telecommand payload interface (interface)
 *    generate telemetry payload interface (interface)
 *    generate app telecommand (record)
 *    generate app telemetry (record)
 * for each node:
 *    generate node (record)
 *    generate node standard telemetry as telemetry payload interface from std telemetry app (record, DON'T forget nodeId)
 * generate standard telemetry payload interface in defined app
 */
public class TestParser {
	public static void main(String[] args) throws ParsingException, IOException {
		// read and connect Corfu configuration from target project
		var projectRootDir = Path.of("/home/ludwig/Coding/Private/telestion-corfu-ba/corfu-example-obsw");
		// var projectRootDir = Path.of("/home/ludwig/Coding/Private/telestion-corfu-ba/corfu-config-converter/sample/innocube");
		var parser = new CorfuConfigParser(YAML.getMapper());
		var config = parser.getProjectConfig(projectRootDir);

		// generate Corfu messages based on Corfu configuration
		var outputDir = Path.of("/home/ludwig/Coding/Private/telestion-corfu-ba/telestion-project-corfu-sample/application/src/main/java");
		var basePkg = new Package("de.wuespace.telestion.project.corfu.sample.auto");
		var fs = new ProjectGeneratorFilesystem(outputDir);
		var engine = new JinjaTemplateEngine(new ResourceTemplateProvider());
		var generator = new CorfuMessageGenerator(fs, engine);
		generator.generate(basePkg, config);
	}
}
