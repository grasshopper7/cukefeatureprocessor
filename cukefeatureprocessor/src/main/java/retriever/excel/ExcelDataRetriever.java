package retriever.excel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;

import retriever.DataRetriever;

public abstract class ExcelDataRetriever extends DataRetriever {
	
	protected File excelFile;
	protected String sheetName;
	protected CellReference rangeTopLeft;
	protected CellReference rangeRightBottom;

	public ExcelDataRetriever(String[] dataFile) {
		
		super(dataFile);
		if (dataFileDetails.length < 5)
			throw new RuntimeException("Excel File Data is corrupt!");
		this.excelFile = Paths.get(dataFileDetails[2]).toFile();
		this.sheetName = dataFileDetails[3];
		String[] range = dataFileDetails[4].split(":");
		if (range.length != 2)
			throw new RuntimeException("Excel File Data is corrupt!");
		rangeTopLeft = new CellReference(range[0]);
		rangeRightBottom = new CellReference(range[1]);
	}

	@Override
	public List<String> getData() {
		
		try (Workbook workbook = WorkbookFactory.create(excelFile)) {
			Sheet sheet = workbook.getSheet(sheetName);	
			return parseCellData(sheet);
		} catch (EncryptedDocumentException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<String> parseCellData(Sheet sheet) {
		
		AreaReference ar = new AreaReference(rangeTopLeft, rangeRightBottom, SpreadsheetVersion.EXCEL97);
		CellReference[] crs = ar.getAllReferencedCells();
		return parseRows(sheet, crs);
	}
	
	public abstract List<String> parseRows(Sheet sheet, CellReference[] crs);

	public static ExcelDataRetriever getExcelDataRetrieverType(String[] dataFile) {
		
		if(dataFile.length > 5)
			return (ExcelDataRetriever) DataRetriever.getDataRetrieverObject(dataFile[5], dataFile);
		return new ExcelSimpleDataRetriever(dataFile);
	}
}
