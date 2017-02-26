package org.weka;

import weka.classifiers.lazy.KStar;

/**
* Clasificador WEKA
* @author martinmineo
*
*/
public class WekaKStar extends Weka {

    private static final String KSTAR_PROPERTY_NAME = "KStar";
    
    /**
     * Constructor
     * @param folds int Cantidad de 'folds'
     * @param nGramMin int nGram mínimo
     * @param nGramMax int nGram máximo
     */
    public WekaKStar(int folds, int nGramMin, int nGramMax) {
        super(new KStar(), folds, nGramMin, nGramMax);
    }

    /**
     * Devuelve un String con las opciones por defecto del clasificador
     * @return String con las opciones por defecto del clasificador
     */
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(KSTAR_PROPERTY_NAME);
    }
    
    /**
     * Devuelve el nombre del clasificador
     * @return String Nombre del clasificador 
     */
	@Override
	public String getClassifierClassName() {
		return (new KStar()).getClass().getName();
	}
}
