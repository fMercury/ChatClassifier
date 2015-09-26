package org.weka;

import weka.classifiers.trees.J48;

public class J48Technique extends MachineLearningTechnique {

    public J48Technique(String sourceDataset, String options) {

        super(new J48(), sourceDataset, options);
    }

    @Override
    public String getResults() {
        // TODO Auto-generated method stub
        
        return ((J48)cls).toString();
    }

}
