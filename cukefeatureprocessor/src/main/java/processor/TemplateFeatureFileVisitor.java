package processor;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class TemplateFeatureFileVisitor extends SimpleFileVisitor<Path> {

	private Arguments arguments;
	
	public TemplateFeatureFileVisitor(Arguments args) {
		super();
		this.arguments = args;
	}
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		if(file.getFileName().toString().endsWith(".feature")) {
			TemplateFeatureFileProcessor tffp = new TemplateFeatureFileProcessor(file, arguments);
			tffp.processFile();
		}
		
		return FileVisitResult.CONTINUE;
	}
}
