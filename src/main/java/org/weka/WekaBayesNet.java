package org.weka;

import weka.classifiers.bayes.BayesNet;

/**
* Clasificador WEKA
* @author martinmineo
*
*/
public class WekaBayesNet extends Weka {
    
    private static final String BAYES_NET_PROPERTY_NAME = "BayesNet";

    /**
     * Constructor
     * @param folds int Cantidad de 'folds'
     * @param nGramMin int nGram mínimo
     * @param nGramMax int nGram máximo
     */
    public WekaBayesNet(int folds, int nGramMin, int nGramMax) {
        super(new BayesNet(), folds, nGramMin, nGramMax);
    }
    
    /**
     * Devuelve un String con las opciones por defecto del clasificador
     * @return String con las opciones por defecto del clasificador
     */
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(BAYES_NET_PROPERTY_NAME);
    }
    
    /**
     * Devuelve el nombre del clasificador
     * @return String Nombre del clasificador 
     */
	@Override
	public String getClassifierClassName() {
		return (new BayesNet()).getClass().getName();
	}
}
