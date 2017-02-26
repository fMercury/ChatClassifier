package org.weka;

import weka.classifiers.functions.SMO;

/**
 * Clasificador WEKA
 * @author martinmineo
 *
 */
public class WekaSMO extends Weka {

    private static final String SMO_PROPERTY_NAME = "SMO";
    
    /**
     * Constructor
     * @param folds int Cantidad de 'folds'
     * @param nGramMin int nGram mínimo
     * @param nGramMax int nGram máximo
     */
    public WekaSMO(int folds, int nGramMin, int nGramMax) {
        super(new SMO(), folds, nGramMin, nGramMax);
    }

    /**
     * Devuelve un String con las opciones por defecto del clasificador
     * @return String con las opciones por defecto del clasificador
     */
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(SMO_PROPERTY_NAME);
    }

    /**
     * Devuelve el nombre del clasificador
     * @return String Nombre del clasificador 
     */
	@Override
	public String getClassifierClassName() {
		return (new SMO()).getClass().getName();
	}

}
