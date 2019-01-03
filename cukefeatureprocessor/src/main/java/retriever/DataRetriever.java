package retriever;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import retriever.excel.ExcelDataRetriever;
import retriever.json.JsonDataRetriever;
import retriever.program.ProgramDataRetriever;
import retriever.text.TextDataRetriever;

public abstract class DataRetriever {
	
	public static final String DELIMITER = "|";
	
	public static final String BOTH_SPACED_DELIMITER = " " + DELIMITER + " ";
	
	public static final String RIGHT_SPACED_DELIMITER = DELIMITER + " ";
	
	public static final String LEFT_SPACED_DELIMITER = " " + DELIMITER;
	
	
	protected String[] dataFileDetails;
	
	public DataRetriever(String[] dataFile) {
		
		if (dataFile.length < 3)
			throw new RuntimeException("File Data is corrupt!");
		this.dataFileDetails = dataFile;
	}
	
	protected String wrapLineSpace(String line) {
		return RIGHT_SPACED_DELIMITER + line + LEFT_SPACED_DELIMITER;
	}

	public static List<String> processData(String dataFile) {

		dataFile = dataFile.replaceAll("\\s*\\"+DELIMITER+"\\s*", DELIMITER);
		dataFile = dataFile.replaceAll("\\"+DELIMITER+"\\"+DELIMITER, "\\"+DELIMITER+" \\"+DELIMITER);

		String[] fileData = dataFile.split("\\|");

		if (fileData.length < 3)
			throw new RuntimeException("File Data is corrupt!");

		return getDataRetrieverType(fileData).getData();
	}

	private static DataRetriever getDataRetrieverType(String[] dataFile) {

		DataRetriever dr = null;
		String dataFileName = dataFile[2];
		String fileExt = dataFileName.substring(dataFileName.lastIndexOf('.') + 1);

		if (fileExt.equalsIgnoreCase("xls") || fileExt.equalsIgnoreCase("xlsx"))
			dr = ExcelDataRetriever.getExcelDataRetrieverType(dataFile);
		else if (fileExt.equalsIgnoreCase("json"))
			dr = JsonDataRetriever.getJsonDataRetrieverType(dataFile);
		else if (fileExt.equalsIgnoreCase("txt"))
			dr = TextDataRetriever.getTextDataRetrieverType(dataFile);
		else if (fileExt.equalsIgnoreCase("java"))
			dr = new ProgramDataRetriever(dataFile);
		else
			throw new RuntimeException("Data store file format is not correct");

		return dr;
	}

	public abstract List<String> getData();

	@SuppressWarnings("unchecked")
	public static DataRetriever getDataRetrieverObject(String clsName, String[] dataFile) {

		try {
			Class<DataRetriever> cls = (Class<DataRetriever>) Class.forName(clsName);			
			return cls.getDeclaredConstructor(String[].class).newInstance(new Object[] { dataFile });
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
