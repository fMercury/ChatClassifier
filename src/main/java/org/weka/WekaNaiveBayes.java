package org.weka;

import weka.classifiers.bayes.NaiveBayes;

/**
* Clasificador WEKA
* @author martinmineo
*
*/
public class WekaNaiveBayes extends Weka {

    private static final String NAIVE_BAYES_PROPERTY_NAME = "NaiveBayes";
    
    /**
     * Constructor
     * @param folds int Cantidad de 'folds'
     * @param nGramMin int nGram mínimo
     * @param nGramMax int nGram máximo
     */
    public WekaNaiveBayes(int folds, int nGramMin, int nGramMax) {
        super(new NaiveBayes(), folds, nGramMin, nGramMax);
    }

    /**
     * Devuelve un String con las opciones por defecto del clasificador
     * @return String con las opciones por defecto del clasificador
     */
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(NAIVE_BAYES_PROPERTY_NAME);
    }
    
    /**
     * Devuelve el nombre del clasificador
     * @return String Nombre del clasificador 
     */
	@Override
	public String getClassifierClassName() {
		return (new NaiveBayes()).getClass().getName();
	}
}
