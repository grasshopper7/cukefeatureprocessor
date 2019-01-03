package retriever;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public abstract class JsonDataRetriever extends DataRetriever {

	protected File jsonFile;
	protected String objectKey;
	protected List<String> headers;

	public JsonDataRetriever(String[] dataFile) {
		super(dataFile);
		if (dataFileDetails.length < 4)
			throw new RuntimeException("Json File Data is corrupt!");
		this.jsonFile = Paths.get(dataFileDetails[2]).toFile();
		this.objectKey = dataFileDetails[3];
	}

	@Override
	public List<String> getData() {

		JsonObject fileObj = jsonFileObject();
		headers = dataHeader(fileObj);
		
		List<String> data = new ArrayList<>();
		data.add(parseHeader());		
		data.addAll(parseRows());

		return data;
	}

	protected abstract List<String> parseRows();

	protected JsonObject jsonFileObject() {
		try {
			Gson gson = new Gson();
			return gson.fromJson(new FileReader(jsonFile), JsonObject.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract List<String> dataHeader(JsonObject fileObj);

	protected String parseHeader() {
		return headers.stream().collect(delimitTxt());
	}

	protected Collector<CharSequence, ?, String> delimitTxt() {
		return Collectors.joining(BOTH_SPACED_DELIMITER, RIGHT_SPACED_DELIMITER, LEFT_SPACED_DELIMITER);
	}
	
	public static JsonDataRetriever getJsonDataRetrieverType(String[] dataFile) {
		if (dataFile.length > 4)
			return (JsonDataRetriever) DataRetriever.getDataRetrieverObject(dataFile[4], dataFile);
		return new JsonSimpleDataRetriever(dataFile);
	}
}
