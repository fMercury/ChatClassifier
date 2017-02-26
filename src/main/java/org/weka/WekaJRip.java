package org.weka;

import weka.classifiers.rules.JRip;

/**
* Clasificador WEKA
* @author martinmineo
*
*/
public class WekaJRip extends Weka {

    private static final String JRIP_PROPERTY_NAME = "JRip";
    
    /**
     * Constructor
     * @param folds int Cantidad de 'folds'
     * @param nGramMin int nGram mínimo
     * @param nGramMax int nGram máximo
     */
    public WekaJRip(int folds, int nGramMin, int nGramMax) {
        super(new JRip(), folds, nGramMin, nGramMax);
    }

    /**
     * Devuelve un String con las opciones por defecto del clasificador
     * @return String con las opciones por defecto del clasificador
     */
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(JRIP_PROPERTY_NAME);
    }
    
    /**
     * Devuelve el nombre del clasificador
     * @return String Nombre del clasificador 
     */
	@Override
	public String getClassifierClassName() {
		return (new JRip()).getClass().getName();
	}
}
