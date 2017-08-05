package org.processDataset;

import org.commons.Constants;
import org.preprocessDataset.Freeling;
import org.weka.Weka;

import weka.core.Instances;

public class PhasesProccessingSingleClassifier extends ProcessDataset {

    private Phase1Processing phase1;
    private Phase2Processing phase2;
    private Phase3Processing phase3;
    
    private Weka wekaClassifier;
    
    /**
     * Constructor
     * @param wekaClassifier Weka Clasificador de todas las fases
     * @param useFreeling boolean Determina si se utilizará Freeling o no 
     * @param useEasyProcessing boolean Determina si se utilizará el procesado simple o el avanzado
     */
    public PhasesProccessingSingleClassifier(Weka wekaClassifier, boolean useFreeling, boolean useEasyProcessing) {
        super(useFreeling, useEasyProcessing);

        this.wekaClassifier = wekaClassifier;
        
        phase1 = new Phase1Processing(useFreeling);
        phase2 = new Phase2Processing(useFreeling);
        phase3 = new Phase3Processing(useFreeling);
    }

    /**
     * Entrena todos los clasificadores WEKA con el archivo pasado por parámetro
     * @param fileName String Archivo para entrenar
     * @return String Archivo que se utilizó para el entrenamiento
     */
    @Override
    public String train(String fileName) {
        
        String tempFileName = copyFileToTempDir(fileName, true, "train");
        fileName = tempFileName;
                
        if (useFreeling)
            fileName = preprocessUsingFreeling(fileName);
        
        trainPhase1(fileName);
        trainPhase2(fileName);
        trainPhase3(fileName);
        
        return tempFileName;
    }
    
    /**
     * Entrena el clasificador de la Fase 1
     * @param fileName String Dataset
     */
    public void trainPhase1(String fileName) {
    	
    	Instances evaluationDataset;
        String modelFileName;
        String phase1FileName = phase1.getDataset(fileName);
         
        evaluationDataset = wekaClassifier.evaluate(phase1FileName);        
        modelFileName = phase1FileName.substring(0, phase1FileName.lastIndexOf(Constants.ARFF_FILE)) + "-singleClassifier" + Constants.DAT_FILE;
        wekaClassifier.train(evaluationDataset, modelFileName);
        
        phase1.setEvaluationResults(wekaClassifier.getEvaluationResults());
    }
    
    /**
     * Entrena los clasificadores de la Fase 2
     * @param fileName String Dataset
     */
    public void trainPhase2(String fileName) {
    	
    	Instances evaluationDataset;
        String modelFileName;
        String phase2FileName = phase2.getDataset(fileName);

        evaluationDataset = wekaClassifier.evaluate(phase2FileName);        
        modelFileName = phase2FileName.substring(0, phase2FileName.lastIndexOf(Constants.ARFF_FILE)) + "-singleClassifier" + Constants.DAT_FILE;
        wekaClassifier.train(evaluationDataset, modelFileName);
        
        phase2.setEvaluationResults(wekaClassifier.getEvaluationResults());
    }

    /**
     * Entrena los clasificadores de la Fase 3
     * @param fileName String Dataset
     */
	public void trainPhase3(String fileName) {
	
    	Instances evaluationDataset;
        String modelFileName;
        String phase3FileName = phase3.getDataset(fileName);
        
        evaluationDataset = wekaClassifier.evaluate(phase3FileName);
        modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(Constants.ARFF_FILE)) + "-singleClassifier" + Constants.DAT_FILE;
        wekaClassifier.train(evaluationDataset, modelFileName);
        
