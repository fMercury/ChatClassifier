package org.weka;

import weka.classifiers.lazy.IBk;
import weka.filters.Filter;

public class WekaIBk extends Weka {

    private static final String IBk_PROPERTY_NAME = "IBk";
    
    public WekaIBk(int folds, int nGramMin, int nGramMax) {
        super(new IBk(), folds, nGramMin, nGramMax);
    }

    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(IBk_PROPERTY_NAME);
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
