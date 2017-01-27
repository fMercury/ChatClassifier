package org.weka;

import weka.classifiers.trees.REPTree;

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
	public String getClassifierClassName() {
		return (new REPTree()).getClass().getName();
	}
}
