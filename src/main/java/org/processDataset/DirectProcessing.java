package org.processDataset;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.commons.Constants;
import org.preprocessDataset.Freeling;
import org.weka.Weka;

import weka.core.Instances;

/**
 * Procesado directo (se utiliza solo un clasificador de WEKA) de los datos
 * 
 * @author martinmineo
 *
 */
public class DirectProcessing extends ProcessDataset {
    
    private Weka wekaClassifier;
    
    /**
     * Constructor
     * @param weka Weka Clasificador
     * @param useFreeling boolean Determina si se utilizará Freeling o no 
     * @param useEasyProcessing boolean Determina si se utilizará el procesado simple o el avanzado
     */
    public DirectProcessing(Weka weka, boolean useFreeling, boolean useEasyProcessing) {
        super(useFreeling, useEasyProcessing);
        
        this.wekaClassifier = weka; 
    }

    /**
     * Entrena el clasificador de WEKA con el archivo pasado por parámetro
     * @param fileName String Archivo para entrenar
     * @retur String Archivo que se utilizó para el entrenamiento
     */
    @Override
    public String train(String fileName) {
        
        String tempFileName = copyFileToTempDir(fileName, true, "train");
        fileName = tempFileName;
        
        if (useFreeling)
            fileName = preprocessUsingFreeling(fileName);
        
        Instances trainDataset = wekaClassifier.evaluate(fileName);
        trainingResults = wekaClassifier.getEvaluationResults();
        
        String modelFileName = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + ".dat";
        wekaClassifier.train(trainDataset, modelFileName);
        
        return tempFileName;
    }

    /**
     * Clasifica, utilizando WEKA, un dataset pasado por parámetro utilizando un modelo creado previamente
     * @param fileName String Archivo a clasificar
     * @param trainFileName String Archivo que contiene el nombre del modelo a utilizar
     * @return String Archivo que contiene el resultado de la clasificación
     */
    @Override
    public String classify(String fileName, String trainFileName) {

        String originalFileName = fileName;        
        String modelFileName; 
        
        String tempFileName = copyFileToTempDir(fileName, false, "classify");
        fileName = tempFileName;
        
        fileName = Freeling.splitSentences(fileName);
        
        if (useEasyProcessing) {
        	modelFileName = Constants.MODEL_FOLDER + wekaClassifier.getClassifierClassName();
      		fileName = preprocessUsingFreeling(fileName);
        }
        else {
        	modelFileName = trainFileName.substring(0, trainFileName.lastIndexOf(Constants.ARFF_FILE));
        	if (useFreeling) {
        		fileName = preprocessUsingFreeling(fileName);
        		modelFileName += "-freeling";
        	}
        }
        
        String labeledFileName = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-labeled" + Constants.ARFF_FILE;
        classifyingResults = wekaClassifier.classify(modelFileName + ".dat", fileName, labeledFileName, "2");
        
        String destFileName = Constants.LABELED_FOLDER + new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()) + originalFileName.substring(originalFileName.lastIndexOf(File.separator), originalFileName.length());
        Weka.copyDataset(labeledFileName, destFileName);
        
        return destFileName;
    }

    /**
     * Devuelve el resultado del entrenamiento
     * @return String Resultado del entrenamiento
     */
    @Override
    public String getTrainingResults() {
      
        return trainingResults;
    }

    /**
     * Devuelve el resultado de la clasificación
     * @return Resultado de la clasificación
     */
    @Override
    public String getClassificationResults() {

        return classifyingResults;
    }

}
