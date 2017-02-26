package org.weka;

import weka.classifiers.rules.PART;

/**
* Clasificador WEKA
* @author martinmineo
*
*/
public class WekaPART extends Weka {
    
    private static final String PART_PROPERTY_NAME = "PART";

    /**
     * Constructor
     * @param folds int Cantidad de 'folds'
     * @param nGramMin int nGram mínimo
     * @param nGramMax int nGram máximo
     */
    public WekaPART(int folds, int nGramMin, int nGramMax) {
        super(new PART(), folds, nGramMin, nGramMax);
    }

    /**
     * Devuelve un String con las opciones por defecto del clasificador
     * @return String con las opciones por defecto del clasificador
     */
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(PART_PROPERTY_NAME);
    }

    /**
     * Devuelve el nombre del clasificador
     * @return String Nombre del clasificador 
     */
	@Override
	public String getClassifierClassName() {
		return (new PART()).getClass().getName();
	}
}
