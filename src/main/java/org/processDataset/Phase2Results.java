package org.processDataset;

public class Phase2Results {

    private String classifier1Results;
    private String classifier2Results;
    
    private String labeledFileNameClassifier1;
    private String labeledFileNameClassifier2;
    
    public Phase2Results (String classifier1Results, String classifier2Results, String labeledFileNameClassifier1, String labeledFileNameClassifier2) {
        
        this.classifier1Results = classifier1Results;
        this.classifier2Results = classifier2Results;
        
        this.labeledFileNameClassifier1 = labeledFileNameClassifier1;
        this.labeledFileNameClassifier2 = labeledFileNameClassifier2;
    }
    
    public String getClassifier1Results() {
        return classifier1Results;
    }
    
    public String getClassifier2Results() {
        return classifier2Results;
    }

    public String getLabeledFileNameClassifier1() {
        return labeledFileNameClassifier1;
    }

    public String getLabeledFileNameClassifier2() {
        return labeledFileNameClassifier2;
    }
    
}


