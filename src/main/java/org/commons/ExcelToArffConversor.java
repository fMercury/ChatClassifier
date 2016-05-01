package org.commons;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.freeling.FreelingAnalyzer3;

import edu.upc.freeling3.ListSentence;

/**
 * Converts an Excel file to a ARFF file.
 * 
 * @author martinmineo
 *
 */

public class ExcelToArffConversor {

    private List<List<Cell>> cells;

    // Files extensions
    private static final String XLSX = ".xlsx";
    private static final String XLS = ".xls";
    private static final String ARFF = ".arff";

    // IPA
    private static final String AREA_SOCIO_EMOCIONAL = "socio-emocional";
    private static final String AREA_TAREA = "tarea";

    private static final String REACCION_POSITIVA = "positiva";
    private static final String REACCION_NEGATIVA = "negativa";
    private static final String REACCION_RESPUESTA = "respuesta";
    private static final String REACCION_PREGUNTA = "pregunta";

    private boolean useFreeling;

    /**
     * Constructor
     */
    public ExcelToArffConversor(boolean useFreeling) {

        cells = new ArrayList<List<Cell>>();
        this.useFreeling = useFreeling;
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
     * Escribe el encabezado del archivo ARFF
     * 
     * @param bw
     * @throws IOException
     */
    private void arffHeader(BufferedWriter bw) throws IOException {

        bw.write("@relation chat");
        bw.newLine();
        bw.newLine();
        bw.write("@attribute Conducta {1,2,3,4,5,6,7,8,9,10,11,12}");
        bw.newLine();
        bw.write("@attribute message string");
        bw.newLine();

        if (useFreeling) {
            bw.write("@attribute adjectives numeric");
            bw.newLine();
            bw.write("@attribute adverbs numeric");
            bw.newLine();
            bw.write("@attribute determinants numeric");
            bw.newLine();
            bw.write("@attribute names numeric");
            bw.newLine();
            bw.write("@attribute verbs numeric");
            bw.newLine();
            bw.write("@attribute pronouns numeric");
            bw.newLine();
            bw.write("@attribute conjuctions numeric");
            bw.newLine();
            bw.write("@attribute interjections numeric");
            bw.newLine();
            bw.write("@attribute prepositions numeric");
            bw.newLine();
            bw.write("@attribute punctuation numeric");
            bw.newLine();
            bw.write("@attribute numerals numeric");
            bw.newLine();
            bw.write("@attribute dates_times numeric");
            bw.newLine();
            bw.write("@attribute emoticon_pos numeric");
            bw.newLine();
            bw.write("@attribute emoticon_neg numeric");
            bw.newLine();
            bw.write("@attribute emoticon_neu numeric");
            bw.newLine();
        }
        bw.newLine();
        bw.write("@data");
        bw.newLine();
    }

    private void arffHeader2(BufferedWriter bw) throws IOException {

        bw.write("@relation chat");
        bw.newLine();
        bw.newLine();
        bw.write("@attribute class_area {" + AREA_SOCIO_EMOCIONAL + "," + AREA_TAREA + "}");
        bw.newLine();
        bw.write("@attribute class_reaccion {" + REACCION_POSITIVA + "," + REACCION_NEGATIVA + "," + REACCION_PREGUNTA + ","
                + REACCION_RESPUESTA + "}");
        bw.newLine();
        bw.write("@attribute class_conducta {1,2,3,4,5,6,7,8,9,10,11,12}");
        bw.newLine();
        bw.write("@attribute message string");
        bw.newLine();
        bw.newLine();
        bw.write("@data");
        bw.newLine();
    }

    /////// Pasar esto a un archivo de propiedades
    public String replaceEmojisWithText(String message) {

        String regex = "([\\u20a0-\\u32ff\\ud83c\\udc00-\\ud83d\\udeff\\udbb9\\udce5-\\udbb9\\udcee])";
        String replacement = "";
        Matcher matchEmo = Pattern.compile(regex).matcher(message);

        while (matchEmo.find()) {
            switch (matchEmo.group()) {
            case "😀":
                replacement = ":D";
                break;
            case "🙂":
            case "☺":
                replacement = ":)";
                break;
            case "😉":
                replacement = ";)";
                break;
            case "😛":
                replacement = ":P";
                break;
            case "😃":
                replacement = "=D";
                break;
            case "👍":
                replacement = "(Y)";
                break;
            case "🙁":
            case "😞":
                replacement = ":(";
                break;
            case "😐":
                replacement = ":|";
                break;
            case "😕":
                replacement = ":/";
                break;
            case "😖":
                replacement = ":S";
            default:
                System.err.println("Emoticon no encontrado: " + message);
                break;
            }
            message = message.replaceAll(matchEmo.group(), replacement);
        }
        return message;
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
            arffHeader2(bw);

            FreelingAnalyzer3 freelingAnalyzer = null;
            if (useFreeling)
                freelingAnalyzer = FreelingAnalyzer3.getInstance();

            // Guardar los datos en un archivo ARFF
            for (int i = 0; i < cells.size(); i++) {
                List<Cell> rowCells = (List<Cell>) cells.get(i);

                Cell cellMessage = (Cell) rowCells.get(0);
                Cell cellConduct = (Cell) rowCells.get(1);

                //////////////////// Conducta ////////////////////
                // Obtener el valor numerico de la celda
                String area;
                String reaccion;
                String conducta = BigDecimal.valueOf((int) (cellConduct.getNumericCellValue())).toPlainString();
                if (!conducta.equals("0")) {
                    // conducta = "?";
                    switch (conducta) {
                    case "1":
                    case "2":
                    case "3":
                        area = AREA_SOCIO_EMOCIONAL;
                        reaccion = REACCION_POSITIVA;
                        break;
                    case "10":
                    case "11":
                    case "12":
                        area = AREA_SOCIO_EMOCIONAL;
                        reaccion = REACCION_NEGATIVA;
                        break;
                    case "4":
                    case "5":
                    case "6":
                        area = AREA_TAREA;
                        reaccion = REACCION_RESPUESTA;
                        break;
                    case "7":
                    case "8":
                    case "9":
                        area = AREA_TAREA;
                        reaccion = REACCION_PREGUNTA;
                        break;
                    default:
                        area = "?";
                        reaccion = "?";
                        break;
                    }
                    bw.write(area + "," + reaccion + "," + conducta + ",");

                    //////////////////// Mensaje ////////////////////
                    String message = replaceEmojisWithText(cellMessage.toString());

                    // Freeling
                    // Stemmer: método para reducir una palabra a su raíz o a un stem o lema
                    if (useFreeling) {
                        ListSentence ls = freelingAnalyzer.analyze(message);
                        message = freelingAnalyzer.getLemmas(ls);
                    }

                    bw.write("'" + addEscapeChar(message) + "'");

                    //////////////////// Categorias ////////////////////
                    if (useFreeling) {
                        bw.write("," + freelingAnalyzer.getAdjectivesCount() + "," + freelingAnalyzer.getAdverbsCount() + ","
                                + freelingAnalyzer.getDeterminantsCount() + "," + freelingAnalyzer.getNamesCount() + ","
                                + freelingAnalyzer.getVerbsCount() + "," + freelingAnalyzer.getPronounsCount() + ","
                                + freelingAnalyzer.getConjunctionsCount() + "," + freelingAnalyzer.getInterjectionsCount() + ","
                                + freelingAnalyzer.getPrepositionsCount() + "," + freelingAnalyzer.getPunctuationCount() + ","
                                + freelingAnalyzer.getNumeralsCount() + "," + freelingAnalyzer.getDatesAndTimesCount() + ","
                                + freelingAnalyzer.getEmoticonPosCount() + "," + freelingAnalyzer.getEmoticonNegCount() + ","
                                + freelingAnalyzer.getEmoticonNeuCount());
                    }

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
