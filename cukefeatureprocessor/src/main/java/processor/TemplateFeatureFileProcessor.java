package processor;

import static retriever.DataRetriever.DELIMITER;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import retriever.DataRetriever;

public class TemplateFeatureFileProcessor {
	
	private Path templateFile;
	private Arguments arguments;
	
	public final static String DATA_FILE_SYMBOL_REGEX = "^\\s*\\"+DELIMITER+"\\s*Data:\\s*\\"+DELIMITER+".*";

	public TemplateFeatureFileProcessor(Path templateFile, Arguments arguments) {
		this.templateFile = templateFile;
		this.arguments = arguments;
	}

	public void processFile() {
	
		try {
			List<String> newLines = new ArrayList<>();
			List<String> lines = Files.readAllLines(templateFile);
			
			for(String line : lines) {
				boolean result = line.matches(DATA_FILE_SYMBOL_REGEX) ? 
						newLines.addAll(DataRetriever.processData(line)) : newLines.add(line);
			}
			
			if(newLines.size() >= lines.size()) {
				String additionalDirStructure = (arguments.getTemplateSourceRootFolder().getNameCount() == templateFile.getNameCount()-1) ?
						"" : templateFile.subpath(arguments.getTemplateSourceRootFolder().getNameCount(), templateFile.getNameCount()-1).toString();
				
				Path destDir = Paths.get(arguments.getFeatureDestinationRootFolder().toString() + 
						FileSystems.getDefault().getSeparator() + additionalDirStructure);
				
				if(!Files.exists(destDir))
					Files.createDirectories(destDir);
				
				Files.write(Paths.get(destDir.toString()+ FileSystems.getDefault().getSeparator() + 
						templateFile.subpath(templateFile.getNameCount()-1, templateFile.getNameCount()).toString()), newLines);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}	

}
