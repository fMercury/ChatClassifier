package org.commons;

import java.io.File;

public class Constants {

    public static final String RESOURCES = "src/main/resources";
    public static final String TEMP_FOLDER = "temp/";
    
    public static String addTempFolderAndSuffix(String completeFileName, String suffix) {
        
        String newFileName;
        
        int lastPathSeparator = completeFileName.lastIndexOf(File.separator);
        String fileFolder = completeFileName.substring(0, lastPathSeparator);
        String fileName = completeFileName.substring(lastPathSeparator + 1, completeFileName.lastIndexOf(".arff"));
        newFileName = fileFolder + File.separator + Constants.TEMP_FOLDER + fileName + suffix;
        
        return newFileName;
    }
    
}
