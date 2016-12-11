package org.weka;

import weka.classifiers.trees.LMT;

public class WekaLMT extends Weka {

    private static final String LMT_PROPERTY_NAME = "LMT";
    
    public WekaLMT(int folds, int nGramMin, int nGramMax) {
        super(new LMT(), folds, nGramMin, nGramMax);
    }

    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(LMT_PROPERTY_NAME);
    }

}
