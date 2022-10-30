package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.generator.CorfuMessageGenerator;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser.ParsingException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.generator.ProjectGeneratorFilesystem;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser.CorfuConfigParser;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.parser.YAML;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.JinjaTemplateEngine;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.ResourceTemplateProvider;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public class CliGenerator {

	public static final String USAGE_STRING = "Usage: corfu-generator <corfu-project-root> <output-dir> <group-name>";

	public static void main(String[] args) throws ParsingException, IOException {
		if (Arrays.asList(args).contains("--help") || Arrays.asList(args).contains("-h")) {
			System.out.println(USAGE_STRING);
			System.out.flush();
			System.exit(0);
		}

		if (args.length < 3) {
			System.err.println("Missing arguments. Needed: " + 3 + " , Got: " + args.length);
			System.err.println(USAGE_STRING);
			System.err.flush();
			System.exit(1);
		}

		// read and connect Corfu configuration from target project
		var projectRootDir = Path.of(args[0]);
		var parser = new CorfuConfigParser(YAML.getMapper());
		var config = parser.getProjectConfig(projectRootDir);

		// generate Corfu messages based on Corfu configuration
		var outputDir = Path.of(args[1]);
		var basePkg = new Package(args[2]);
		var fs = new ProjectGeneratorFilesystem(outputDir);
		var engine = new JinjaTemplateEngine(new ResourceTemplateProvider());
		var generator = new CorfuMessageGenerator(fs, engine, basePkg);
		generator.generate(config);
	}
}
