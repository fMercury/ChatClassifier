package org.weka;

import weka.classifiers.bayes.NaiveBayesMultinomialUpdateable;
import weka.filters.supervised.attribute.NominalToBinary;

public class WekaNaiveBayesMultinomialUpdateable extends Weka {
    
    private static final String NBMU_PROPERTY_NAME = "NBMU";
    
    public WekaNaiveBayesMultinomialUpdateable(int folds, int nGramMin, int nGramMax) {
        super(new NaiveBayesMultinomialUpdateable(), folds, nGramMin, nGramMax);
        
        filtersList.add(new NominalToBinary());
    }

    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(NBMU_PROPERTY_NAME);
    }
    
}
