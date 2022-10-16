package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.exception.ParsingException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;

import java.nio.file.Path;

public class TestParser {
	public static void main(String[] args) throws ParsingException {
		// var projectRootDir = Path.of("/home/ludwig/Coding/Private/telestion-corfu-ba/corfu-example-obsw");
		var projectRootDir = Path.of("/home/ludwig/Coding/Private/telestion-corfu-ba/corfu-config-converter/sample/innocube");

		var parser = new CorfuConfigParser(YAML.getMapper());

		var config = parser.getProjectConfig(projectRootDir);

		var engine = new TemplateEngine();

		var pkg = new Package("de.wuespace.telestion.generated.innocube");

		for (int i = 0; i < config.apps.size(); i++) {
			System.out.printf("%d: %s%n", i, config.apps.get(i).getName().raw());
		}

		var app = config.apps.get(15);

		var secondTelecommand = app.extendedTelemetry.get("ReceiveStatus");

		var rendering = engine.renderTelemetryPayloadRecord(pkg, secondTelecommand);

		System.out.println(rendering);

		//System.out.println(nodeClass.rendering());
	}
}
