package org.processDataset;

/**
 * Almacena los resutlados de la clasificación de la Fase 2
 * @author martinmineo
 *
 */
public class Phase2Results {

    private String classifier1Results;
    private String classifier2Results;
    
    private String labeledFileNameClassifier1;
    private String labeledFileNameClassifier2;
    
    /**
     * Constructor
     * @param classifier1Results String Resultado de la clasificación del clasificador 1
     * @param classifier2Results String Resultado de la clasificación del clasificador 2
     * @param labeledFileNameClassifier1 String Archivo clasificado con el clasificador 1
     * @param labeledFileNameClassifier2 String Archivo clasificado con el clasificador 2
     */
    public Phase2Results (String classifier1Results, String classifier2Results, String labeledFileNameClassifier1, String labeledFileNameClassifier2) {
        
        this.classifier1Results = classifier1Results;
        this.classifier2Results = classifier2Results;
        
        this.labeledFileNameClassifier1 = labeledFileNameClassifier1;
        this.labeledFileNameClassifier2 = labeledFileNameClassifier2;
    }
    
    /**
     * Devuelve los resultados de la clasificación del clasificador 1
     * @return String Resultados de la clasificación del clasificador 1
     */
    public String getClassifier1Results() {
        return classifier1Results;
    }
    
    /**
     * Devuelve los resultados de la clasificación del clasificador 2
     * @return String Resultados de la clasificación del clasificador 2
     */
    public String getClassifier2Results() {
        return classifier2Results;
    }

    /**
     * Devuelve el archivo clasificado por el clasificador 1
     * @return String Archivo clasificado
     */
    public String getLabeledFileNameClassifier1() {
        return labeledFileNameClassifier1;
    }

    /**
     * Devuelve el archivo clasificado por el clasificador 2
     * @return String Archivo clasificado
     */
    public String getLabeledFileNameClassifier2() {
        return labeledFileNameClassifier2;
    }
    
}


