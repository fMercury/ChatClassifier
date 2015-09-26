package org.weka;

import weka.classifiers.bayes.NaiveBayes;

public class NaiveBayesTechnique extends MachineLearningTechnique {

    public NaiveBayesTechnique(String sourceDataset, String options) {
        
        super(new NaiveBayes(), sourceDataset, options);
    }

    @Override
    public String getResults() {
        // TODO Auto-generated method stub
        
        return ((NaiveBayes)cls).toString();
    }

}
