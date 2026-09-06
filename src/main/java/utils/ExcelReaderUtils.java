package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReaderUtils {

    public static Object[][] getExcelData(String filePath, String sheetName) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);

        int rowCount = sheet.getLastRowNum(); // Excludes header
        int colCount = sheet.getRow(0).getLastCellNum();

        Object[][] data = new Object[rowCount][colCount];
        DataFormatter formatter = new DataFormatter();

        for (int i = 0; i < rowCount; i++) {
            Row row = sheet.getRow(i + 1); // Skip header row
            for (int j = 0; j < colCount; j++) {
                Cell cell = row.getCell(j);
                data[i][j] = formatter.formatCellValue(cell); // Safely formats any cell to String
            }
        }

        workbook.close();
        fis.close();
        return data;
    }
} 
    
