package processor;

import java.io.IOException;
import java.nio.file.Files;

public class FileProcessor {

	public static void process(String[] args) {
		
		Arguments arguments = new Arguments(args);
		
		try {
			Files.walkFileTree(arguments.getTemplateSourceRootFolder(), new TemplateFeatureFileVisitor(arguments));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
