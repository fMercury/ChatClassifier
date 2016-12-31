package org.weka;

import weka.classifiers.functions.SMO;
import weka.filters.Filter;

public class WekaSMO extends Weka {

    private static final String SMO_PROPERTY_NAME = "SMO";
    
    public WekaSMO(int folds, int nGramMin, int nGramMax) {
        super(new SMO(), folds, nGramMin, nGramMax);
    }

    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(SMO_PROPERTY_NAME);
    }
    
    @Override
    protected boolean hasSpecialFilter() {
        return false;
    }
    
    @Override
    protected Filter getSpecialFilter() {
         return null;
    }

}
