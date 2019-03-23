package retriever.excel;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelSimpleDataRetriever extends ExcelDataRetriever {

	public ExcelSimpleDataRetriever(String[] dataFile) {
		super(dataFile);
	}

    @Override
    public List<String> parseRows(Sheet sheet, CellReference[] crs) {

        DataFormatter dataFormatter = new DataFormatter();
        return Arrays
                .stream(crs)
                .collect(Collectors.toMap(CellReference::getRow,
                        cr -> dataFormatter.formatCellValue(sheet.getRow(cr.getRow()).getCell(cr.getCol())).trim(),
                        (v1, v2) -> String.join(BOTH_SPACED_DELIMITER, v1, v2)))
                .values().stream().map(this::wrapLineSpace).collect(Collectors.toList());
    }
}
