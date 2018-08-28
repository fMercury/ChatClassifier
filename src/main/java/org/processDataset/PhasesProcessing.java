package org.processDataset;

import org.commons.Constants;
import org.preprocessDataset.Freeling;
import org.weka.Weka;

import weka.core.Instances;

public class PhasesProcessing extends ProcessDataset {

    private Phase1Processing phase1;
    private Phase2Processing phase2;
    private Phase3Processing phase3;
    
    private Weka wekaPhase1Classifier;
    private Weka wekaPhase2Classifier1;
    private Weka wekaPhase2Classifier2;
    private Weka wekaPhase3Classifier1;
    private Weka wekaPhase3Classifier2;
    private Weka wekaPhase3Classifier3;
    private Weka wekaPhase3Classifier4;
    
    /**
     * Constructor
     * @param wekaPhase1Classifier Weka Clasificador de la Fase 1
     * @param wekaPhase2Classifier1 Weka Clasificador 1 de la Fase 2 
     * @param wekaPhase2Classifier2 Weka Clasificador 2 de la Fase 2
     * @param wekaPhase3Classifier1 Weka Clasificador 1 de la Fase 3
     * @param wekaPhase3Classifier2 Weka Clasificador 2 de la Fase 3
     * @param wekaPhase3Classifier3 Weka Clasificador 3 de la Fase 3
     * @param wekaPhase3Classifier4 Weka Clasificador 4 de la Fase 3
     * @param useFreeling boolean Determina si se utilizará Freeling o no 
     * @param useEasyProcessing boolean Determina si se utilizará el procesado simple o el avanzado
     */
    public PhasesProcessing(Weka wekaPhase1Classifier, Weka wekaPhase2Classifier1, Weka wekaPhase2Classifier2, 
                            Weka wekaPhase3Classifier1, Weka wekaPhase3Classifier2, Weka wekaPhase3Classifier3, 
                            Weka wekaPhase3Classifier4, boolean useFreeling, boolean useEasyProcessing) {
        super(useFreeling, useEasyProcessing);

        this.wekaPhase1Classifier = wekaPhase1Classifier;
        this.wekaPhase2Classifier1 = wekaPhase2Classifier1;
        this.wekaPhase2Classifier2 = wekaPhase2Classifier2;
        this.wekaPhase3Classifier1 = wekaPhase3Classifier1;
        this.wekaPhase3Classifier2 = wekaPhase3Classifier2;
        this.wekaPhase3Classifier3 = wekaPhase3Classifier3;
        this.wekaPhase3Classifier4 = wekaPhase3Classifier4;
        
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
    private void trainPhase1(String fileName) {
    	
    	Instances evaluationDataset;
        String modelFileName;
        String phase1FileName = phase1.getDataset(fileName);
         
        evaluationDataset = wekaPhase1Classifier.evaluate(phase1FileName);        
        modelFileName = phase1FileName.substring(0, phase1FileName.lastIndexOf(Constants.ARFF_FILE)) + Constants.DAT_FILE;
        wekaPhase1Classifier.train(evaluationDataset, modelFileName);
        
        phase1.setEvaluationResults(wekaPhase1Classifier.getEvaluationResults());
    }
    
    /**
     * Entrena los clasificadores de la Fase 2
     * @param fileName String Dataset
     */
    private void trainPhase2(String fileName) {
    	
    	Instances evaluationDataset;
        String modelFileName;
        String phase2FileName = phase2.getDataset(fileName);

        // Clasificador 1
        evaluationDataset = wekaPhase2Classifier1.evaluate(phase2FileName, "2", "last");        
        modelFileName = phase2FileName.substring(0, phase2FileName.lastIndexOf(Constants.ARFF_FILE)) + "-classifier1" + Constants.DAT_FILE;
        wekaPhase2Classifier1.train(evaluationDataset, modelFileName);
        
        // Clasificador 2        
        evaluationDataset = wekaPhase2Classifier2.evaluate(phase2FileName, "2", "first");                
        modelFileName = phase2FileName.substring(0, phase2FileName.lastIndexOf(Constants.ARFF_FILE)) + "-classifier2" + Constants.DAT_FILE;
        wekaPhase2Classifier2.train(evaluationDataset, modelFileName);
        
        phase2.setEvaluationResults(wekaPhase2Classifier1.getEvaluationResults() + wekaPhase2Classifier2.getEvaluationResults());
    }

    /**
     * Entrena los clasificadores de la Fase 3
     * @param fileName String Dataset
     */
	private void trainPhase3(String fileName) {
	
    	Instances evaluationDataset;
        String modelFileName;
        String phase3FileName = phase3.getDataset(fileName);
        
        // Clasificador 1
        evaluationDataset = wekaPhase3Classifier1.evaluate(phase3FileName, "2", "2,3,4");
        modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(Constants.ARFF_FILE)) + "-classifier1" + Constants.DAT_FILE;
        wekaPhase3Classifier1.train(evaluationDataset, modelFileName);
        
        // Clasificador 2
        evaluationDataset = wekaPhase3Classifier2.evaluate(phase3FileName, "2", "1,2,3");
        modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(Constants.ARFF_FILE)) + "-classifier2" + Constants.DAT_FILE;
        wekaPhase3Classifier2.train(evaluationDataset, modelFileName);
        
