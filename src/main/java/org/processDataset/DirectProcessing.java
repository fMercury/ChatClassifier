package org.processDataset;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.commons.Constants;
import org.preprocessDataset.Freeling;
import org.weka.Weka;

import weka.core.Instances;

public class DirectProcessing extends ProcessDataset {
    
    private Weka wekaClassifier;
    
    public DirectProcessing(Weka weka, boolean useFreeling, boolean useEasyProcessing) {
        super(useFreeling, useEasyProcessing);
        
        this.wekaClassifier = weka; 
    }

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
        
        String destFileName = Constants.LABELED_FOLDER + new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()) + File.separator + originalFileName.substring(originalFileName.lastIndexOf(File.separator), originalFileName.length());
        Weka.copyDataset(labeledFileName, destFileName);
        
        return destFileName;
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
