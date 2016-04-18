package org.weka;

import weka.classifiers.functions.SMO;

public class SMOClassifierTechnique extends MachineLearningClassifierTechnique {


    private static final String SMO_PROPERTY_NAME = "SMO";

    public SMOClassifierTechnique() {

        super();
        setClassifier(new SMO());
    }

    @Override
    public String getValidOptions() {

        return properties.getProperty(SMO_PROPERTY_NAME);
    }

}
