package org.commons;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelManager {

    private String fileName;
    private HSSFWorkbook workbook;
    private int rowCount;
    private HSSFSheet currentSheet;
    
    public ExcelManager(String fileName) {
        
        this.fileName = fileName;
        workbook = new HSSFWorkbook();
        rowCount = 0;
    }
    
    public void addSheet(String sheetName) {
        
        rowCount = 0;
        currentSheet = workbook.createSheet(sheetName);  
    }
    
    public void addRow(List<String> cellsValues) {
        
        HSSFRow rowhead = currentSheet.createRow((short)rowCount++);
        int cellIndex = 0;
        for (String cellValue : cellsValues) {
            rowhead.createCell(cellIndex++).setCellValue(cellValue);
        }
    }
    
    public void saveExcel() {
        
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
