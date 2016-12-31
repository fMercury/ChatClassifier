package org.weka;

import weka.classifiers.rules.JRip;
import weka.filters.Filter;

public class WekaJRip extends Weka {

    private static final String JRIP_PROPERTY_NAME = "JRip";
    
    public WekaJRip(int folds, int nGramMin, int nGramMax) {
        super(new JRip(), folds, nGramMin, nGramMax);
    }

    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(JRIP_PROPERTY_NAME);
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
