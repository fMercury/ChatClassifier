package org.weka;

import weka.classifiers.trees.REPTree;
import weka.filters.Filter;

public class WekaREPTree extends Weka {
    
    private static final String REPTREE_PROPERTY_NAME = "REPTree";

    public WekaREPTree(int folds, int nGramMin, int nGramMax) {
        super(new REPTree(), folds, nGramMin, nGramMax);
    }
    
    @Override
    public String getClassifierOptionDescription() {
        return properties.getProperty(REPTREE_PROPERTY_NAME);
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
