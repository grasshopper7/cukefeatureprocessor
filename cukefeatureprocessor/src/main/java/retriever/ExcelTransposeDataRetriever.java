package retriever;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

public class ExcelTransposeDataRetriever extends ExcelDataRetriever {

	public ExcelTransposeDataRetriever(String[] dataFile) {	
		super(dataFile);
	}
	
	@Override
	public List<String> parseRows(Sheet sheet, CellReference[] crs) {
		
		List<String> data = new ArrayList<>();
		Map<Short,String> coldata = new HashMap<>();
		DataFormatter dataFormatter = new DataFormatter();
		
		Arrays.stream(crs).forEach( cr -> coldata.merge(cr.getCol(),
				dataFormatter.formatCellValue(sheet.getRow(cr.getRow()).getCell(cr.getCol())),
				(v1,v2) -> String.join(BOTH_SPACED_DELIMITER, v1, v2)));
		
		coldata.values().stream().forEach(v -> data.add(wrapLineSpace(v)));	
		return data;
	}	
}
