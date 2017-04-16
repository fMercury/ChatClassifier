package org.commons;

import java.io.File;

/**
 * Contiene las constantes utilizadas en todo el código
 * @author martinmineo
 *
 */
public class Constants {

    public static final String RESOURCES = "src" + File.separator + "main" + File.separator + "resources";
    public static final String DATASETS_FOLDER = "datasets" + File.separator;
    public static final String HANGOUTS_FOLDER = DATASETS_FOLDER + "hangouts" + File.separator;
    public static final String TEMP_FOLDER = "temp" + File.separator;
    public static final String RESULTS_FOLDER = "results";
    public static final String CLASSIFICATION_FOLDER = RESULTS_FOLDER + File.separator + "classification";
    public static final String LABELED_FOLDER = RESULTS_FOLDER + File.separator + "labeled" + File.separator;
    public static final String ANALYSIS_FOLDER = RESULTS_FOLDER + File.separator + "analysis" + File.separator;
    public static final String MODEL_FOLDER = "models" + File.separator;
    public static final String ARFF_FILE = ".arff";
    public static final String CSV_FILE = ".csv";
    public static final String DAT_FILE = ".dat";
    public static final String JSON_FILE = ".json";
    public static final String XLS_FILE = ".xls";
    public static final String TXT_FILE = ".txt";
    
}
