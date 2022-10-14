package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.generator;

import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.generator.type.*;

import java.io.IOException;
import java.util.List;

public class CorfuMessageGenerator {
	private final GeneratorFilesystem fs;

	public CorfuMessageGenerator(GeneratorFilesystem fs) {
		this.fs = fs;
	}

	public NodeDefinition addNode(short nodeId, String nodeName, List<HardwareDefinition> hardwareTargets) {
		// TODO: Implement
		throw new UnsupportedOperationException("Not implemented");
	}

	public AppTelecommandDefinition addAppTelecommand(short appId, String appName) {
		// TODO: Implement
		throw new UnsupportedOperationException("Not implemented");
	}

	public EntryValueType addAppStruct(short appId, String structName, List<PayloadEntry> entries) {
		// TODO: Implement
		throw new UnsupportedOperationException("Not implemented");
	}

	public void addTelecommandPayload(short appId, short payloadId, String payloadName, List<PayloadEntry> entries) {
		// TODO: Implement
		throw new UnsupportedOperationException("Not implemented");
	}

	public void addAppTelemetry(short appId, String appName) {
		// TODO: Implement
		throw new UnsupportedOperationException("Not implemented");
	}

	public void addTelemetryPayload(short appId, short payloadId, String payloadName, List<PayloadEntry> entries) {
		// TODO: Implement
		throw new UnsupportedOperationException("Not implemented");
	}

	public void addStandardTelemetry(short appId, List<PayloadEntry> entries) {
		// TODO: Implement
		throw new UnsupportedOperationException("Not implemented");
	}

	public void write(String packageScope) throws IOException {
		// irgendwas mit this.fs machen
		// TODO: Implement
		throw new UnsupportedOperationException("Not implemented");
	}
}
