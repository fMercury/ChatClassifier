package org.weka;

import weka.classifiers.trees.LMT;

/**
* Clasificador WEKA
* @author martinmineo
*
*/
public class WekaLMT extends Weka {

    private static final String LMT_PROPERTY_NAME = "LMT";
    
    /**
     * Constructor
     * @param folds int Cantidad de 'folds'
     * @param nGramMin int nGram mínimo
     * @param nGramMax int nGram máximo
     */
    public WekaLMT(int folds, int nGramMin, int nGramMax) {
        super(new LMT(), folds, nGramMin, nGramMax);
    }

    /**
     * Devuelve un String con las opciones por defecto del clasificador
     * @return String con las opciones por defecto del clasificador
     */
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(LMT_PROPERTY_NAME);
    }
    
    /**
     * Devuelve el nombre del clasificador
     * @return String Nombre del clasificador 
     */
	@Override
	public String getClassifierClassName() {
		return (new LMT()).getClass().getName();
	}

}
