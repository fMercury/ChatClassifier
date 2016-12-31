package org.weka;

import weka.classifiers.trees.LMT;
import weka.filters.Filter;

public class WekaLMT extends Weka {

    private static final String LMT_PROPERTY_NAME = "LMT";
    
    public WekaLMT(int folds, int nGramMin, int nGramMax) {
        super(new LMT(), folds, nGramMin, nGramMax);
    }

    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(LMT_PROPERTY_NAME);
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
