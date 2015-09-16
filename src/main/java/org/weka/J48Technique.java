package org.weka;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;

public class J48Technique extends MachineLearningTechnique {

    public J48Technique(Classifier cls) {

        super();
        cls = new J48();
    }

    @Override
    public String getResults() {
        // TODO Auto-generated method stub
        return null;
    }

}
