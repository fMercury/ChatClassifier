package org.weka;

import weka.classifiers.trees.J48;

public class J48Technique extends MachineLearningTechnique {

    public J48Technique() {

        super(new J48());
    }

    @Override
    public String getResults() {
        // TODO Auto-generated method stub

        return ((J48) cls).toString();
    }

    @Override
    public String getValidOptions() {
        
        return "-U\nUse unpruned tree.\n\n-C <pruning confidence>\nSet confidence threshold for pruning. (default 0.25)\n\n-M <minimum number of instances>\nSet minimum number of instances per leaf. (default 2)\n\n-R\nUse reduced error pruning.\n\n-N <number of folds>\nSet number of folds for reduced error pruning. One fold is used as pruning set. (default 3)\n\n-B\nUse binary splits only.\n\n-S\nDon't perform subtree raising.\n\n-L\nDo not clean up after the tree has been built.\n\n-A\nLaplace smoothing for predicted probabilities.\n\n-Q <seed>\nSeed for random data shuffling (default 1).";
    }

}
