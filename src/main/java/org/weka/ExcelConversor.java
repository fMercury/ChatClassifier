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
 * Converts an Excel file to a ARFF file.
 * 
 * @author martinmineo
 *
 */

public class ExcelConversor {

    private List<List<Cell>> cells;

    // Files extensions
    private static final String XLSX = ".xlsx";
    private static final String XLS = ".xls";
    private static final String ARFF = ".arff";

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
     * Add escape character to quotes (')
     * 
     * @param str
     *            - String to add escape character if contains quotes or comma
     * @return String with escape characters
     */
    private String addEscapeChar(String str) {

        if (str.contains("'"))
            str = str.replace("'", "\\'");

        return str;
    }

    /**
     * Saves to a file all data (except rows not-labeled or labeled with value
     * zero) contained in member variable "cells"
     * 
     * @param arffFileName
     *            - file name to be used
     */
    private void saveToArff(String arffFileName) {

        try {
            File f = new File(arffFileName);
            if (!f.exists()) {
                f.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(f);
            OutputStream os = (OutputStream) fileOutputStream;
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);

            String cellValue;

            // Write ARFF header
            bw.write("@relation chat");
            bw.newLine();
            bw.newLine();
            bw.write("@attribute Mensaje string");
            bw.newLine();
            bw.write("@attribute Conducta {1,2,3,4,5,6,7,8,9,10,11,12}");
            bw.newLine();
            bw.newLine();
            bw.write("@data");
            bw.newLine();

            // Save ARFF data
            for (int i = 0; i < cells.size(); i++) {
                List<Cell> rowCells = (List<Cell>) cells.get(i);

                Cell cellMessage = (Cell) rowCells.get(0);
                Cell cellConduct = (Cell) rowCells.get(1);

                if (cellConduct.getNumericCellValue() > 0) {
                    // Mensaje
                    cellValue = "'" + addEscapeChar(cellMessage.toString()) + "'";
                    bw.write(cellValue + ",");

                    // Conducta
                    // Get value from cell
                    cellValue = BigDecimal.valueOf((int) (cellConduct.getNumericCellValue())).toPlainString();
                    bw.write(cellValue);

                    bw.newLine();
                }
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
     * Loads data from an Excel file and saves it to a ARFF file
     * 
     * @param in
     *            - File path
     */
    public String excelToARFF(String in) {

        cells.clear();
        if (loadExcel(in) != null) {
            String out = in.substring(0, in.lastIndexOf(".xls"));
            saveToArff(out + ARFF);
            return out + ARFF;
        }
        return null;
    }
}
