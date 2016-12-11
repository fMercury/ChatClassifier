package org.weka;

import weka.classifiers.trees.J48;

public class WekaJ48 extends Weka {

    private static final String J48_PROPERTY_NAME = "J48";
    
    public WekaJ48(int folds, int nGramMin, int nGramMax) {
        super(new J48(), folds, nGramMin, nGramMax);
    }

    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(J48_PROPERTY_NAME);
    }

}
