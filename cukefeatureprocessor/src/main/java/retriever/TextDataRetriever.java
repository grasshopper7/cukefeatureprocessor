package retriever;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TextDataRetriever extends DataRetriever {

	protected Path textFile;
	protected String delimiter;

	public TextDataRetriever(String[] dataFile) {

		super(dataFile);
		if (dataFileDetails.length < 4)
			throw new RuntimeException("Text File Data is corrupt!");
		this.textFile = Paths.get(dataFileDetails[2]);
		this.delimiter = (dataFileDetails[3].equalsIgnoreCase(" ")) ? DELIMITER : dataFileDetails[3];
	}

	@Override
	public List<String> getData() {
		try {
			List<String> lines = Files.readAllLines(textFile);
			return parseLines(lines);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<String> parseLines(List<String> lines) {
		return lines.stream().map(l -> parseLine(l)).collect(Collectors.toList());
	}
		
	public String parseLine(String line) {
		line = line.trim().replaceAll("\\s*\\"+delimiter+"\\s*", BOTH_SPACED_DELIMITER);
		return wrapLineSpace(line);
	}

	public static TextDataRetriever getTextDataRetrieverType(String[] dataFile) {

		if (dataFile.length > 5)
			return (TextDataRetriever) DataRetriever.getDataRetrieverObject(dataFile[4], dataFile);
		return new TextSimpleDataRetriever(dataFile);
	}
}
