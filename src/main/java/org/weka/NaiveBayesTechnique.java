package org.weka;

import weka.classifiers.bayes.NaiveBayes;

public class NaiveBayesTechnique extends MachineLearningTechnique {

    public NaiveBayesTechnique() {
        
        super(new NaiveBayes());
    }

    @Override
    public String getResults() {
        // TODO Auto-generated method stub
        
        return ((NaiveBayes)cls).toString();
    }

    @Override
    public String getValidOptions() {
        
        return "-K\nUse kernel density estimator rather than normal distribution for numeric attributes\n\n-D\nUse supervised discretization to process numeric attributes\n\n-O\nDisplay model in old format (good when there are many classes)";
    }

}
