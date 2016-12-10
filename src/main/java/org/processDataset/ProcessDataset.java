package org.processDataset;

import org.preprocessDataset.Freeling;
import org.weka.Weka;

public abstract class ProcessDataset {
    
    protected String trainingResults;
    protected String classifyingResults;
    
    protected boolean useFreeling;
    protected Freeling freeling;
    
    protected Weka weka;
    
    public ProcessDataset(Weka weka, boolean useFreeling) {
        
        this.useFreeling = useFreeling;
        freeling = new Freeling();
        this.weka = weka;
    }
    
    public abstract void train(String fileName);
    public abstract void classify(String fileName, String trainFileName);
    public abstract String getTrainingResults();
    public abstract String getClassificationResults();
    
    protected String preprocessUsingFreeling(String fileName) {
        
        return freeling.freelingAnalisys(fileName);
    }
    
    
}
