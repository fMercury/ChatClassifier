package org.processDataset;

/**
 * Almacena los resutlados de la clasificación de la Fase 3
 * @author martinmineo
 *
 */
public class Phase3Results {

    private String classifier1Results;
    private String classifier2Results;
    private String classifier3Results;
    private String classifier4Results;
    
    private String labeledFileNameClassifier1;
    private String labeledFileNameClassifier2;
    private String labeledFileNameClassifier3;
    private String labeledFileNameClassifier4;
    
    /**
     * Constructor
     * @param classifier1Results String Resultado de la clasificación del clasificador 1
     * @param classifier2Results String Resultado de la clasificación del clasificador 2
     * @param classifier3Results String Resultado de la clasificación del clasificador 3
     * @param classifier4Results String Resultado de la clasificación del clasificador 4
     * @param labeledFileNameClassifier1 String Archivo clasificado con el clasificador 1
     * @param labeledFileNameClassifier2 String Archivo clasificado con el clasificador 2
     * @param labeledFileNameClassifier3 String Archivo clasificado con el clasificador 3
     * @param labeledFileNameClassifier4 String Archivo clasificado con el clasificador 4
     */
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
     * Devuelve los resultados de la clasificación del clasificador 3
     * @return String Resultados de la clasificación del clasificador 3
     */
    public String getClassifier3Results() {
        return classifier3Results;
    }
    
    /**
     * Devuelve los resultados de la clasificación del clasificador 4
     * @return String Resultados de la clasificación del clasificador 4
     */
    public String getClassifier4Results() {
        return classifier4Results;
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
    
    /**
     * Devuelve el archivo clasificado por el clasificador 3
     * @return String Archivo clasificado
     */
    public String getLabeledFileNameClassifier3() {
        return labeledFileNameClassifier3;
    }
    
    /**
     * Devuelve el archivo clasificado por el clasificador 4
     * @return String Archivo clasificado
     */
    public String getLabeledFileNameClassifier4() {
        return labeledFileNameClassifier4;
    }
}
