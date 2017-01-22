package org.processDataset;

public class Phase1Results {

    private String classifierResults;
    private String labeledFileName;
    
    public Phase1Results(String phase1ClassifierResults, String labeledFileNamePhase1) {
        
        this.classifierResults = phase1ClassifierResults;
        this.labeledFileName = labeledFileNamePhase1;
    }
    
    public String getClassifierResults() {
        return classifierResults;
    }

    public String getLabeledFileName() {
        return labeledFileName;
    }
}
