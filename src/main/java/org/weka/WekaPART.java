package org.weka;

import weka.classifiers.rules.PART;

public class WekaPART extends Weka {
    
    private static final String PART_PROPERTY_NAME = "PART";

    public WekaPART(int folds, int nGramMin, int nGramMax) {
        super(new PART(), folds, nGramMin, nGramMax);
    }

    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(PART_PROPERTY_NAME);
    }

}
