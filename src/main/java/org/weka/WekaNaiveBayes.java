package org.weka;

import weka.classifiers.bayes.NaiveBayes;
import weka.filters.Filter;

public class WekaNaiveBayes extends Weka {

    private static final String NAIVE_BAYES_PROPERTY_NAME = "NaiveBayes";
    
    public WekaNaiveBayes(int folds, int nGramMin, int nGramMax) {
        super(new NaiveBayes(), folds, nGramMin, nGramMax);
    }

    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(NAIVE_BAYES_PROPERTY_NAME);
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
