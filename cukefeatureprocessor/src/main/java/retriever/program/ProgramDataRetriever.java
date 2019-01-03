package retriever.program;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import retriever.DataRetriever;

public class ProgramDataRetriever extends DataRetriever {

	public ProgramDataRetriever(String[] dataFile) {

		super(dataFile);
		if (dataFileDetails.length < 4)
			throw new RuntimeException("Program File Data is corrupt!");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getData() {
				
		try {
			String clsName = dataFileDetails[2].substring(0, dataFileDetails[2].lastIndexOf('.'));
			
			Class<?> cls = Class.forName(clsName);
			Method m = cls.getMethod(dataFileDetails[3], String[].class);
			Object o = cls.newInstance();
			
			Object args = dataFileDetails.length > 3 ? 
					Arrays.copyOfRange(dataFileDetails, 4, dataFileDetails.length) :
						new String[0];
			return (List<String>)(m.invoke(o, args));			
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String wrapRowDelimiter(String... elements) {
		return RIGHT_SPACED_DELIMITER +String.join(BOTH_SPACED_DELIMITER, elements)+LEFT_SPACED_DELIMITER;
	}

}
