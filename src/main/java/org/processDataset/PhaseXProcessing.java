package org.processDataset;

import java.util.ArrayList;

import weka.core.Attribute;

/**
 * clase abstracta que reune funcionalidad para el procesamiento en fases
 * @author martinmineo
 *
 */
public abstract class PhaseXProcessing {
    
    protected boolean useFreeling;
    private String results;
    protected int correctClassifiedInstances;
    protected int incorrectClassifiedInstances;
    
    /**
     * Constructor
     * @param useFreeling boolean Determina si se va a utilizar Freeling como pre-procesamiento o no
     */
    public PhaseXProcessing(boolean useFreeling) {

        this.useFreeling = useFreeling;
        correctClassifiedInstances = 0;
        incorrectClassifiedInstances = 0;
    }
    
    protected abstract ArrayList<Attribute> getAttributes(boolean includeName);
    protected abstract String getDataset(String fileName);
    protected abstract String getTrainDataset(String fileName, boolean includeName, String fileNameSufix);
    
    /**
     * Almacena los resultados de la evaluación del entrenamiento
     * @param results String Resultados
     */
    protected void setEvaluationResults(String results) {
        
        this.results = results;
    }
    
    /**
     * Devuelve los resultados de la evaluación del entrenamiento
     * @return String Resultados de la evaluación del entrenamiento
     */
    protected String getResults() {
        
        return results;
    }
}
