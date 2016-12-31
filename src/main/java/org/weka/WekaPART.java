package org.weka;

import weka.classifiers.rules.PART;
import weka.filters.Filter;

public class WekaPART extends Weka {
    
    private static final String PART_PROPERTY_NAME = "PART";

    public WekaPART(int folds, int nGramMin, int nGramMax) {
        super(new PART(), folds, nGramMin, nGramMax);
    }

    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(PART_PROPERTY_NAME);
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
