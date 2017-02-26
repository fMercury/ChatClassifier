package org.weka;

import weka.classifiers.rules.DecisionTable;

/**
* Clasificador WEKA
* @author martinmineo
*
*/
public class WekaDecisionTable extends Weka {
    
    private static final String DECISION_TABLE_PROPERTY_NAME = "DecisionTable";

    /**
     * Constructor
     * @param folds int Cantidad de 'folds'
     * @param nGramMin int nGram mínimo
     * @param nGramMax int nGram máximo
     */
    public WekaDecisionTable(int folds, int nGramMin, int nGramMax) {
        super(new DecisionTable(), folds, nGramMin, nGramMax);
    }
    
    /**
     * Devuelve un String con las opciones por defecto del clasificador
     * @return String con las opciones por defecto del clasificador
     */
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(DECISION_TABLE_PROPERTY_NAME);
    }

    /**
     * Devuelve el nombre del clasificador
     * @return String Nombre del clasificador 
     */
	@Override
	public String getClassifierClassName() {
		return (new DecisionTable()).getClass().getName();
	}
}
