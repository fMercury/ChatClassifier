package org.weka;

import weka.classifiers.trees.REPTree;

/**
* Clasificador WEKA
* @author martinmineo
*
*/
public class WekaREPTree extends Weka {
    
    private static final String REPTREE_PROPERTY_NAME = "REPTree";

    /**
     * Constructor
     * @param folds int Cantidad de 'folds'
     * @param nGramMin int nGram mínimo
     * @param nGramMax int nGram máximo
     */
    public WekaREPTree(int folds, int nGramMin, int nGramMax) {
        super(new REPTree(), folds, nGramMin, nGramMax);
    }
    
    /**
     * Devuelve un String con las opciones por defecto del clasificador
     * @return String con las opciones por defecto del clasificador
     */
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(REPTREE_PROPERTY_NAME);
    }
    
    /**
     * Devuelve el nombre del clasificador
     * @return String Nombre del clasificador 
     */
	@Override
	public String getClassifierClassName() {
		return (new REPTree()).getClass().getName();
	}
}
