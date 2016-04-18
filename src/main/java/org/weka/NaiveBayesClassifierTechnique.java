package org.weka;

import weka.classifiers.bayes.NaiveBayes;

public class NaiveBayesClassifierTechnique extends MachineLearningClassifierTechnique {

    private static final String NAIVE_BAYES_PROPERTY_NAME = "NaiveBayes";

    public NaiveBayesClassifierTechnique() {

        super();
        setClassifier(new NaiveBayes());
    }

    @Override
    public String getValidOptions() {

        return properties.getProperty(NAIVE_BAYES_PROPERTY_NAME);
    }

}
