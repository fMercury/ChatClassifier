package org.weka;

import weka.classifiers.meta.LogitBoost;

/**
* Clasificador WEKA
* @author martinmineo
*
*/
public class WekaLogitBoost extends Weka {

    private static final String LOGIT_BOOST_PROPERTY_NAME = "LogitBoost";
    
    /**
     * Constructor
     * @param folds int Cantidad de 'folds'
     * @param nGramMin int nGram mínimo
     * @param nGramMax int nGram máximo
     */
    public WekaLogitBoost(int folds, int nGramMin, int nGramMax) {
        super(new LogitBoost(), folds, nGramMin, nGramMax);
    }

    /**
     * Devuelve un String con las opciones por defecto del clasificador
     * @return String con las opciones por defecto del clasificador
     */
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(LOGIT_BOOST_PROPERTY_NAME);
    }
    
    /**
     * Devuelve las opciones del clasificador
     * @return StringBuilder Opciones del clasificador
     */
    @Override
    public StringBuilder getClassifierOptions() {

        String[] options = classifier.getOptions();
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < options.length; i++) {
            String s = options[i];
            if (s.contains("\""))
                s = s.replace("\"", "\\\"");
            if (s.contains("weka."))
                s = "\"" + s + "\"";
            if(!s.contains("-batch-size"))
                builder.append(s + " ");
        }
        return builder;
    }

    /**
     * Devuelve el nombre del clasificador
     * @return String Nombre del clasificador 
     */
	@Override
	public String getClassifierClassName() {
		return (new LogitBoost()).getClass().getName();
	}
    
    
}
