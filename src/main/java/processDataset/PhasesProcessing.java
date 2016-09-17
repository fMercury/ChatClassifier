package processDataset;

import org.weka.Weka;

import preprocessDataset.Freeling;
import weka.core.Instances;

public class PhasesProcessing extends ProcessDataset {

    private Phase1Processing phase1;
    private Phase2Processing phase2;
    private Phase3Processing phase3;
    
    public PhasesProcessing(Weka weka, boolean useFreeling) {
        super(weka, useFreeling);

        phase1 = new Phase1Processing(useFreeling);
        phase2 = new Phase2Processing(useFreeling);
        phase3 = new Phase3Processing(useFreeling);
    }

    @Override
    public void train(String fileName) {
                
        if (useFreeling)
            fileName = preprocessUsingFreeling(fileName);
        
        Instances evaluationDataset;
        String modelFileName;
        
        // Fase 1
        String phase1FileName = phase1.getDataset(fileName);
        
        evaluationDataset = weka.evaluate(phase1FileName);
        phase1.setResults(weka.getEvaluationResults());
        
        weka.train(evaluationDataset);
        modelFileName = phase1FileName.substring(0, phase1FileName.lastIndexOf(".arff")) + ".dat";
        weka.saveModel(modelFileName);
        
        // Fase 2
        String phase2FileName = phase2.getDataset(fileName);

        evaluationDataset = weka.evaluate(phase2FileName);
        phase2.setResults(weka.getEvaluationResults());
        
        weka.train(evaluationDataset);
        modelFileName = phase2FileName.substring(0, phase2FileName.lastIndexOf(".arff")) + ".dat";
        weka.saveModel(modelFileName);
        
        // Fase 3
        String phase3FileName = phase3.getDataset(fileName);

        evaluationDataset = weka.evaluate(phase3FileName);
        phase3.setResults(weka.getEvaluationResults());
        
        weka.train(evaluationDataset);
        modelFileName = phase3FileName.substring(0, phase3FileName.lastIndexOf(".arff")) + ".dat";
        weka.saveModel(modelFileName);
    }

    @Override
    public void classify(String fileName, String trainFileName) {

        String modelFileName = trainFileName.substring(0, trainFileName.lastIndexOf(".arff"));

        fileName = Freeling.splitSentences(fileName);
        
        if (useFreeling) {
            fileName = preprocessUsingFreeling(fileName);
            modelFileName += "-freeling";
        }
        
        // Fase 1
        String phaseFileName = phase1.getDataset(fileName);
        String labeledFileNamePhase1 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase1.arff";
        String phase1ClassifyingResults = weka.classify(modelFileName + "-phase1.dat", phaseFileName, labeledFileNamePhase1, "2");

        // Fase 2
        phaseFileName = phase2.getTrainDataset(labeledFileNamePhase1, true);
        String labeledFileNamePhase2 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase2.arff";
        String phase2ClassifyingResults = weka.classify(modelFileName + "-phase2.dat", phaseFileName, labeledFileNamePhase2, "3");

        // Fase 3
        phaseFileName = phase3.getTrainDataset(labeledFileNamePhase2, true);
        String labeledFileNamePhase3 = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled-phase3.arff";
        String phase3ClassifyingResults = weka.classify(modelFileName + "-phase3.dat", phaseFileName, labeledFileNamePhase3, "4");
        
        classifyingResults = "===================\n      Phase 1\n===================\n" + phase1ClassifyingResults;
        classifyingResults += "\n\n===================\n      Phase 2\n===================\n" + phase2ClassifyingResults;
        classifyingResults += "\n\n===================\n      Phase 3\n===================\n" + phase3ClassifyingResults;
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
