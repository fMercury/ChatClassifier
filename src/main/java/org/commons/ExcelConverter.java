package org.commons;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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
 * Convierte un archivo Excel en uno ARFF
 * 
 * @author martinmineo
 *
 */

public class ExcelConverter {

    private List<List<Cell>> cells;

    /**
     * Constructor
     */
    public ExcelConverter() {
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

        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            Workbook workbook;
            Sheet sheet;

            if (fileName.contains(Constants.XLSX_FILE))
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
                        Cell cellName = row.getCell(4);
                        Cell cellMessage = row.getCell(6);

                        if (cellMessage != null && cellMessage.toString() != "") {
                            List<Cell> rowCells = new ArrayList<Cell>(); 
                            rowCells.add(cellName);
                            rowCells.add(cellMessage);
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
    }
    
    /**
     * Agrega al String pasado por parámetro los caracteres de escape necesarios
     * @param str Texto para agregarle los caracteres de escape
     * @return String con los caracteres de escape agregados
     */
    private String addEscapeChar(String str) {
        
        if (str != null)
        {
            str = str.replace("'", "\\'");
            str = str.replace('\n', ' ');
            str = str.replace('\u0001', ' ');
            str = str.replace('\u0002', ' ');
        }
        else
            return "";
            
        return str;
    }

    /**
     * Crea el encabezado de los archivos ARFF 
     * @return
     */
    private String getARFFHeader() {
        String header;
        
        header =    "@relation chat" + '\n' + 
                    '\n' + 
                    "@attribute Conducta {1,2,3,4,5,6,7,8,9,10,11,12}" + '\n' + 
                    "@attribute nombre string" + '\n' + 
                    "@attribute message string" + '\n' + 
                    '\n' + 
                    "@data" + '\n';
        return header;
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

            // Escribir el encabezado del archivo ARFF
            bw.write(getARFFHeader());

            String lastName = "";
            // Guardar los datos en un archivo ARFF
            for (int i = 0; i < cells.size(); i++) {
                List<Cell> rowCells = (List<Cell>) cells.get(i);

                Cell cellName = (Cell) rowCells.get(0);
                Cell cellMessage = (Cell) rowCells.get(1);
                
                String name, message;
                name = "";
                
                if (cellName != null) {
                    name = cellName.toString();
                }

                if (name == "")
                    name = lastName;
                    
                message = "";
                if (cellMessage != null) {
                    message = cellMessage.toString();
                }

                bw.write("?,'" + addEscapeChar(name) + "','" + addEscapeChar(message) + "'");
                bw.newLine();

                lastName = name;
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
            saveToArff(out + Constants.ARFF_FILE);
            return out + Constants.ARFF_FILE;
        }
        return null;
    }
}