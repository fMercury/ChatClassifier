package org.weka;

import weka.classifiers.trees.J48;

/**
* Clasificador WEKA
* @author martinmineo
*
*/
public class WekaJ48 extends Weka {

    private static final String J48_PROPERTY_NAME = "J48";
    
    /**
     * Constructor
     * @param folds int Cantidad de 'folds'
     * @param nGramMin int nGram mínimo
     * @param nGramMax int nGram máximo
     */
    public WekaJ48(int folds, int nGramMin, int nGramMax) {
        super(new J48(), folds, nGramMin, nGramMax);
    }

    /**
     * Devuelve un String con las opciones por defecto del clasificador
     * @return String con las opciones por defecto del clasificador
     */
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(J48_PROPERTY_NAME);
    }
    
    /**
     * Devuelve el nombre del clasificador
     * @return String Nombre del clasificador 
     */
	@Override
	public String getClassifierClassName() {
		return (new J48()).getClass().getName();
	}
    
}
