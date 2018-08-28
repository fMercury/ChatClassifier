package org.processDataset;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.commons.Constants;
import org.preprocessDataset.Freeling;
import org.weka.Weka;

/**
 * Clase abstracta que reune funcionalidad utilizada por DirectProcessing y PhasesProcessing
 * @author martinmineo
 *
 */
public abstract class ProcessDataset {
    
    protected String trainingResults;
    protected String classifyingResults;
    
    protected boolean useFreeling;
    protected Freeling freeling;
    
    protected boolean useEasyProcessing;
    
    /**
     * Constructor
     * @param useFreeling boolean Determina si se utilizará Freeling o no 
     * @param useEasyProcessing boolean Determina si se utilizará el procesado simple o el avanzado
     */
    public ProcessDataset(boolean useFreeling, boolean useEasyProcessing) {
        
        this.useFreeling = useFreeling;        
        freeling = new Freeling();
        this.useEasyProcessing = useEasyProcessing;        
    }
    
    public abstract String train(String fileName);
    public abstract String classify(String fileName, String trainFileName);
    public abstract String getTrainingResults();
    public abstract String getClassificationResults();
    
    /**
     * Procesa el dataset con Freeling y devuelve los resultados
     * @param fileName Dataset
     * @return String Dataset procesado con Freeling
     */
    protected String preprocessUsingFreeling(String fileName) {
        
        return freeling.freelingAnalisys(fileName);
    }
    
    /**
     * Copia un archivo a una carpeta temporal
     * @param filePath String Ruta del archivo
     * @param deleteTempFolder Determina si es necesario eliminar la carpeta temporal o no
     * @param stage Etapa (clasificación y entrenamiento)
     * @return String Nueva nombre de archivo
     */
    protected String copyFileToTempDir(String filePath, boolean deleteTempFolder, String stage) {
        
        int lastPathSeparator = filePath.lastIndexOf(File.separator);
        String fileFolder = filePath.substring(0, lastPathSeparator);
        String fileName = filePath.substring(lastPathSeparator + 1, filePath.length());
        String folderName = fileFolder + File.separator + Constants.TEMP_FOLDER + new SimpleDateFormat("yyyyMMdd HHmmssss").format(new Date()) + " " + stage + File.separator;
        String newFilePath = folderName + fileName;
        
        File folder = new File(folderName);
        File source = new File(filePath);
        File dest = new File(newFilePath);
        try {
            if (folder.exists() && deleteTempFolder)
                FileUtils.deleteDirectory(folder);
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return newFilePath;
    }
    
    protected String mergeFinalClassification(Phase3Results phase3Results, String fileName) {
        
        String finalFile = Constants.LABELED_FOLDER + new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()) + File.separator + fileName.substring(fileName.lastIndexOf(File.separator), fileName.length());;
        Weka.mergeInstances(phase3Results.getLabeledFileNameClassifier1(), phase3Results.getLabeledFileNameClassifier2(), phase3Results.getLabeledFileNameClassifier3(), phase3Results.getLabeledFileNameClassifier4(), finalFile);
        
        return finalFile;
        
    }
}
