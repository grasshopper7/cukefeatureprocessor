package retriever.json;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonSimpleDataRetriever extends JsonDataRetriever {

	protected JsonObject jobj;
	
	public JsonSimpleDataRetriever(String[] dataFile) {
		super(dataFile);
	}

	@Override
	public List<String> parseRows() {
		List<String> data = new ArrayList<>();
		List<JsonArray> ljarr = headers.stream().map(h -> jobj.getAsJsonArray(h)).collect(Collectors.toList());

		IntStream.range(0, ljarr.get(0).size()).forEach(i -> {
			data.add(ljarr.stream().map(ja -> ja.get(i).getAsString()).collect(delimitTxt()));
		});
		return data;
	}
	
	@Override
	protected List<String> dataHeader(JsonObject fileObj) {

		jobj = fileObj.getAsJsonObject(objectKey);
		return jobj.keySet().stream().collect(Collectors.toList());
	}
}