        phase3.setEvaluationResults(wekaClassifier.getEvaluationResults());
	}

	/**
	 * Clasifica un dataset pasado por parámetro utilizando los modelos creados previamente
	 * @param fileName String Dataset
	 * @param trainFileName Archivo que contiene el nombre del modelo
	 * @return String Archivo con dataset clasificado
	 */
    @Override
    public String classify(String fileName, String trainFileName) {

        String originalFileName = fileName;
        String modelFileName = null;

        String tempFileName = copyFileToTempDir(fileName, false, "classify");
        fileName = tempFileName;
        
        fileName = Freeling.splitSentences(fileName);
        
        if (useEasyProcessing) {
        	fileName = preprocessUsingFreeling(fileName);
        }
        else {
        	modelFileName = trainFileName.substring(0, trainFileName.lastIndexOf(Constants.ARFF_FILE));
        	if (useFreeling) {
        		fileName = preprocessUsingFreeling(fileName);
        		modelFileName += "-freeling";
        	}
        }
        
        // Clasificación
        Phase1Results phase1Results = classifyPhase1(fileName, modelFileName);
        Phase2Results phase2Results = classifyPhase2(fileName, modelFileName, phase1Results.getLabeledFileName());
        Phase3Results phase3Results = classifyPhase3(fileName, modelFileName, phase2Results.getLabeledFileNameClassifier1(), phase2Results.getLabeledFileNameClassifier2());
        
        saveResults(phase1Results, phase2Results, phase3Results);
        
        return originalFileName;
    }
    
    /**
     * Clasifica un dataset pasado por parámetro en la Fase 1
     * @param fileName String Dataset 
     * @param modelFileName Modelo
     * @return Phase1Results Resultado de la clasificadión de la Fase 1
     */
    private Phase1Results classifyPhase1(String fileName, String modelFileName) {
        
        String phaseFileName = phase1.getDataset(fileName);
        String labeledFileNamePhase1 = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-labeled-phase1-singleClassifier" + Constants.ARFF_FILE;
        
        String model;
        if (useEasyProcessing)
        	model = Constants.MODEL_FOLDER + wekaClassifier.getClassifierClassName() + "-phase1" + Constants.DAT_FILE;
        else
        	model = modelFileName + "-phase1-singleClassifier" + Constants.DAT_FILE;
        
        String phase1ClassifierResults = wekaClassifier.classify(model, phaseFileName, labeledFileNamePhase1, "2");
        
        Phase1Results results = new Phase1Results(phase1ClassifierResults, labeledFileNamePhase1);
        
        return results;
    }
    
    /**
     * Clasifica un dataset pasado por parámetro en la Fase 2
     * @param fileName String Dataset 
     * @param modelFileName Modelo
     * @param labeledFileNamePhase1 Dataset resultado de la Fase 1
     * @return Phase2Results Resultado de la clasificadión de la Fase 2
     */
    private Phase2Results classifyPhase2(String fileName, String modelFileName, String labeledFileNamePhase1) {

        String phaseFileName = phase2.getTrainDataset(labeledFileNamePhase1, true, "(classifier 1 and 2)");
        
        String modelClassifier;
        if (useEasyProcessing)
        	modelClassifier = Constants.MODEL_FOLDER + wekaClassifier.getClassifierClassName() + "-phase2" + Constants.DAT_FILE;
        else
        	modelClassifier = modelFileName + "-phase2-singleClassifier" + Constants.DAT_FILE;
        
        String labeledFileNamePhase2Classifier1 = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-labeled-phase2-singleClassifier" + Constants.ARFF_FILE;
        String phase2ClassifierResults = wekaClassifier.classify(modelClassifier, phaseFileName, labeledFileNamePhase2Classifier1, "3");

        Phase2Results results = new Phase2Results(phase2ClassifierResults, "", labeledFileNamePhase2Classifier1, "");
        
        return results;
    }
    
    /**
     * Clasifica un dataset pasado por parámetro en la Fase 3
     * @param fileName String Dataset 
     * @param modelFileName Modelo
     * @param labeledFileNamePhase2Classifier1 Dataset resultado del clasificador 1 de la Fase 2
     * @param labeledFileNamePhase2Classifier2 Dataset resultado del clasificador 2 de la Fase 2
     * @return Phase2Results Resultado de la clasificadión de la Fase 3
     */
    private Phase3Results classifyPhase3(String fileName, String modelFileName, String labeledFileNamePhase2Classifier1, String labeledFileNamePhase2Classifier2) {
    	
        String phaseFileName = phase3.getTrainDataset(labeledFileNamePhase2Classifier1, true, "(classifier 1,2,3 and 4)");
        
        String modelClassifier;
        if (useEasyProcessing)
        	modelClassifier = Constants.MODEL_FOLDER + wekaClassifier.getClassifierClassName() + "-phase3" + Constants.DAT_FILE;
        else
        	modelClassifier = modelFileName + "-phase3-singleClassifier" + Constants.DAT_FILE;
        
        String labeledFileNamePhase3Classifier1 = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-labeled-phase3-singleClassifier" + Constants.ARFF_FILE;
        String phase3Classifier1Results = wekaClassifier.classify(modelClassifier, phaseFileName, labeledFileNamePhase3Classifier1, "4");
    
        Phase3Results results = new Phase3Results(phase3Classifier1Results, "", "", "", labeledFileNamePhase3Classifier1, "", "", "");
        
        return results;
    }
    
    /**
     * Alcacena los resultados de las 3 fases de clasificación
     * @param phase1Results Phase1Results Resultado de calsificación de la Fase 1
     * @param phase2Results Phase2Results Resultado de calsificación de la Fase 2
     * @param phase3Results Phase3Results Resultado de calsificación de la Fase 3
     */
    private void saveResults(Phase1Results phase1Results, Phase2Results phase2Results, Phase3Results phase3Results) {
        
        String fileHeader;
        String fileData;
        // Fase 1
        fileHeader = getResultHeader(phase1Results.getClassifierResults());
        fileData = getResultData(phase1Results.getClassifierResults());
        classifyingResults =      "===================\n      Phase 1\n===================\n" + fileHeader + "@data" + fileData;
        
        // Fase 2
        fileHeader = getResultHeader(phase2Results.getClassifier1Results());
        fileData = getResultData(phase2Results.getClassifier1Results());
        classifyingResults += "\n\n===================\n      Phase 2\n===================\n" + fileHeader + "@data" + fileData;
        
        // Fase 3
        fileHeader = getResultHeader(phase3Results.getClassifier1Results());
        fileData = getResultData(phase3Results.getClassifier1Results());
        classifyingResults += "\n\n===================\n      Phase 3\n===================\n" + fileHeader + "@data" + fileData;        
    }
    
    /**
     * Devuelve un header de archivo ARFF 
     * @param result String Resultado
     * @return String Header de archivo ARFF
     */
    private String getResultHeader(String result) {
    	
    	return result.substring(result.indexOf("@attribute"), result.indexOf("@data"));
    }
    
    /**
     * Devuelve los datos de la sección @data 
     * @param result String Resultado
     * @return Datos de la sección @data
     */
    private String getResultData(String result) {
    	
		return result.substring(result.indexOf("@data") + 5, result.length());
	}

    /**
     * Devuelve los resultados del entrenamiento
     * @return String Resultados del entrenamiento 
     */
    @Override
    public String getTrainingResults() {
    	
    	// Fase 1
    	trainingResults = "===================\n      Phase 1\n===================\n" + phase1.getResults();
    	
        // Fase 2
        trainingResults += "\n===================\n      Phase 2\n===================\n" + phase2.getResults();
               
    	// Fase 3
        trainingResults += "\n===================\n      Phase 3\n===================\n" + phase3.getResults();
        
        return trainingResults;
    }

    /**
     * Devuelve los resultados de la clasificación
     * @return String Resultados de la clasificación
     */
    @Override
    public String getClassificationResults() {

        return classifyingResults;
    }
}
