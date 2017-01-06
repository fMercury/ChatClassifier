package org.processDataset;

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
    
    public PhasesProcessing(Weka wekaPhase1Classifier, Weka wekaPhase2Classifier1, Weka wekaPhase2Classifier2, 
                            Weka wekaPhase3Classifier1, Weka wekaPhase3Classifier2, Weka wekaPhase3Classifier3, 
                            Weka wekaPhase3Classifier4, boolean useFreeling) {
        super(useFreeling);

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

    @Override
    public String train(String fileName) {
        
        String tempFileName = copyFileToTempDir(fileName, true);
        fileName = tempFileName;
                
        if (useFreeling)
            fileName = preprocessUsingFreeling(fileName);
        
        Instances evaluationDataset;
        String modelFileName;
        
        // Fase 1
        String phase1FileName = phase1.getDataset(fileName);
        
        evaluationDataset = wekaPhase1Classifier.evaluate(phase1FileName);        
        modelFileName = phase1FileName.substring(0, phase1FileName.lastIndexOf(".arff")) + ".dat";
        wekaPhase1Classifier.train(evaluationDataset, modelFileName);
        
        phase1.setResults(wekaPhase1Classifier.getEvaluationResults());
        
        // Fase 2
        String phase2FileName = phase2.getDataset(fileName);

        // Clasificador 1
        evaluationDataset = wekaPhase2Classifier1.evaluate(phase2FileName, "2", "last");        
        modelFileName = phase2FileName.substring(0, phase2FileName.lastIndexOf(".arff")) + "-classifier1.dat";
        wekaPhase2Classifier1.train(evaluationDataset, modelFileName);
        
        // Clasificador 2        
        evaluationDataset = wekaPhase2Classifier2.evaluate(phase2FileName, "2", "first");                
        modelFileName = phase2FileName.substring(0, phase2FileName.lastIndexOf(".arff")) + "-classifier2.dat";
        wekaPhase2Classifier2.train(evaluationDataset, modelFileName);
        
        phase2.setResults(wekaPhase2Classifier1.getEvaluationResults() + wekaPhase2Classifier2.getEvaluationResults());
        
        // Fase 3
        String phase3FileName = phase3.getDataset(fileName);
        
        // Clasificador 1
        evaluationDataset = wekaPhase3Classifier1.evaluate(phase3FileName, "2", "2,3,4");
        modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(".arff")) + "-classifier1.dat";
        wekaPhase3Classifier1.train(evaluationDataset, modelFileName);
        
        // Clasificador 2
        evaluationDataset = wekaPhase3Classifier2.evaluate(phase3FileName, "2", "1,2,3");
        modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(".arff")) + "-classifier2.dat";
        wekaPhase3Classifier2.train(evaluationDataset, modelFileName);
        
        // Clasificador 3
        evaluationDataset = wekaPhase3Classifier3.evaluate(phase3FileName, "2", "1,2,4");
        modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(".arff")) + "-classifier3.dat";
        wekaPhase3Classifier3.train(evaluationDataset, modelFileName);
        
        // Clasificador 4
        evaluationDataset = wekaPhase3Classifier4.evaluate(phase3FileName, "2", "1,3,4");
        modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(".arff")) + "-classifier4.dat";
        wekaPhase3Classifier4.train(evaluationDataset, modelFileName);
     
        phase3.setResults(wekaPhase3Classifier1.getEvaluationResults() + wekaPhase3Classifier2.getEvaluationResults() + 
        				  wekaPhase3Classifier3.getEvaluationResults() + wekaPhase3Classifier4.getEvaluationResults());
        
        return tempFileName;
    }

    @Override
    public void classify(String fileName, String trainFileName) {

        String modelFileName = trainFileName.substring(0, trainFileName.lastIndexOf(".arff"));

        String tempFileName = copyFileToTempDir(fileName, false);
        fileName = tempFileName;
        
        fileName = Freeling.splitSentences(fileName);
        
        if (useFreeling) {
            fileName = preprocessUsingFreeling(fileName);
            modelFileName += "-freeling";
        }
        
        // Clasificación
        // Fase 1
        String phaseFileName = phase1.getDataset(fileName);
        String labeledFileNamePhase1 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase1.arff";
        String phase1Results = wekaPhase1Classifier.classify(modelFileName + "-phase1.dat", phaseFileName, labeledFileNamePhase1, "2");

        // Fase 2
        phaseFileName = phase2.getTrainDataset(labeledFileNamePhase1, true, "(classifier 1 and 2)");
        
        // Clasificador 1
        String labeledFileNamePhase2Classifier1 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase2-classifier1.arff";
        String phase2Classifier1Results = wekaPhase2Classifier1.classify(modelFileName + "-phase2-classifier1.dat", phaseFileName, labeledFileNamePhase2Classifier1, "3", "2", "last");
        // Clasificador 2
        String labeledFileNamePhase2Classifier2 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase2-classifier2.arff";
        String phase2Classifier2Results = wekaPhase2Classifier2.classify(modelFileName + "-phase2-classifier2.dat", phaseFileName, labeledFileNamePhase2Classifier2, "3", "2", "first");

        // Fase 3
        phaseFileName = phase3.getTrainDataset(labeledFileNamePhase2Classifier1, true, "(classifier 1 and 4)");
        // Clasificador 1
        String labeledFileNamePhase3Classifier1 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase3-classifier1.arff";
        String phase3Classifier1Results = wekaPhase3Classifier1.classify(modelFileName + "-phase3-classifier1.dat", phaseFileName, labeledFileNamePhase3Classifier1, "4", "2", "2,3,4");
        // Clasificador 4
        String labeledFileNamePhase3Classifier4 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase3-classifier4.arff";
        String phase3Classifier4Results = wekaPhase3Classifier4.classify(modelFileName + "-phase3-classifier4.dat", phaseFileName, labeledFileNamePhase3Classifier4, "4", "2", "1,3,4");
        
        phaseFileName = phase3.getTrainDataset(labeledFileNamePhase2Classifier2, true, "(classifier 2 and 3)");
        // Clasificador 2
        String labeledFileNamePhase3Classifier2 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase3-classifier2.arff";
        String phase3Classifier2Results = wekaPhase3Classifier2.classify(modelFileName + "-phase3-classifier2.dat", phaseFileName, labeledFileNamePhase3Classifier2, "4", "2", "1,2,3");
        // Clasificador 3
        String labeledFileNamePhase3Classifier3 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase3-classifier3.arff";
        String phase3Classifier3Results = wekaPhase3Classifier3.classify(modelFileName + "-phase3-classifier3.dat", phaseFileName, labeledFileNamePhase3Classifier3, "4", "2", "1,2,4");
        
        String fileHeader;
        String fileData;
        
        // Guardar resultados
        // Fase 1
        fileHeader = getResultHeader(phase1Results);
        fileData = getResultData(phase1Results);
        classifyingResults =      "===================\n      Phase 1\n===================\n" + fileHeader + "@data" + fileData;
        
        // Fase 2
        fileHeader = getResultHeader(phase2Classifier1Results);
        fileData = getResultData(phase2Classifier1Results);
        classifyingResults += "\n\n===================\n      Phase 2\n===================\n" + fileHeader + "@data" + fileData;
        fileData = getResultData(phase2Classifier2Results);
        classifyingResults += fileData;
        
        // Fase 3
        fileHeader = getResultHeader(phase3Classifier1Results);
        fileData = getResultData(phase3Classifier1Results);
        classifyingResults += "\n\n===================\n      Phase 3\n===================\n" + fileHeader + "@data" + fileData;
        fileData = getResultData(phase3Classifier2Results);
        classifyingResults += fileData;
        fileData = getResultData(phase3Classifier3Results);
        classifyingResults += fileData;
        fileData = getResultData(phase3Classifier4Results);
        classifyingResults += fileData;
    }
    
    private String getResultHeader(String result) {
    	
    	return result.substring(result.indexOf("@attribute"), result.indexOf("@data"));
    }
    
    private String getResultData(String result) {
    	
		return result.substring(result.indexOf("@data") + 5, result.length());
	}

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

    @Override
    public String getClassificationResults() {

        return classifyingResults;
    }
}
