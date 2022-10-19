package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.exception.ParsingException;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;

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

		var struct = app.structs.get("BlockListEntry");

		var rendering = engine.renderAppStandardTelemetryRecord(pkg, app);

		System.out.println(engine.renderNodeRecord(pkg, config.nodes.get(0)));

		System.out.println(rendering);

		//System.out.println(nodeClass.rendering());
	}
}
