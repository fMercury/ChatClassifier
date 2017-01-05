package org.processDataset;

import java.util.ArrayList;

import weka.core.Attribute;

public abstract class PhaseXProcessing {
    
    protected boolean useFreeling;
    private String results;
    protected int correctClassifiedInstances;
    protected int incorrectClassifiedInstances;
    
    public PhaseXProcessing(boolean useFreeling) {

        this.useFreeling = useFreeling;
        correctClassifiedInstances = 0;
        incorrectClassifiedInstances = 0;
    }
    
    protected abstract ArrayList<Attribute> getAttributes(boolean includeName);
    protected abstract String getDataset(String fileName);
    protected abstract String getTrainDataset(String fileName, boolean includeName, String fileNameSufix);
    
    protected void setResults(String results) {
        
        this.results = results;
    }
    
    protected String getResults() {
        
        return results;
    }
}
