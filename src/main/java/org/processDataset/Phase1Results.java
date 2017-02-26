package org.processDataset;

/**
 * Almacena los resutlados de la clasificación de la Fase 1
 * @author martinmineo
 *
 */
public class Phase1Results {

    private String classifierResults;
    private String labeledFileName;
    
    /**
     * Constructor 
     * @param classifierResults String Resultado de la clasificación
     * @param labeledFileName Archivo clasificado
     */
    public Phase1Results(String classifierResults, String labeledFileName) {
        
        this.classifierResults = classifierResults;
        this.labeledFileName = labeledFileName;
    }
    
    /**
     * Devuelve los resultados de la clasificación
     * @return String Resultados de la clasificación
     */
    public String getClassifierResults() {
        return classifierResults;
    }

    /**
     * Devuelve el archivo clasificado
     * @return String Archivo clasificado
     */
    public String getLabeledFileName() {
        return labeledFileName;
    }
}
