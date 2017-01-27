package org.processDataset;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.commons.Constants;
import org.preprocessDataset.Freeling;

public abstract class ProcessDataset {
    
    protected String trainingResults;
    protected String classifyingResults;
    
    protected boolean useFreeling;
    protected Freeling freeling;
    
    protected boolean useEasyProcessing;;
    
    public ProcessDataset(boolean useFreeling, boolean useEasyProcessing) {
        
        this.useFreeling = useFreeling;        
        freeling = new Freeling();
        this.useEasyProcessing = useEasyProcessing;        
    }
    
    public abstract String train(String fileName);
    public abstract String classify(String fileName, String trainFileName);
    public abstract String getTrainingResults();
    public abstract String getClassificationResults();
    
    protected String preprocessUsingFreeling(String fileName) {
        
        return freeling.freelingAnalisys(fileName);
    }
    
    protected String copyFileToTempDir(String filePath, boolean deleteTempFolder, String stage) {
        
        int lastPathSeparator = filePath.lastIndexOf(File.separator);
        String fileFolder = filePath.substring(0, lastPathSeparator);
        String fileName = filePath.substring(lastPathSeparator + 1, filePath.length());
        String folderName = fileFolder + File.separator + Constants.TEMP_FOLDER + new SimpleDateFormat("yyyyMMdd HHmmssss").format(new Date()) + " " + stage + File.separator;
        String newFilePath = folderName + fileName;
        
        File folder = new File(folderName);
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
    
    public String getPreTrainnedModel() {
    	
    	return "";
    }
    
    
}
