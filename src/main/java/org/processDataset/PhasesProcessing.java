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
        phase1.setResults(wekaPhase1Classifier.getEvaluationResults());
        
        wekaPhase1Classifier.train(evaluationDataset);
        modelFileName = phase1FileName.substring(0, phase1FileName.lastIndexOf(".arff")) + ".dat";
        wekaPhase1Classifier.saveModel(modelFileName);
        
        // Fase 2
        String phase2FileName = phase2.getDataset(fileName);

        wekaPhase2Classifier1.filterByCathegory("2", "first");
        evaluationDataset = wekaPhase2Classifier1.evaluate(phase2FileName);
        phase2.setResults(wekaPhase2Classifier1.getEvaluationResults());
        
        wekaPhase2Classifier1.train(evaluationDataset);
        modelFileName = phase2FileName.substring(0, phase2FileName.lastIndexOf(".arff")) + "-phase2-classifier1.dat";
        wekaPhase2Classifier1.saveModel(modelFileName);
        
        
        wekaPhase2Classifier2.filterByCathegory("2", "last");
        evaluationDataset = wekaPhase2Classifier2.evaluate(phase2FileName);
        phase2.setResults(wekaPhase2Classifier2.getEvaluationResults());
        
        wekaPhase2Classifier2.train(evaluationDataset);
        modelFileName = phase2FileName.substring(0, phase2FileName.lastIndexOf(".arff")) + "-phase2-classifier2.dat";
        wekaPhase2Classifier2.saveModel(modelFileName);
        
        // Fase 3
        String phase3FileName = phase3.getDataset(fileName);
        
        wekaPhase3Classifier1.filterByCathegory("3", "first");
        evaluationDataset = wekaPhase3Classifier1.evaluate(phase3FileName);
        phase3.setResults(wekaPhase3Classifier1.getEvaluationResults());
        
        wekaPhase3Classifier1.train(evaluationDataset);
        modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(".arff")) + "-phase3-classifier1.dat";
        wekaPhase3Classifier1.saveModel(modelFileName);
        

        wekaPhase3Classifier2.filterByCathegory("3", "2");
        evaluationDataset = wekaPhase3Classifier2.evaluate(phase3FileName);
        phase3.setResults(wekaPhase3Classifier2.getEvaluationResults());
        
        wekaPhase3Classifier2.train(evaluationDataset);
        modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(".arff")) + "-phase3-classifier2.dat";
        wekaPhase3Classifier2.saveModel(modelFileName);
        
        
        wekaPhase3Classifier3.filterByCathegory("3", "3");
        evaluationDataset = wekaPhase3Classifier3.evaluate(phase3FileName);
        phase3.setResults(wekaPhase3Classifier3.getEvaluationResults());
        
        wekaPhase3Classifier3.train(evaluationDataset);
        modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(".arff")) + "-phase3-classifier3.dat";
        wekaPhase3Classifier3.saveModel(modelFileName);
        
        
        wekaPhase3Classifier4.filterByCathegory("3", "last");
        evaluationDataset = wekaPhase3Classifier4.evaluate(phase3FileName);
        phase3.setResults(wekaPhase3Classifier4.getEvaluationResults());
        
        wekaPhase3Classifier4.train(evaluationDataset);
        modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(".arff")) + "-phase3-classifier4.dat";
        wekaPhase3Classifier4.saveModel(modelFileName);

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
        
        // Fase 1
        String phaseFileName = phase1.getDataset(fileName);
        String labeledFileNamePhase1 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase1.arff";
        String phase1ClassifyingResults = wekaPhase1Classifier.classify(modelFileName + "-phase1.dat", phaseFileName, labeledFileNamePhase1, "2");

        // Fase 2
        phaseFileName = phase2.getTrainDataset(labeledFileNamePhase1, true);
        String labeledFileNamePhase2 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase2.arff";
//        String phase2ClassifyingResults = weka.classify(modelFileName + "-phase2.dat", phaseFileName, labeledFileNamePhase2, "3");

        // Fase 3
        phaseFileName = phase3.getTrainDataset(labeledFileNamePhase2, true);
        String labeledFileNamePhase3 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase3.arff";
//        String phase3ClassifyingResults = weka.classify(modelFileName + "-phase3.dat", phaseFileName, labeledFileNamePhase3, "4");
        
        classifyingResults = "===================\n      Phase 1\n===================\n" + phase1ClassifyingResults;
//        classifyingResults += "\n\n===================\n      Phase 2\n===================\n" + phase2ClassifyingResults;
//        classifyingResults += "\n\n===================\n      Phase 3\n===================\n" + phase3ClassifyingResults;
    }

    @Override
    public String getTrainingResults() {

        trainingResults = "===================\n      Phase 1\n===================\n" + phase1.getResults();
        trainingResults += "\n===================\n      Phase 2\n===================\n" + phase2.getResults();
        trainingResults += "\n===================\n      Phase 3\n===================\n" + phase3.getResults();

        return trainingResults;
    }

    @Override
    public String getClassificationResults() {

        return classifyingResults;
    }
}
