package processDataset;

import org.weka.Weka;

import preprocessDataset.Freeling;

public abstract class ProcessDataset {
    
    String trainingResults;
    String classifyingResults;
    
    boolean useFreeling;
    Freeling freeling;
    
    Weka weka;
    
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