        // Clasificador 3
        evaluationDataset = wekaPhase3Classifier3.evaluate(phase3FileName, "2", "1,2,4");
        modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(Constants.ARFF_FILE)) + "-classifier3" + Constants.DAT_FILE;
        wekaPhase3Classifier3.train(evaluationDataset, modelFileName);
        
        // Clasificador 4
        evaluationDataset = wekaPhase3Classifier4.evaluate(phase3FileName, "2", "1,3,4");
        modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(Constants.ARFF_FILE)) + "-classifier4" + Constants.DAT_FILE;
        wekaPhase3Classifier4.train(evaluationDataset, modelFileName);
        
        phase3.setEvaluationResults(wekaPhase3Classifier1.getEvaluationResults() + wekaPhase3Classifier2.getEvaluationResults() + 
                wekaPhase3Classifier3.getEvaluationResults() + wekaPhase3Classifier4.getEvaluationResults());
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
        
        String finalClassificationFile = mergeFinalClassification(phase3Results, originalFileName);
        saveResults(phase1Results, phase2Results, phase3Results);
        
        return finalClassificationFile;
    }
    
    /**
     * Clasifica un dataset pasado por parámetro en la Fase 1
     * @param fileName String Dataset 
     * @param modelFileName Modelo
     * @return Phase1Results Resultado de la clasificadión de la Fase 1
     */
    private Phase1Results classifyPhase1(String fileName, String modelFileName) {
        
        String phaseFileName = phase1.getDataset(fileName);
        String labeledFileNamePhase1 = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-labeled-phase1" + Constants.ARFF_FILE;
        
        String model;
        if (useEasyProcessing)
        	model = Constants.MODEL_FOLDER + wekaPhase1Classifier.getClassifierClassName() + "-phase1" + Constants.DAT_FILE;
        else
        	model = modelFileName + "-phase1" + Constants.DAT_FILE;
        
        String phase1ClassifierResults = wekaPhase1Classifier.classify(model, phaseFileName, labeledFileNamePhase1, "2");
        
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
        
        String modelClassifier1;
        if (useEasyProcessing)
        	modelClassifier1 = Constants.MODEL_FOLDER + wekaPhase2Classifier1.getClassifierClassName() + "-phase2" + Constants.DAT_FILE;
        else
        	modelClassifier1 = modelFileName + "-phase2-classifier1" + Constants.DAT_FILE;
        String modelClassifier2;
        if (useEasyProcessing)
        	modelClassifier2 = Constants.MODEL_FOLDER + wekaPhase2Classifier2.getClassifierClassName() + "-phase2" + Constants.DAT_FILE;
        else
        	modelClassifier2 = modelFileName + "-phase2-classifier2" + Constants.DAT_FILE;        
        
        // Clasificador 1
        String labeledFileNamePhase2Classifier1 = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-labeled-phase2-classifier1" + Constants.ARFF_FILE;
        String phase2Classifier1Results = wekaPhase2Classifier1.classify(modelClassifier1, phaseFileName, labeledFileNamePhase2Classifier1, "3", "2", "last");
        // Clasificador 2
        String labeledFileNamePhase2Classifier2 = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-labeled-phase2-classifier2" + Constants.ARFF_FILE;
        String phase2Classifier2Results = wekaPhase2Classifier2.classify(modelClassifier2, phaseFileName, labeledFileNamePhase2Classifier2, "3", "2", "first");

        Phase2Results results = new Phase2Results(phase2Classifier1Results, phase2Classifier2Results, labeledFileNamePhase2Classifier1, labeledFileNamePhase2Classifier2);
        
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
    	
        String phaseFileName = phase3.getTrainDataset(labeledFileNamePhase2Classifier1, true, "(classifier 1 and 4)");
        
        String modelClassifier1;
        if (useEasyProcessing)
        	modelClassifier1 = Constants.MODEL_FOLDER + wekaPhase3Classifier1.getClassifierClassName() + "-phase3" + Constants.DAT_FILE;
        else
        	modelClassifier1 = modelFileName + "-phase3-classifier1" + Constants.DAT_FILE;
        String modelClassifier2;
        if (useEasyProcessing)
        	modelClassifier2 = Constants.MODEL_FOLDER + wekaPhase3Classifier2.getClassifierClassName() + "-phase3" + Constants.DAT_FILE;
        else
        	modelClassifier2 = modelFileName + "-phase3-classifier2" + Constants.DAT_FILE;
        String modelClassifier3;
        if (useEasyProcessing)
        	modelClassifier3 = Constants.MODEL_FOLDER + wekaPhase3Classifier3.getClassifierClassName() + "-phase3" + Constants.DAT_FILE;
        else
        	modelClassifier3 = modelFileName + "-phase3-classifier3" + Constants.DAT_FILE;
        String modelClassifier4;
        if (useEasyProcessing)
        	modelClassifier4 = Constants.MODEL_FOLDER + wekaPhase3Classifier4.getClassifierClassName() + "-phase3" + Constants.DAT_FILE;
        else
        	modelClassifier4 = modelFileName + "-phase3-classifier4" + Constants.DAT_FILE;
        
        // Clasificador 1
        String labeledFileNamePhase3Classifier1 = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-labeled-phase3-classifier1" + Constants.ARFF_FILE;
        String phase3Classifier1Results = wekaPhase3Classifier1.classify(modelClassifier1, phaseFileName, labeledFileNamePhase3Classifier1, "4", "2", "2,3,4");
        // Clasificador 4
        String labeledFileNamePhase3Classifier4 = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-labeled-phase3-classifier4" + Constants.ARFF_FILE;
        String phase3Classifier4Results = wekaPhase3Classifier4.classify(modelClassifier4, phaseFileName, labeledFileNamePhase3Classifier4, "4", "2", "1,3,4");
        
        phaseFileName = phase3.getTrainDataset(labeledFileNamePhase2Classifier2, true, "(classifier 2 and 3)");
        // Clasificador 2
        String labeledFileNamePhase3Classifier2 = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-labeled-phase3-classifier2" + Constants.ARFF_FILE;
        String phase3Classifier2Results = wekaPhase3Classifier2.classify(modelClassifier2, phaseFileName, labeledFileNamePhase3Classifier2, "4", "2", "1,2,3");
        // Clasificador 3
        String labeledFileNamePhase3Classifier3 = fileName.substring(0, fileName.lastIndexOf(Constants.ARFF_FILE)) + "-labeled-phase3-classifier3" + Constants.ARFF_FILE;
        String phase3Classifier3Results = wekaPhase3Classifier3.classify(modelClassifier3, phaseFileName, labeledFileNamePhase3Classifier3, "4", "2", "1,2,4");
    
        Phase3Results results = new Phase3Results(phase3Classifier1Results, phase3Classifier2Results, phase3Classifier3Results, phase3Classifier4Results, labeledFileNamePhase3Classifier1, labeledFileNamePhase3Classifier2, labeledFileNamePhase3Classifier3, labeledFileNamePhase3Classifier4);
        
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
        fileData = getResultData(phase2Results.getClassifier2Results());
        classifyingResults += fileData;
        
        // Fase 3
        fileHeader = getResultHeader(phase3Results.getClassifier1Results());
        fileData = getResultData(phase3Results.getClassifier1Results());
        classifyingResults += "\n\n===================\n      Phase 3\n===================\n" + fileHeader + "@data" + fileData;
        fileData = getResultData(phase3Results.getClassifier2Results());
        classifyingResults += fileData;
        fileData = getResultData(phase3Results.getClassifier3Results());
        classifyingResults += fileData;
        fileData = getResultData(phase3Results.getClassifier4Results());
        classifyingResults += fileData;
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
    	
    	double correctlyClassifiedInstances;
    	double incorrectlyClassifiedInstances;	
    	double correctPercentage;
    	double incorrectPercentage;    	
    	String correctlyClassified;
    	String incorrectlyClassified;
    	String spaces;
    	
    	// Fase 1
    	trainingResults = "===================\n      Phase 1\n===================\n" + phase1.getResults();
    	trainingResults += "\n==================\n  Phase 1 resume \n==================\n";
    	
    	correctlyClassifiedInstances = wekaPhase1Classifier.getCorrectClassifiedInstances();
    	incorrectlyClassifiedInstances = wekaPhase1Classifier.getIncorrectClassifiedInstances();	
    	correctPercentage = correctlyClassifiedInstances * 100.0 / (correctlyClassifiedInstances + incorrectlyClassifiedInstances);
    	incorrectPercentage = 100.0 - correctPercentage;
    	
    	correctlyClassified = "Correctly Classified Instances         " + (int)correctlyClassifiedInstances;
    	spaces = new String(new char[58 - correctlyClassified.length()]).replace("\0", " ");
    	trainingResults += correctlyClassified + spaces + String.format ("%.4f", correctPercentage) + " %\n";
    	incorrectlyClassified = "Incorrectly Classified Instances       " + (int)incorrectlyClassifiedInstances;
    	spaces = new String(new char[58 - incorrectlyClassified.length()]).replace("\0", " ");
    	trainingResults += incorrectlyClassified + spaces +  String.format ("%.4f", incorrectPercentage) + " %" + "\n";

        // Fase 2
        trainingResults += "\n===================\n      Phase 2\n===================\n" + phase2.getResults();
        trainingResults += "\n==================\n  Phase 2 resume \n==================\n";
        
        correctlyClassifiedInstances = wekaPhase2Classifier1.getCorrectClassifiedInstances() + wekaPhase2Classifier2.getCorrectClassifiedInstances();
    	incorrectlyClassifiedInstances = wekaPhase2Classifier1.getIncorrectClassifiedInstances() + wekaPhase2Classifier2.getIncorrectClassifiedInstances();	
    	correctPercentage = correctlyClassifiedInstances * 100.0 / (correctlyClassifiedInstances + incorrectlyClassifiedInstances);
    	incorrectPercentage = 100.0 - correctPercentage;
    	
    	correctlyClassified = "Correctly Classified Instances         " + (int)correctlyClassifiedInstances;
    	spaces = new String(new char[58 - correctlyClassified.length()]).replace("\0", " ");
    	trainingResults += correctlyClassified + spaces + String.format ("%.4f", correctPercentage) + " %\n";
    	incorrectlyClassified = "Incorrectly Classified Instances       " + (int)incorrectlyClassifiedInstances;
    	spaces = new String(new char[58 - incorrectlyClassified.length()]).replace("\0", " ");
    	trainingResults += incorrectlyClassified + spaces +  String.format ("%.4f", incorrectPercentage) + " %" + "\n";
        
    	// Fase 3
        trainingResults += "\n===================\n      Phase 3\n===================\n" + phase3.getResults();
        trainingResults += "\n==================\n  Phase 3 resume \n==================\n";
        
    	correctlyClassifiedInstances = wekaPhase3Classifier1.getCorrectClassifiedInstances() + wekaPhase3Classifier2.getCorrectClassifiedInstances() + 
    								 wekaPhase3Classifier3.getCorrectClassifiedInstances() + wekaPhase3Classifier4.getCorrectClassifiedInstances();
    	incorrectlyClassifiedInstances = wekaPhase3Classifier1.getIncorrectClassifiedInstances() + wekaPhase3Classifier2.getIncorrectClassifiedInstances() + 
    								   wekaPhase3Classifier3.getIncorrectClassifiedInstances() + wekaPhase3Classifier4.getIncorrectClassifiedInstances();	
    	correctPercentage = correctlyClassifiedInstances * 100.0 / (correctlyClassifiedInstances + incorrectlyClassifiedInstances);
    	incorrectPercentage = 100.0 - correctPercentage;
    	
    	correctlyClassified = "Correctly Classified Instances         " +(int) correctlyClassifiedInstances;
    	spaces = new String(new char[58 - correctlyClassified.length()]).replace("\0", " ");
    	trainingResults += correctlyClassified + spaces + String.format ("%.4f", correctPercentage) + " %\n";
    	incorrectlyClassified = "Incorrectly Classified Instances       " + (int)incorrectlyClassifiedInstances;
    	spaces = new String(new char[58 - incorrectlyClassified.length()]).replace("\0", " ");
    	trainingResults += incorrectlyClassified + spaces +  String.format ("%.4f", incorrectPercentage) + " %" + "\n";

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
