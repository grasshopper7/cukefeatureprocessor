package retriever;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class JsonExplodedDataRetriever extends JsonDataRetriever {
	
	protected JsonArray jarr;
	
	public JsonExplodedDataRetriever(String[] dataFile) {	
		super(dataFile);
	}

	@Override
	public List<String> parseRows() {
		List<String> data = new ArrayList<>();
		
		for(JsonElement je : jarr) {
			JsonObject jo = je.getAsJsonObject();
			data.add(headers.stream().map(h -> jo.getAsJsonPrimitive(h).getAsString().trim()).collect(delimitTxt()));
		}
		return data;
	}

	@Override
	protected List<String> dataHeader(JsonObject fileObj) {
		
		jarr = fileObj.getAsJsonArray(objectKey);		
		return jarr.get(0).getAsJsonObject().keySet().stream().collect(Collectors.toList());
	}
}
