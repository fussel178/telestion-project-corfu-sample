package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.fs.GeneratorFilesystem;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.TemplateEngine;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.type.AppRendering;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.type.NodeRendering;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.type.PayloadRendering;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.CorfuProjectConfiguration;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;

import java.io.IOException;
import java.util.*;

public class CorfuMessageGenerator {

	public static final Package APPS_PKG = new Package("app");
	public static final Package NODES_PKG = new Package("node");

	private final GeneratorFilesystem fs;
	private final TemplateEngine engine;

	public final List<PayloadRendering> renderedTelecommandPayloads;
	public final List<PayloadRendering> renderedTelemetryPayloads;
	public final List<AppRendering> renderedAppTelecommands;
	public final List<AppRendering> renderedAppTelemetries;
	public final List<NodeRendering> renderedNodes;
	public final Map<String, AppRendering> renderedAppStandardTelemetries;
	public AppRendering payloadInterface;

	public CorfuMessageGenerator(GeneratorFilesystem fs, TemplateEngine engine) {
		this.fs = fs;
		this.engine = engine;
		this.renderedTelecommandPayloads = new ArrayList<>();
		this.renderedTelemetryPayloads = new ArrayList<>();
		this.renderedAppTelecommands = new ArrayList<>();
		this.renderedAppTelemetries = new ArrayList<>();
		this.renderedNodes = new ArrayList<>();
		this.renderedAppStandardTelemetries = new HashMap<>();
		this.payloadInterface = null;
	}

	public void reset() {
		this.renderedTelecommandPayloads.clear();
		this.renderedTelemetryPayloads.clear();
		this.renderedAppTelecommands.clear();
		this.renderedAppTelemetries.clear();
		this.renderedNodes.clear();
		this.payloadInterface = null;
	}

	public GeneratorFilesystem getGeneratorFilesystem() {
		return fs;
	}

	public TemplateEngine getEngine() {
		return engine;
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
	 * @param basePkg
	 * @param config
	 * @throws IOException gets thrown when errors happen during filesystem operations
	 */
	public void generate(Package basePkg, CorfuProjectConfiguration config) throws IOException {
		reset();

		var appsPkg = basePkg.resolve(APPS_PKG);
		var nodesPkg = basePkg.resolve(NODES_PKG);

		// 1: Apps
		for (var app : config.apps) {
			var appPkg = appsPkg.resolve(new Package(app.getName().packageName()));

			// 1.1: Generate all app structs
			for (var struct : app.structs.values()) {
				var structRendering = engine.renderAppStructRecord(appPkg, struct);
				fs.writeFile(structRendering);
			}

			// 1.2: Generate standard telemetry
			var standardTelemetryRendering = engine.renderAppStandardTelemetryRecord(appPkg, app);
			renderedAppStandardTelemetries.put(app.getName().raw(), standardTelemetryRendering);
			fs.writeFile(standardTelemetryRendering);

			// 1.3: Generate all telecommand payloads
			for (var telecommand : app.telecommands.values()) {
				var telecommandRendering = engine.renderTelecommandPayloadRecord(appPkg, telecommand);
				renderedTelecommandPayloads.add(telecommandRendering);
				fs.writeFile(telecommandRendering);
			}

			// 1.4: Generate all telemetry payloads
			for (var telemetry : app.extendedTelemetry.values()) {
				var telemetryRendering = engine.renderTelemetryPayloadRecord(appPkg, telemetry);
				renderedTelemetryPayloads.add(telemetryRendering);
				fs.writeFile(telemetryRendering);
			}

			// 1.5: Generate the telecommand payload interface
			var tcInterfaceRendering = engine.renderAppTelecommandPayloadInterface(appPkg, app);
			fs.writeFile(tcInterfaceRendering);

			// 1.6: Generate the telemetry payload interface
			var tmInterfaceRendering = engine.renderAppTelemetryPayloadInterface(appPkg, app);
			fs.writeFile(tmInterfaceRendering);

			if (app.generatesStandardTelemetry()) {
				payloadInterface = tmInterfaceRendering;
			}

			// 1.7: Generate the app telecommand record
			var appTelecommandRendering = engine.renderAppTelecommandRecord(appPkg, app);
			renderedAppTelecommands.add(appTelecommandRendering);
			fs.writeFile(appTelecommandRendering);

			var appTelemetryRendering = engine.renderAppTelemetryRecord(appPkg, app);
			renderedAppTelemetries.add(appTelemetryRendering);
			fs.writeFile(appTelemetryRendering);
		}

		// 2: Nodes
		for (var node : config.nodes) {
			var nodePkg = nodesPkg.resolve(new Package(node.getName().packageName()));

			// 2.1: Generate node record
			var nodeRendering = engine.renderNodeRecord(nodePkg, node);
			renderedNodes.add(nodeRendering);
			fs.writeFile(nodeRendering);

			if (generatesStandardTelemetry()) {
				// 2.2: Generate node standard telemetry
				var appStandardTelemetryRenderings = node.apps.keySet().stream()
						.map(renderedAppStandardTelemetries::get)
						.toList();
				var nodeStandardTelemetryRendering = engine.renderNodeStandardTelemetryRecord(
						nodePkg,
						node,
						appStandardTelemetryRenderings,
						payloadInterface
				);
				fs.writeFile(nodeStandardTelemetryRendering);
			}
		}

		// 3: Overwrite standard telemetry application
		// TODO!

		// 4: Generate registrar
		var registrarRendering = engine.renderRegistrarClass(
				basePkg,
				renderedNodes,
				renderedAppTelecommands,
				renderedAppTelemetries,
				renderedTelecommandPayloads,
				renderedTelemetryPayloads
		);
		fs.writeFile(registrarRendering);
	}
}
