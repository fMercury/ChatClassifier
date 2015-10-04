package org.controler;

import org.weka.MachineLearningTechnique;
import org.weka.J48Technique;
import org.weka.NaiveBayesTechnique;

public class WekaControler {

    private MachineLearningTechnique mlt;

    // Techniques
    private static final int J48 = 1;
    private static final int NAIVE_BAYES = 2;

    public WekaControler(int technique) {

        switch (technique) {
        case J48:
            mlt = new J48Technique();
            break;
        case NAIVE_BAYES:
            mlt = new NaiveBayesTechnique();
            break;
        }
    }

    public String trainClassifier(String sourceDataset, String options) {

        mlt.setDataset(sourceDataset);
        mlt.setOptions(options);
        mlt.trainClassifier();
        return mlt.getResults();
    }

    public StringBuilder getOptions() {

        String[] options = mlt.getOptions();
        
        StringBuilder builder = new StringBuilder();
        for (String s : options) {
            builder.append(s + " ");
        }
        return builder;
    }

    public String getValidOptions() {
        
        return mlt.getValidOptions();
    }
}
