package org.weka;

import weka.classifiers.lazy.IBk;

/**
* Clasificador WEKA
* @author martinmineo
*
*/
public class WekaIBk extends Weka {

    private static final String IBk_PROPERTY_NAME = "IBk";
    
    /**
     * Constructor
     * @param folds int Cantidad de 'folds'
     * @param nGramMin int nGram mínimo
     * @param nGramMax int nGram máximo
     */
    public WekaIBk(int folds, int nGramMin, int nGramMax) {
        super(new IBk(), folds, nGramMin, nGramMax);
    }

    /**
     * Devuelve un String con las opciones por defecto del clasificador
     * @return String con las opciones por defecto del clasificador
     */
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(IBk_PROPERTY_NAME);
    }
    
    /**
     * Devuelve el nombre del clasificador
     * @return String Nombre del clasificador 
     */
	@Override
	public String getClassifierClassName() {
		return (new IBk()).getClass().getName();
	}
}
