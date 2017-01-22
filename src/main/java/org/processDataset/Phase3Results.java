package org.processDataset;

public class Phase3Results {

    private String classifier1Results;
    private String classifier2Results;
    private String classifier3Results;
    private String classifier4Results;
    
    private String labeledFileNameClassifier1;
    private String labeledFileNameClassifier2;
    private String labeledFileNameClassifier3;
    private String labeledFileNameClassifier4;
    
    
    public Phase3Results(String classifier1Results, String classifier2Results, String classifier3Results, String classifier4Results, String labeledFileNameClassifier1, String labeledFileNameClassifier2, String labeledFileNameClassifier3, String labeledFileNameClassifier4) {

        this.classifier1Results = classifier1Results;
        this.classifier2Results = classifier2Results;
        this.classifier3Results = classifier3Results;
        this.classifier4Results = classifier4Results;
        
        this.labeledFileNameClassifier1 = labeledFileNameClassifier1;
        this.labeledFileNameClassifier2 = labeledFileNameClassifier2;
        this.labeledFileNameClassifier3 = labeledFileNameClassifier3;
        this.labeledFileNameClassifier4 = labeledFileNameClassifier4;
    }
    
    public String getClassifier1Results() {
        return classifier1Results;
    }
    
    public String getClassifier2Results() {
        return classifier2Results;
    }
    
    public String getClassifier3Results() {
        return classifier3Results;
    }
    
    public String getClassifier4Results() {
        return classifier4Results;
    }
    
    public String getLabeledFileNameClassifier1() {
        return labeledFileNameClassifier1;
    }
    
    public String getLabeledFileNameClassifier2() {
        return labeledFileNameClassifier2;
    }
    
    public String getLabeledFileNameClassifier3() {
        return labeledFileNameClassifier3;
    }
    
    public String getLabeledFileNameClassifier4() {
        return labeledFileNameClassifier4;
    }
}
