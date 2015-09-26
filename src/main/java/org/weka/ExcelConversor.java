package org.weka;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Converts an Excel file to a CSV file.
 * 
 * @author martinmineo
 *
 */

public class ExcelConversor {

    private List<List<Cell>> cells;

    // Files extensions
    private static final String XLSX = ".xlsx";
    private static final String XLS = ".xls";
    private static final String CSV = ".csv";

    /**
     * Constructor
     */
    public ExcelConversor() {

        cells = new ArrayList<List<Cell>>();
    }

    /**
     * Returns a list of lists with data in columns G (mensaje) and H (conducta)
     * from excel file (only XLS and XLSX files) contained in parameter fileName
     * (except header)
     * 
     * @param fileName
     *            - path and file name to add to current object
     * @return List of lists with data contained in parameter fileName (except
     *         header)
     */
    private List<List<Cell>> loadExcel(String fileName) {

        if (fileName.contains(XLSX) || fileName.contains(XLS)) {
            try {
                FileInputStream fileInputStream = new FileInputStream(fileName);
                Workbook workbook;
                Sheet sheet;

                if (fileName.contains(XLSX))
                    workbook = new XSSFWorkbook(fileInputStream);
                else {
                    POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
                    workbook = new HSSFWorkbook(fsFileSystem);
                }

                for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                    sheet = workbook.getSheetAt(sheetIndex);

                    Iterator<Row> rowIterator = sheet.rowIterator();
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();

                        if (row.getRowNum() > 1) {
                            Cell cellMensaje = row.getCell(6);

                            if (cellMensaje != null && cellMensaje.toString() != "") {
                                List<Cell> rowCells = new ArrayList<Cell>();
                                Cell cellConducta = row.getCell(10);
                                rowCells.add(cellMensaje);
                                rowCells.add(cellConducta);
                                cells.add(rowCells);
                            }
                        }
                    }
                }
                workbook.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return cells;
        } else
            return null;
    }

    /**
     * Add escape character to double quotes (") or comma (,)
     * 
     * @param str
     *            - String to add escape character if contains quotes or comma
     * @return String with escape characters
     */
    private String addEscapeChar(String str) {

        String tempStr = str;
        if (tempStr.contains("\""))
            tempStr = tempStr.replace("\"", "\\\"");
        else if (tempStr.contains(","))
            tempStr = tempStr.replace(",", "\\,");

        return tempStr;
    }

    /**
     * Saves to a file all data contained in member variable "cells"
     * 
     * @param csvFileName
     *            - file name to be used
     */
    private void saveToCsv(String csvFileName) {

        try {
            File f = new File(csvFileName);
            if (!f.exists()) {
                f.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(f);
            OutputStream os = (OutputStream) fileOutputStream;
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);

            String cellValue;

            // Write CSV header
            bw.write("Mensaje,Conducta");
            bw.newLine();

            // Save CSV data
            for (int i = 0; i < cells.size(); i++) {
                List<Cell> rowCells = (List<Cell>) cells.get(i);

                // Mensaje
                Cell cell = (Cell) rowCells.get(0);
                // Add escape character to double quotes: ", then add quotes to
                // string cellValue
                cellValue = "\"" + addEscapeChar(cell.toString()) + "\"";
                bw.write(cellValue + ",");

                // Conducta
                cell = (Cell) rowCells.get(1);
                // Get value from cell
                cellValue = BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                bw.write(cellValue);

                bw.newLine();
            }

            bw.flush();
            bw.close();
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Loads data from an Excel file and saves it to a CSV file
     * 
     * @param in
     *            - File path
     */
    public String excelToCSV(String in) {

        cells.clear();
        if (loadExcel(in) != null) {
            String out = in.substring(0, in.lastIndexOf(".xls"));
            saveToCsv(out + CSV);
            return out + CSV;
        }
        return null;
    }
}
