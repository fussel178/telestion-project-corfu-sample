package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.template.type.*;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.*;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.type.Package;

import java.util.List;

public interface TemplateEngine {
	StructRendering renderAppStructRecord(Package pkg, MessageFields config);

	AppRendering renderAppStandardTelemetryRecord(Package pkg, AppConfiguration config);

	PayloadRendering renderTelecommandPayloadRecord(Package pkg, Message config);

	PayloadRendering renderTelemetryPayloadRecord(Package pkg, Message config);

	AppRendering renderAppTelecommandPayloadInterface(Package pkg, AppConfiguration config);

	AppRendering renderAppTelemetryPayloadInterface(Package pkg, AppConfiguration config);

	AppRendering renderAppTelemetryPayloadInterface(Package pkg, AppConfiguration config, List<NodeRendering> nodePayloads);

	AppRendering renderAppTelecommandRecord(Package pkg, AppConfiguration config);

	AppRendering renderAppTelemetryRecord(Package pkg, AppConfiguration config);

	NodeRendering renderNodeRecord(Package pkg, NodeConfiguration config);

	NodeRendering renderNodeStandardTelemetryRecord(
			Package pkg,
			NodeConfiguration config,
			List<AppRendering> appStandardTelemetryRenderings,
			AppRendering payloadInterface
	);

	PlainRendering renderRegistrarClass(
			Package pkg,
			List<NodeRendering> renderedNodes,
			List<AppRendering> renderedAppTelecommands,
			List<AppRendering> renderedAppTelemetries,
			List<PayloadRendering> renderedTelecommandPayloads,
			List<PayloadRendering> renderedTelemetryPayloads
	);
}
