package org.processDataset;

import org.preprocessDataset.Freeling;
import org.weka.Weka;

import weka.core.Instances;

public class DirectProcessing extends ProcessDataset {
    
    private Weka wekaClassifier;
    
    public DirectProcessing(Weka weka, boolean useFreeling) {
        super(useFreeling);
        
        this.wekaClassifier = weka; 
        this.useFreeling = useFreeling;
    }

    @Override
    public String train(String fileName) {
        
        String tempFileName = copyFileToTempDir(fileName, true);
        fileName = tempFileName;
        
        if (useFreeling)
            fileName = preprocessUsingFreeling(fileName);
        
        Instances trainDataset = wekaClassifier.evaluate(fileName);
        trainingResults = wekaClassifier.getEvaluationResults();
        
        String modelFileName = fileName.substring(0, fileName.lastIndexOf(".arff")) + ".dat";
        wekaClassifier.train(trainDataset, modelFileName);
        
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
        
        String labeledFileName = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled.arff";
        classifyingResults = wekaClassifier.classify(modelFileName + ".dat", fileName, labeledFileName, "2");
    }

    @Override
    public String getTrainingResults() {
      
        return trainingResults;
    }

    @Override
    public String getClassificationResults() {

        return classifyingResults;
    }

}
