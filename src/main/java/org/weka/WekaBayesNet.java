package org.weka;

import weka.classifiers.bayes.BayesNet;

public class WekaBayesNet extends Weka {
    
    private static final String BAYES_NET_PROPERTY_NAME = "BayesNet";

    public WekaBayesNet(int folds, int nGramMin, int nGramMax) {
        super(new BayesNet(), folds, nGramMin, nGramMax);
    }
    
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(BAYES_NET_PROPERTY_NAME);
    }

}
