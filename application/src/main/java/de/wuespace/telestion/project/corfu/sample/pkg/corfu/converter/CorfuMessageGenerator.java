package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.fs.GeneratorFilesystem;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.TemplateEngine;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.type.AppRendering;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.type.NodeRendering;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.type.PayloadRendering;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.CorfuProjectConfiguration;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Message;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;

import java.io.IOException;
import java.util.*;

public class CorfuMessageGenerator {

	public static final Package APPS_PKG = new Package("app");
	public static final Package NODES_PKG = new Package("node");

	private final GeneratorFilesystem fs;
	private final TemplateEngine engine;
	private final Package basePkg;

	public final List<PayloadRendering> renderedTelecommandPayloads;
	public final List<PayloadRendering> renderedTelemetryPayloads;
	public final List<AppRendering> renderedAppTelecommands;
	public final List<AppRendering> renderedAppTelemetries;
	public final List<NodeRendering> renderedNodes;
	public final Map<String, AppRendering> renderedAppStandardTelemetries;
	public final List<NodeRendering> renderedNodeStandardTelemetries;
	public AppRendering payloadInterface;

	public CorfuMessageGenerator(GeneratorFilesystem fs, TemplateEngine engine, Package basePkg) {
		this.fs = fs;
		this.engine = engine;
		this.basePkg = basePkg;
		this.renderedTelecommandPayloads = new ArrayList<>();
		this.renderedTelemetryPayloads = new ArrayList<>();
		this.renderedAppTelecommands = new ArrayList<>();
		this.renderedAppTelemetries = new ArrayList<>();
		this.renderedNodes = new ArrayList<>();
		this.renderedAppStandardTelemetries = new HashMap<>();
		this.renderedNodeStandardTelemetries = new ArrayList<>();
		this.payloadInterface = null;
	}

	public void reset() throws IOException {
		this.renderedTelecommandPayloads.clear();
		this.renderedTelemetryPayloads.clear();
		this.renderedAppTelecommands.clear();
		this.renderedAppTelemetries.clear();
		this.renderedNodes.clear();
		this.renderedAppStandardTelemetries.clear();
		this.renderedNodeStandardTelemetries.clear();
		this.payloadInterface = null;

		fs.delete(basePkg.path());
	}

	public GeneratorFilesystem getGeneratorFilesystem() {
		return fs;
	}

	public TemplateEngine getEngine() {
		return engine;
	}

	public Package getBasePkg() {
		return basePkg;
	}

	public boolean generatesStandardTelemetry() {
		return Objects.nonNull(payloadInterface);
	}

