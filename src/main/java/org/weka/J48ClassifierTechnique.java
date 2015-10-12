package org.weka;

import weka.classifiers.trees.J48;

public class J48ClassifierTechnique extends MachineLearningClassifierTechnique {

    private static final String J48_PROPERTY_NAME = "J48";

    public J48ClassifierTechnique() {

        super();
        setClassifier(new J48());
    }

    @Override
    public String getTrainResults() {
        // TODO Auto-generated method stub

        return ((J48) cls).toString();
    }

    @Override
    public String getValidOptions() {

        return properties.getProperty(J48_PROPERTY_NAME);
    }

}
