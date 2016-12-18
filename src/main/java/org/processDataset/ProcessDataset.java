package org.processDataset;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.commons.Constants;
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
    
    public abstract String train(String fileName);
    public abstract void classify(String fileName, String trainFileName);
    public abstract String getTrainingResults();
    public abstract String getClassificationResults();
    
    protected String preprocessUsingFreeling(String fileName) {
        
        return freeling.freelingAnalisys(fileName);
    }
    
    protected String copyFileToTempDir(String filePath, boolean deleteTempFolder) {
        
        int lastPathSeparator = filePath.lastIndexOf(File.separator);
        String fileFolder = filePath.substring(0, lastPathSeparator);
        String fileName = filePath.substring(lastPathSeparator + 1, filePath.length());
        String newFilePath = fileFolder + File.separator + Constants.TEMP_FOLDER + fileName;
        
        File folder = new File(fileFolder + File.separator + Constants.TEMP_FOLDER);
        File source = new File(filePath);
        File dest = new File(newFilePath);
        try {
            if (folder.exists() && deleteTempFolder)
                FileUtils.deleteDirectory(folder);
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return newFilePath;
    }
    
    
}