	/**
	 * Steps in which order to generate Corfu definitions:
	 * <ol>
	 *     <li>
	 *         for each app:
	 *         <ol>
	 *             <li>generate structs (records)</li>
	 *             <li>generate standard telemetry (record)</li>
	 *             <li>generate all telecommand payloads (records)</li>
	 *             <li>generate all telemetry payloads (records)</li>
	 *             <li>generate telecommand payload interface (interface)</li>
	 *             <li>generate telemetry payload interface (interface)</li>
	 *             <li>generate app telecommand (record)</li>
	 *             <li>generate app telemetry (record)</li>
	 *         </ol>
	 *     </li>
	 *     <li>
	 *         for each node:
	 *         <ol>
	 *             <li>generate node + hardware (record + enum)</li>
	 *             <li>generate node standard telemetry (record)</li>
	 *         </ol>
	 *     </li>
	 *     <li>regenerate standard telemetry app telemetry payload interface (interface)</li>
	 *     <li>generate registrar</li>
	 * </ol>
	 *
	 * @param config
	 * @throws IOException gets thrown when errors happen during filesystem operations
	 */
	public void generate(CorfuProjectConfiguration config) throws IOException {
		reset();

		var appsPkg = basePkg.resolve(APPS_PKG);
		var nodesPkg = basePkg.resolve(NODES_PKG);

		// 1: Apps
		System.out.printf("Generate app definitions%n");
		for (var app : config.apps) {
			var appPkg = appsPkg.resolve(new Package(app.getName().packageName()));
			System.out.printf("+ App: %s (%s)%n", app.getName().raw(), appPkg.binaryName());

			// 1.1: Generate all app structs
			System.out.printf("  + app structs:%n");
			for (var struct : app.structs.values()) {
				System.out.printf("    + struct: %s%n", struct.getName().raw());
				var structRendering = engine.renderAppStructRecord(appPkg, struct);
				fs.writeFile(structRendering);
			}

			// 1.2: Generate standard telemetry
			System.out.printf("  + app standard telemetry%n");
			var standardTelemetryRendering = engine.renderAppStandardTelemetryRecord(appPkg, app);
			renderedAppStandardTelemetries.put(app.getName().raw(), standardTelemetryRendering);
			fs.writeFile(standardTelemetryRendering);

			// 1.3: Generate all telecommand payloads
			System.out.printf("  + telecommand payloads:%n");
			for (var telecommand : app.telecommands.values()) {
				System.out.printf("    + telecommand payload: %s%n", telecommand.getName().raw());
				var telecommandRendering = engine.renderTelecommandPayloadRecord(appPkg, telecommand);
				renderedTelecommandPayloads.add(telecommandRendering);
				fs.writeFile(telecommandRendering);
			}

			// 1.4: Generate all telemetry payloads
			System.out.printf("  + telemetry payloads:%n");
			for (var telemetry : app.extendedTelemetry.values()) {
				System.out.printf("    + telemetry payload: %s%n", telemetry.getName().raw());
				var telemetryRendering = engine.renderTelemetryPayloadRecord(appPkg, telemetry);
				renderedTelemetryPayloads.add(telemetryRendering);
				fs.writeFile(telemetryRendering);
			}

			// 1.5: Generate the telecommand payload interface
			System.out.printf("  + telecommand payload interface%n");
			var tcInterfaceRendering = engine.renderAppTelecommandPayloadInterface(appPkg, app);
			fs.writeFile(tcInterfaceRendering);

			// 1.6: Generate the telemetry payload interface
			System.out.printf("  + telemetry payload interface%n");
			var tmInterfaceRendering = engine.renderAppTelemetryPayloadInterface(appPkg, app);
			fs.writeFile(tmInterfaceRendering);

			if (app.generatesStandardTelemetry()) {
				System.out.printf("  + app is standard telemetry sender%n");
				payloadInterface = tmInterfaceRendering;
			}

			// 1.7: Generate the app telecommand record
			System.out.printf("  + app telecommand record%n");
			var appTelecommandRendering = engine.renderAppTelecommandRecord(appPkg, app);
			renderedAppTelecommands.add(appTelecommandRendering);
			fs.writeFile(appTelecommandRendering);

			System.out.printf("  + app telemetry record%n");
			var appTelemetryRendering = engine.renderAppTelemetryRecord(appPkg, app);
			renderedAppTelemetries.add(appTelemetryRendering);
			fs.writeFile(appTelemetryRendering);
		}

		// 2: Nodes
		System.out.printf("Generate node definitions%n");
		for (var node : config.nodes) {
			var nodePkg = nodesPkg.resolve(new Package(node.getName().packageName()));
			System.out.printf("+ Node: %s (%s)%n", node.getName().raw(), nodePkg.binaryName());

			// 2.1: Generate node record
			System.out.printf("  + node record%n");
			var nodeRendering = engine.renderNodeRecord(nodePkg, node);
			renderedNodes.add(nodeRendering);
			fs.writeFile(nodeRendering);

			if (generatesStandardTelemetry()) {
				// 2.2: Generate node standard telemetry
				System.out.printf("  + node standard telemetry%n");
				// create list of app standard telemetries that the node standard telemetry use
				var appStandardTelemetryRenderings = node.apps.keySet().stream()
						.map(renderedAppStandardTelemetries::get)
						.toList();
				var nodeStandardTelemetryRendering = engine.renderNodeStandardTelemetryRecord(
						nodePkg,
						node,
						appStandardTelemetryRenderings,
						payloadInterface
				);
				renderedNodeStandardTelemetries.add(nodeStandardTelemetryRendering);

				var telemetryPayloadConfig = new Message();
				telemetryPayloadConfig.setName("placeholder_message_for_standard_telemetry");
				telemetryPayloadConfig.setAssociatedApp(payloadInterface.config());
				telemetryPayloadConfig.finalizeConfig();
				renderedTelemetryPayloads.add(new PayloadRendering(
						nodeStandardTelemetryRendering.rendering(),
						nodeStandardTelemetryRendering.className(),
						nodeStandardTelemetryRendering.pkg(),
						telemetryPayloadConfig
				));
				fs.writeFile(nodeStandardTelemetryRendering);
			}
		}

		// 3: Overwrite standard telemetry application
		if (generatesStandardTelemetry()) {
			var app = payloadInterface.config();
			var appPkg = appsPkg.resolve(app.getName().packageName());
			System.out.printf("insert standard telemetry in payload interface for app %s%n", app.getName().raw());

			var tmInterfaceRendering = engine.renderAppTelemetryPayloadInterface(appPkg, app, renderedNodeStandardTelemetries);
			fs.writeFile(tmInterfaceRendering);
		}

		// 4: Generate registrar
		System.out.printf("generate registrar%n");
		var registrarRendering = engine.renderRegistrarClass(
				basePkg,
				renderedNodes,
				renderedAppTelecommands,
				renderedAppTelemetries,
				renderedTelecommandPayloads,
				renderedTelemetryPayloads
		);
		fs.writeFile(registrarRendering);

		System.out.printf("Done%n");
	}
}
