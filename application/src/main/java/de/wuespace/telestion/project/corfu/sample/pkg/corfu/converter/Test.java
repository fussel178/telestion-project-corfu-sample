package de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.wuespace.telestion.api.message.JsonMessage;
import de.wuespace.telestion.project.corfu.sample.pkg.corfu.converter.generator.ProjectGeneratorFilesystem;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaRecordSource;

import java.io.IOException;
import java.nio.file.Path;

public class Test {
	public static void main(String[] args) throws IOException {
		var javaClass = Roaster.parse(JavaRecordSource.class, "public record Test() implements JsonMessage {}");
		//var javaClass = Roaster.create(JavaRecordSource.class);

		javaClass.addImport(JsonMessage.class);
		javaClass.addMethod();

		javaClass.setPackage("de.wuespace.telestion.corfu.generated").setName("TestMessage implements JsonMessage");
		//javaClass.addInterface(JsonMessage.class);

		//((InterfaceCapableSource<JavaRecordSource>) javaClass).addInterface(JsonMessage.class);

		javaClass.addAnnotation(JsonIgnoreProperties.class).setLiteralValue("ignoreUnknown", "true");

		System.out.println(javaClass);

		var interfaceClass = Roaster.create(JavaClassSource.class);
		interfaceClass.setPackage("de.wuespace.telestion").setName("InterfaceClass");
		interfaceClass.addInterface(JsonMessage.class);

		System.out.println(interfaceClass);

		Path basePath = Path.of("/tmp", "test");

		var fs = new ProjectGeneratorFilesystem(basePath);

		Path duplicateFilePath = Path.of("folder1", "folder2", "file1.txt");
		fs.writeFile(duplicateFilePath, "1 Hello World!");
		fs.writeFile(Path.of("folder1", "folder2", "file2.txt"), "2 Hello World!");
		fs.writeFile(Path.of("folder1", "folder3", "file3.txt"), "3 Hello World!");
		fs.writeFile(Path.of("folder4", "folder5", "file4.txt"), "4 Hello World!");
		fs.writeFile(duplicateFilePath, "OVERRIDDEN!");
		System.out.println("Successfully written files");
	}
}
