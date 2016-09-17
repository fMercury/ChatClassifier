package processDataset;

import org.weka.Weka;

import preprocessDataset.Freeling;
import weka.core.Instances;

public class DirectProcessing extends ProcessDataset {
    
    public DirectProcessing(Weka weka, boolean useFreeling) {
        super(weka, useFreeling);
        
        this.useFreeling = useFreeling;
    }

    @Override
    public void train(String fileName) {
        
        if (useFreeling)
            fileName = preprocessUsingFreeling(fileName);
        
        Instances trainDataset = weka.evaluate(fileName);
        trainingResults = weka.getEvaluationResults();
        
        weka.train(trainDataset);
        String modelFileName = fileName.substring(0, fileName.lastIndexOf(".arff")) + ".dat";
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
        
        String labeledFileName = fileName.substring(0, fileName.lastIndexOf(".arff")) + "-labeled.arff";
        classifyingResults = weka.classify(modelFileName + ".dat", fileName, labeledFileName, "2");
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
