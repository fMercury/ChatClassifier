package org.weka;

import weka.classifiers.bayes.NaiveBayesMultinomialUpdateable;
import weka.filters.supervised.attribute.NominalToBinary;

/**
* Clasificador WEKA
* @author martinmineo
*
*/
public class WekaNaiveBayesMultinomialUpdateable extends Weka {
    
    private static final String NBMU_PROPERTY_NAME = "NBMU";
    
    /**
     * Constructor
     * @param folds int Cantidad de 'folds'
     * @param nGramMin int nGram mínimo
     * @param nGramMax int nGram máximo
     */
    public WekaNaiveBayesMultinomialUpdateable(int folds, int nGramMin, int nGramMax) {
        super(new NaiveBayesMultinomialUpdateable(), folds, nGramMin, nGramMax);
        
        trainingFiltersList.add(new NominalToBinary());
    }

    /**
     * Devuelve un String con las opciones por defecto del clasificador
     * @return String con las opciones por defecto del clasificador
     */
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(NBMU_PROPERTY_NAME);
    }
    
    /**
     * Devuelve el nombre del clasificador
     * @return String Nombre del clasificador 
     */
	@Override
	public String getClassifierClassName() {
		return (new NaiveBayesMultinomialUpdateable()).getClass().getName();
	}
}
