package retriever;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

public class ExcelSimpleDataRetriever extends ExcelDataRetriever {

	public ExcelSimpleDataRetriever(String[] dataFile) {
		super(dataFile);
	}

	public List<String> parseRows(Sheet sheet, CellReference[] crs) {
		
		List<String> data = new ArrayList<>();
		Map<Integer,String> rowdata = new HashMap<>();
		DataFormatter dataFormatter = new DataFormatter();
		
		Arrays.stream(crs).forEach( cr -> rowdata.merge(cr.getRow(),
				dataFormatter.formatCellValue(sheet.getRow(cr.getRow()).getCell(cr.getCol())).trim(),
				(v1,v2) -> String.join(BOTH_SPACED_DELIMITER, v1, v2)));
		
		rowdata.values().stream().forEach(v -> data.add(wrapLineSpace(v)));	
		return data;
	}
}
