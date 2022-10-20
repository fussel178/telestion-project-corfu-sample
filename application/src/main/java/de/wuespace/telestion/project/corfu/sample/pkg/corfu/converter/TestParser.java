package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.exception.ParsingException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.fs.ProjectGeneratorFilesystem;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser.CorfuConfigParser;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser.ParserOptions;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser.YAML;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.JinjaTemplateEngine;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.ResourceTemplateProvider;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;

import java.io.IOException;
import java.nio.file.Path;

public class TestParser {
	public static void main(String[] args) throws ParsingException, IOException {
		// read and connect Corfu configuration from target project
		//var projectRootDir = Path.of("/home/ludwig/Coding/Private/telestion-corfu-ba/corfu-example-obsw");
		var projectRootDir = Path.of("/home/ludwig/Coding/Private/telestion-corfu-ba/corfu-config-converter/sample/innocube");

		var options = new ParserOptions().addIgnoredNode("fake-transceiver");
		var parser = new CorfuConfigParser(YAML.getMapper(), options);
		var config = parser.getProjectConfig(projectRootDir);

		// generate Corfu messages based on Corfu configuration
		var outputDir = Path.of("/home/ludwig/Coding/Private/telestion-corfu-ba/telestion-project-corfu-sample/application/generated/main/java");
		var basePkg = new Package("de.wuespace.telestion.project.corfu.sample.auto");
		var fs = new ProjectGeneratorFilesystem(outputDir);
		var engine = new JinjaTemplateEngine(new ResourceTemplateProvider());
		var generator = new CorfuMessageGenerator(fs, engine, basePkg);
		generator.generate(config);
	}
}
